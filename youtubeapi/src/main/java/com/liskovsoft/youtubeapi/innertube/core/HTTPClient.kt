package com.liskovsoft.youtubeapi.innertube.core

import android.net.Uri
import androidx.core.net.toUri
import com.liskovsoft.googlecommon.common.converters.gson.WithGson
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.sharedutils.querystringparser.UrlEncodedQueryString
import com.liskovsoft.youtubeapi.innertube.models.InnertubeContext
import com.liskovsoft.youtubeapi.innertube.models.PlayerResult
import com.liskovsoft.youtubeapi.innertube.utils.CLIENTS
import com.liskovsoft.youtubeapi.innertube.utils.CLIENT_NAME_IDS
import com.liskovsoft.youtubeapi.innertube.utils.SUPPORTED_CLIENTS
import com.liskovsoft.youtubeapi.innertube.utils.URLS
import com.liskovsoft.youtubeapi.innertube.utils.toJsonString
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url
import kotlin.collections.get

@WithGson
private interface InnertubePlayerApi {
    @POST()
    fun retrievePlayer(@Url url: String, @HeaderMap headers: Map<String, String>, @Body jsonBody: String): Call<PlayerResult?>
}

internal class HTTPClient(val session: Session) {
    private val requestApi = RetrofitHelper.create(InnertubePlayerApi::class.java)

    fun fetch(input: String, init: RequestInit): PlayerResult? {
        val innertubeUrl = URLS.API.PRODUCTION_1 + session.apiVersion
        val baseURL = innertubeUrl

        val url = "${baseURL}${if (baseURL.endsWith("/") || input.startsWith("/")) "" else "/"}$input"

        val requestHeaders = init.headers

        val requestUrl = UrlEncodedQueryString.parse(url)

        requestUrl.set("prettyPrint", "false")
        requestUrl.set("alt", "json")

        setupCommonHeaders(requestHeaders, session, url.toUri())

        //val isInnertubeReq = baseURL == innertubeUrl || baseURL == URLS.YT_UPLOAD

        var requestBody: String? = null
        var isWebKids: Boolean? = false

        if (requestHeaders["Content-Type"] == "application/json") {
            val jsonPayload = processJsonPayload(init.body, session)

            val (newBody, processedIsWebKids, processedClientVersion, processedClientNameId, adjustedClientName) = jsonPayload

            requestBody = newBody
            isWebKids = processedIsWebKids

            processedClientVersion?.let {
                requestHeaders["X-Youtube-Client-Version"] = it
            }

            processedClientNameId?.let {
                requestHeaders["X-Youtube-Client-Name"] = it
            }

            when (adjustedClientName) {
                CLIENTS.ANDROID.NAME,
                CLIENTS.YTMUSIC_ANDROID.NAME -> {
                    CLIENTS.ANDROID.USER_AGENT?.let { requestHeaders["User-Agent"] = it }
                    requestHeaders["X-GOOG-API-FORMAT-VERSION"] = "2"
                }
                CLIENTS.IOS.NAME -> {
                    CLIENTS.IOS.USER_AGENT?.let { requestHeaders["User-Agent"] = it }
                }
            }
        } else if (requestHeaders["Content-Type"] == "application/x-protobuf") {
            // Assume it is always an Android request.
            CLIENTS.ANDROID.USER_AGENT?.let { requestHeaders["User-Agent"] = it }
            requestHeaders["X-GOOG-API-FORMAT-VERSION"] = "2"
            requestHeaders.remove("X-Youtube-Client-Version")
        }

        // TODO: not implemented
        // Authenticate (NOTE: YouTube Kids does not support regular bearer tokens)
        //if (session.logged_in && is_innertube_req && !is_web_kids) {
        //    const oauth = session.oauth;
        //
        //    if (oauth.oauth2_tokens) {
        //        if (oauth.shouldRefreshToken()) {
        //            await oauth.refreshAccessToken();
        //        }
        //
        //        request_headers.set('Authorization', `Bearer ${oauth.oauth2_tokens.access_token}`);
        //    }
        //
        //    const cookie = this.#cookie;
        //
        //    if (cookie) {
        //        const sapisid = getCookie(cookie, 'SAPISID');
        //
        //        if (sapisid) {
        //            request_headers.set('Authorization', await generateSidAuth(sapisid));
        //            request_headers.set('X-Goog-Authuser', session.account_index.toString());
        //            if (session.context.user.onBehalfOfUser)
        //                request_headers.set('X-Goog-PageId', session.context.user.onBehalfOfUser);
        //        }
        //
        //        request_headers.set('Cookie', cookie);
        //    }
        //}

        if (requestBody == null)
            return null

        val playerResultWrapper = requestApi.retrievePlayer(requestUrl.toString(), requestHeaders, requestBody)

        return RetrofitHelper.get(playerResultWrapper, false)
    }

    private fun setupCommonHeaders(requestHeaders: MutableMap<String, String>, session: Session, requestUrl: Uri) {
        requestHeaders["Accept"] = "*/*"
        requestHeaders["Accept-Language"] = "*"

        requestHeaders["X-Goog-Visitor-Id"] =
            session.context.client.visitorData ?: ""

        requestHeaders["X-Youtube-Client-Version"] =
            session.context.client.clientVersion ?: ""

        val clientNameId = CLIENT_NAME_IDS[
            session.context.client.clientName
        ]

        if (clientNameId != null) {
            requestHeaders["X-Youtube-Client-Name"] = clientNameId
        }

        requestHeaders["User-Agent"] = session.userAgent
        requestHeaders["Origin"] = requestUrl.scheme + "://" + requestUrl.host
    }

    private fun processJsonPayload(body: RequestInitBody, session: Session): JsonPayloadProcessed {
        val parsedPayload = body
        val adjustedContext = session.context // why do JSON.parse(JSON.stringify(session.context)) as Context

        adjustContext(adjustedContext, parsedPayload.client)

        // merge session and body

        parsedPayload.context = adjustedContext

        val newPayload = parsedPayload

        val clientVersion = newPayload.context?.client?.clientVersion

        val clientNameFromAdjustedContext = newPayload.context?.client?.clientName
        val clientNameId = CLIENT_NAME_IDS[clientNameFromAdjustedContext]

        newPayload.client = null

        val isWebKids = newPayload.context?.client?.clientName == CLIENTS.WEB_KIDS.NAME

        return JsonPayloadProcessed(
            toJsonString(newPayload),
            isWebKids,
            clientVersion,
            clientNameId,
            newPayload.context?.client?.clientName
        )
    }


    private fun adjustContext(ctx: InnertubeContext, client: String?) {
        if (client == null) return

        val clientName = client.uppercase()

        if (clientName !in SUPPORTED_CLIENTS) {
            throw IllegalStateException("Invalid client: $client")
        }

        if (clientName != "WEB") {
            ctx.client.configInfo = null
        }

        if (
            clientName == "ANDROID" ||
            clientName == "YTMUSIC_ANDROID" ||
            clientName == "YTSTUDIO_ANDROID"
        ) {
            ctx.client.androidSdkVersion = CLIENTS.ANDROID.SDK_VERSION
            ctx.client.userAgent = CLIENTS.ANDROID.USER_AGENT
            ctx.client.osName = "Android"
            ctx.client.osVersion = "13"
            ctx.client.platform = "MOBILE"
        }

        when (clientName) {
            "MWEB" -> {
                ctx.client.clientVersion = CLIENTS.MWEB.VERSION
                ctx.client.clientName = CLIENTS.MWEB.NAME
                ctx.client.clientFormFactor = "SMALL_FORM_FACTOR"
                ctx.client.platform = "MOBILE"
            }

            "IOS" -> {
                ctx.client.deviceMake = "Apple"
                ctx.client.deviceModel = CLIENTS.IOS.DEVICE_MODEL
                ctx.client.clientVersion = CLIENTS.IOS.VERSION
                ctx.client.clientName = CLIENTS.IOS.NAME
                ctx.client.platform = "MOBILE"
                ctx.client.osName = CLIENTS.IOS.OS_NAME
                ctx.client.osVersion = CLIENTS.IOS.OS_VERSION
                ctx.client.browserName = null
                ctx.client.browserVersion = null
            }

            "YTMUSIC" -> {
                ctx.client.clientVersion = CLIENTS.YTMUSIC.VERSION
                ctx.client.clientName = CLIENTS.YTMUSIC.NAME
            }

            "ANDROID" -> {
                ctx.client.clientVersion = CLIENTS.ANDROID.VERSION
                ctx.client.clientFormFactor = "SMALL_FORM_FACTOR"
                ctx.client.clientName = CLIENTS.ANDROID.NAME
            }

            "YTMUSIC_ANDROID" -> {
                ctx.client.clientVersion = CLIENTS.YTMUSIC_ANDROID.VERSION
                ctx.client.clientFormFactor = "SMALL_FORM_FACTOR"
                ctx.client.clientName = CLIENTS.YTMUSIC_ANDROID.NAME
            }

            "YTSTUDIO_ANDROID" -> {
                ctx.client.clientVersion = CLIENTS.YTSTUDIO_ANDROID.VERSION
                ctx.client.clientFormFactor = "SMALL_FORM_FACTOR"
                ctx.client.clientName = CLIENTS.YTSTUDIO_ANDROID.NAME
            }

            "TV" -> {
                ctx.client.clientVersion = CLIENTS.TV.VERSION
                ctx.client.clientName = CLIENTS.TV.NAME
                ctx.client.userAgent = CLIENTS.TV.USER_AGENT
            }

            "TV_SIMPLY" -> {
                ctx.client.clientVersion = CLIENTS.TV_SIMPLY.VERSION

            }
        }
    }
}

internal data class RequestInit(
    val baseURL: String? = null,
    val headers: MutableMap<String, String> = mutableMapOf("Content-Type" to "application/json"),
    val body: RequestInitBody
)

internal class RequestInitBody(
    val videoId: String,
    var client: String? = null,
    // other values specific for player....
    session: Session
) {
    val contentCheckOk: Boolean = true
    val racyCheckOk: Boolean = true
    val playbackContext: PlaybackContext = PlaybackContext(session)
    var context: InnertubeContext? = null

    class PlaybackContext(session: Session) {
        val adPlaybackContext: AdPlaybackContext = AdPlaybackContext()
        val contentPlaybackContext: ContentPlaybackContext = ContentPlaybackContext(session)

        class AdPlaybackContext {
            val pyv: Boolean = true
        }

        class ContentPlaybackContext(session: Session) {
            val signatureTimestamp: String? = session.player?.signatureTimestamp
        }
    }
}

internal data class JsonPayloadProcessed(
    val newBody: String,
    val isWebKids: Boolean,
    val clientVersion: String?,
    val clientNameId: String?,
    val adjustedClientName: String?
)