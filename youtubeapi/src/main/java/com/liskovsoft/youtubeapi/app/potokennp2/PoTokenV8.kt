package com.liskovsoft.youtubeapi.app.potokennp2

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.sharedutils.okhttp.OkHttpManager
import com.liskovsoft.youtubeapi.app.nsigsolver.common.loadScript
import com.liskovsoft.youtubeapi.app.potokennp2.misc.V8Wrapper
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import io.reactivex.SingleEmitter
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

private const val potLibPrefix = "potokennp/"
private const val solverLibPrefix = "nsigsolver/"

@RequiresApi(19)
internal class PoTokenV8 private constructor(
    context: Context,
    private var onInitDone: () -> Unit
) : PoTokenGenerator {
    private val v8Wrapper: V8Wrapper = V8Wrapper()
    private val poTokenEmitters = mutableListOf<Pair<String, (String) -> Unit>>()
    private var expirationMs: Long = -1
    var initError: Throwable? = null
    private val v8NpmLibFilenames = listOf("${solverLibPrefix}polyfill.js", "${potLibPrefix}jsdom.js", "${potLibPrefix}po_token.js")

    //region Initialization
    init {
        // load common libs: jsdom, polyfill
        v8Wrapper.registerJavaMethod(
            { _, parameters ->

                val botguardResponse = parameters.getString(0)

                onRunBotguardResult(botguardResponse)
            },
            "onRunBotguardResult"
        )
        v8Wrapper.registerJavaMethod(
            { _, parameters ->

                val error = parameters.getString(0)

                onJsInitializationError(error)
            },
            "onJsInitializationError"
        )
        v8Wrapper.registerJavaMethod(
            { _, parameters ->

                val identifier = parameters.getString(0)
                val poTokenU8 = parameters.getString(1)

                onObtainPoTokenResult(identifier, poTokenU8)
            },
            "onObtainPoTokenResult"
        )
        v8Wrapper.registerJavaMethod(
            { _, parameters ->

                val identifier = parameters.getString(0)
                val error = parameters.getString(1)

                onObtainPoTokenError(identifier, error)
            },
            "onObtainPoTokenError"
        )
    }

    /**
     * Must be called right after instantiating [PoTokenV8] to perform the actual
     * initialization. This will asynchronously go through all the steps needed to load BotGuard,
     * run it, and obtain an `integrityToken`.
     */
    private fun loadScriptAndObtainBotguard() {
        Log.d(TAG, "loadHtmlAndObtainBotguard() called")

        v8Wrapper.executeVoidScript(loadScript(v8NpmLibFilenames))
        downloadAndRunBotguard()
    }

    /**
     * Called during initialization by the JavaScript snippet appended to the HTML page content in
     * [loadScriptAndObtainBotguard] after the WebView content has been loaded.
     */
    private fun downloadAndRunBotguard() {
        Log.d(TAG, "downloadAndRunBotguard() called")

        val client = AppClient.WEB

        val responseBody = makeBotguardServiceRequest(
            "https://www.youtube.com/youtubei/v1/att/get?prettyPrint=false",
            """
                {
                            context: {
                                client: {
                                    clientName: "${client.clientName}",
                                    clientVersion: "${client.clientVersion}",
                                },
                            },
                            engagementType: "ENGAGEMENT_TYPE_UNBOUND",
                 }
            """,
            mapOf(
                "Content-Type" to "application/json"
            )
        ) ?: return

        val parsedChallengeData = parseDescrambledChallengeData(responseBody)

        runOnMainThread {
            v8Wrapper.executeVoidScript(
                """
                    try {
                        data = $parsedChallengeData;
                        runBotGuard(data).then(function (result) {
                            this.webPoSignalOutput = result.webPoSignalOutput;
                            onRunBotguardResult(result.botguardResponse);
                        }, function (error) {
                            onJsInitializationError(error + "\n" + error.stack);
                        });
                    } catch (error) {
                        onJsInitializationError(error + "\n" + error.stack);
                    }
                """
            )
        }
    }

    /**
     * Called during initialization by the JavaScript snippets from either
     * [downloadAndRunBotguard] or [onRunBotguardResult].
     */
    private fun onJsInitializationError(error: String) {
        val msg = "onJsInitializationError: $error"
        Log.e(TAG, msg)
        onInitializationErrorCloseAndCancel(buildExceptionForJsError(msg))
    }

    /**
     * Called during initialization by the JavaScript snippet from [downloadAndRunBotguard] after
     * obtaining the BotGuard execution output [botguardResponse].
     */
    private fun onRunBotguardResult(botguardResponse: String) {
        Log.d(TAG, "botguardResponse: $botguardResponse")

        val responseBody = makeBotguardServiceRequest(
            "$BASE_URL/\$rpc/google.internal.waa.v1.Waa/GenerateIT",
            "[ \"$REQUEST_KEY\", \"$botguardResponse\" ]",
        ) ?: return

        Log.d(TAG, "GenerateIT response: $responseBody")
        val (integrityToken, expirationTimeInSeconds) = parseIntegrityTokenData(responseBody)

        // MOD: backport Instant.now().plusSeconds
        // leave 10 minutes of margin just to be sure
        //expirationInstant = Instant.now().plusSeconds(expirationTimeInSeconds - 600)
        expirationMs = System.currentTimeMillis() + ((expirationTimeInSeconds - 600) * 1_000)

        runOnMainThread {
            v8Wrapper.executeVoidScript(
                "this.integrityToken = $integrityToken"
            )

            Log.d(TAG, "initialization finished, expiration=${expirationTimeInSeconds}s")
            onInitDone()
        }
    }
    //endregion

    //region Obtaining poTokens
    override fun generatePoToken(identifier: String): String {
        Log.d(TAG, "generatePoToken() called with identifier $identifier")
        lateinit var pot: String

        addPoTokenEmitter(identifier) {
            pot = it
        }

        val u8Identifier = stringToU8(identifier)

        runOnMainThread {
            v8Wrapper.executeVoidScript(
                """
                        try {
                            identifier = "$identifier";
                            u8Identifier = $u8Identifier;
                            poTokenU8 = obtainPoToken(webPoSignalOutput, integrityToken, u8Identifier);
                            poTokenU8String = "";
                            for (i = 0; i < poTokenU8.length; i++) {
                                if (i != 0) poTokenU8String += ",";
                                poTokenU8String += poTokenU8[i];
                            }
                            onObtainPoTokenResult(identifier, poTokenU8String);
                        } catch (error) {
                            onObtainPoTokenError(identifier, error + "\n" + error.stack);
                        }
                    """,
            )
        }

        initError?.let { throw it }

        return pot
    }

    /**
     * Called by the JavaScript snippet from [generatePoToken] when an error occurs in calling the
     * JavaScript `obtainPoToken()` function.
     */
    fun onObtainPoTokenError(identifier: String, error: String) {
        val msg = "onObtainPoTokenError: identifier=$identifier error=$error"
        Log.e(TAG, msg)
        onInitializationErrorCloseAndCancel(buildExceptionForJsError(msg))
    }

    /**
     * Called by the JavaScript snippet from [generatePoToken] with the original identifier and the
     * result of the JavaScript `obtainPoToken()` function.
     */
    fun onObtainPoTokenResult(identifier: String, poTokenU8: String) {
        Log.d(TAG, "Generated poToken (before decoding): identifier=$identifier poTokenU8=$poTokenU8")
        val poToken = u8ToBase64(poTokenU8)

        Log.d(TAG, "Generated poToken: identifier=$identifier poToken=$poToken")
        popPoTokenEmitter(identifier)?.invoke(poToken)
    }

    override fun isExpired(): Boolean {
        // MOD: java.time backport
        //return Instant.now().isAfter(expirationInstant)
        return System.currentTimeMillis() > expirationMs
    }

    //endregion

    //region Handling multiple emitters
    /**
     * Adds the ([identifier], [emitter]) pair to the [poTokenEmitters] list. This makes it so that
     * multiple poToken requests can be generated invparallel, and the results will be notified to
     * the right emitters.
     */
    private fun addPoTokenEmitter(identifier: String, emitter: (String) -> Unit) {
        synchronized(poTokenEmitters) {
            poTokenEmitters.add(Pair(identifier, emitter))
        }
    }

    /**
     * Extracts and removes from the [poTokenEmitters] list a [SingleEmitter] based on its
     * [identifier]. The emitter is supposed to be used immediately after to either signal a success
     * or an error.
     */
    private fun popPoTokenEmitter(identifier: String): ((String) -> Unit)? {
        return synchronized(poTokenEmitters) {
            poTokenEmitters.indexOfFirst { it.first == identifier }.takeIf { it >= 0 }?.let {
                poTokenEmitters.removeAt(it).second
            }
        }
    }

    /**
     * Clears [poTokenEmitters] and returns its previous contents. The emitters are supposed to be
     * used immediately after to either signal a success or an error.
     */
    private fun popAllPoTokenEmitters(): List<Pair<String, (String) -> Unit>> {
        return synchronized(poTokenEmitters) {
            val result = poTokenEmitters.toList()
            poTokenEmitters.clear()
            result
        }
    }
    //endregion

    //region Utils

    /**
     * Makes a POST request to [url] with the given [data] by setting the correct headers. Calls
     * [onInitializationErrorCloseAndCancel] in case of any network errors and also if the response
     * does not have HTTP code 200, therefore this is supposed to be used only during
     * initialization. Calls [handleResponseBody] with the response body if the response is
     * successful. The request is performed in the background and a disposable is added to
     * [disposables].
     */
    private fun makeBotguardServiceRequest(
        url: String,
        data: String,
        headers: Map<String, String> = emptyMap()
    ): String? {
        val response = OkHttpManager.instance().doPostRequest(
            url,
            mapOf(
                // replace the downloader user agent
                "User-Agent" to USER_AGENT,
                "Accept" to "application/json",
                "Content-Type" to "application/json+protobuf",
                "x-goog-api-key" to GOOGLE_API_KEY,
                "x-user-agent" to "grpc-web-javascript/0.1",
            ) + headers,
            data,
            null
        )

        val httpCode = response.code()

        if (httpCode != 200) {
            onInitializationErrorCloseAndCancel(PoTokenException("Invalid response code: $httpCode"))
        }

        if (response.body() == null) {
            onInitializationErrorCloseAndCancel(PoTokenException("Response body is empty. Response code: $httpCode"))
        }

        return response.body()?.string()
    }

    /**
     * Handles any error happening during initialization, releasing resources and sending the error
     * to [generatorEmitter].
     */
    private fun onInitializationErrorCloseAndCancel(error: Throwable) {
        initError = error
        popAllPoTokenEmitters()
        runOnMainThread {
            close()
            // throw error
            onInitDone()
        }
    }

    /**
     * Releases all [v8Wrapper] and [disposables] resources.
     */
    @MainThread
    override fun close() {
        v8Wrapper.shutdownRuntime()
    }
    //endregion

    companion object : PoTokenGenerator.Factory {
        private val TAG = PoTokenV8::class.simpleName
        // Public API key used by BotGuard, which has been got by looking at BotGuard requests
        private const val GOOGLE_API_KEY = "AIzaSyDyT5W0Jh49F30Pqqtyfdf7pDLFKLJoAnw" // NOSONAR
        private const val REQUEST_KEY = "O43z0dpjhgX20SCx4KAo"
        private const val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36(KHTML, like Gecko)"
        //private const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
        //    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.3"
        private const val BASE_URL = "https://jnn-pa.googleapis.com"

        override fun newPoTokenGenerator(context: Context): PoTokenGenerator {
            if (hasThermalServiceBug(context)) {
                throw BadWebViewException("ThermalService isn't available")
            }

            if (hasUsbServiceBug(context)) {
                throw BadWebViewException("Usb service isn't available")
            }

            val latch = CountDownLatch(1)

            lateinit var potWv: PoTokenV8
            var initError: Throwable? = null

            runOnMainThread {
                potWv = try {
                    PoTokenV8(context) { latch.countDown() }
                } catch (e: Throwable) {
                    initError = V8WrapperException("${e::class.simpleName}: ${e.message}")
                    latch.countDown()
                    return@runOnMainThread
                }
                potWv.loadScriptAndObtainBotguard()
            }

            latch.await(20, TimeUnit.SECONDS)

            initError?.let { throw it }
            potWv.initError?.let { throw it }

            return potWv
        }

        /**
         * Runs [runnable] on the main thread using `Handler(Looper.getMainLooper()).post()`, and
         * if the `post` fails emits an error on [emitterIfPostFails].
         */
        private fun runOnMainThread(
            runnable: Runnable
        ) {
            if (!Handler(Looper.getMainLooper()).post(runnable)) {
                throw PoTokenException("Could not run on main thread")
            }
        }
    }
}
