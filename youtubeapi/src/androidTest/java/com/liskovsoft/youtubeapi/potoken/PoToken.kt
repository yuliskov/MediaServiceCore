package com.liskovsoft.youtubeapi.potoken

import com.google.gson.JsonElement
import com.liskovsoft.sharedutils.TestHelpers
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.js.V8Runtime

internal class PoToken {
    data class Arguments(val privateScript: String?,
                         val program: String?,
                         val globalName: String?,
                         val bgConfig: BotGuardConfig
    )
    data class Result(val poToken: String?,
                      val integrityTokenData: Data?)
    data class Data(val integrityToken: String?,
                    val estimatedTtlSecs: Int?,
                    val mintRefreshThreshold: Int?,
                    val webSafeFallbackToken: String?)

    private data class BotGuardResult(val integrityTokenData: Data?, val postProcessFunction: String?)

    /**
     * Generates a Proof of Origin Token.
     * @param args - The arguments for generating the token.
     */
    fun generate(args: Arguments): Result? {
        val bgResult = invokeBotGuard(args.privateScript, args.program, args.globalName, args.bgConfig) ?: return null

        if (bgResult.postProcessFunction?.isEmpty() == true) throw IllegalStateException("postProcessFunction cannot be empty")

        val script = listOf(
            DOM_WRAPPER.trimIndent(),
            args.privateScript,
            """
                const acquirePo = ${bgResult.postProcessFunction}(base64ToU8((_b = (_a = '${bgResult.integrityTokenData?.integrityToken}') !== null && _a !== void 0 ? _a : '${bgResult.integrityTokenData?.webSafeFallbackToken}') !== null && _b !== void 0 ? _b : ''));
                const result = acquirePo(new TextEncoder().encode('${args.bgConfig.identifier}'));
                u8ToBase64(result, true)
            """.trimIndent()
        )

        val poToken = V8Runtime.instance().evaluate(script.joinToString(""))

        return Result(poToken, bgResult.integrityTokenData)
    }

    /**
     * Creates a placeholder PoToken. This can be used while `sps` (StreamProtectionStatus) is 2, but will not work once it changes to 3.
     * @param identifier - Visitor ID or Data Sync ID.
     */
    fun generatePlaceholder(identifier: String, clientState: Int? = null): String? {
        return null
    }

    /**
     * Invokes the Botguard VM.
     * @param program - The bytecode to run.
     * @param globalName - The name of the VM in the global scope.
     * @param bgConfig - The Botguard configuration.
     */
    private fun invokeBotGuard(privateScript: String?, program: String?, globalName: String?, bgConfig: BotGuardConfig): BotGuardResult? {
        val script = listOf(
            DOM_WRAPPER.trimIndent(),

            //TestHelpers.readResource("potoken/jsdom_browserify.js"),
            //"var mydom = new jsdom.JSDOM(); window = mydom.window; document = mydom.window.document;",

            //TestHelpers.readResource("potoken/domino_browserify.js"),
            //"var setTimeout = () => {}; var clearInterval = () => {}; var setInterval = () => {}; var clearTimeout = () => {};",
            //"var window1 = domino.createWindow('<h1>Hello world</h1>', 'http://example.com'); var window = window1.window; var document = window1.document; var addEventListener = window.addEventListener;",
            //"var window1 = domino.createWindow('<h1>Hello world</h1>', 'http://example.com'); Object.assign(this, window1);",

            privateScript,
            """
               var vm = $globalName;
               const attFunctions = {};
               const setAttFunctions = (fn1, fn2, fn3, fn4) => {
                   Object.assign(attFunctions, { fn1, fn2, fn3, fn4 });
               };

               vm.a('$program', setAttFunctions, true, undefined, () => {});

               var botguardResponse;
               const postProcessFunctions = [];
               attFunctions.fn1((response) => (botguardResponse = response), [, , postProcessFunctions]);
               const payload = ['${bgConfig.requestKey}', botguardResponse];
               // TODO: handle multiple postProcessFunctions?
               var second = JSON.stringify(payload);
               var first = '';
               if (postProcessFunctions.length > 0)
                    first = postProcessFunctions[0].name;
               first + "$RESULT_DELIM" + second;
            """.trimIndent()
        )

        val result = V8Runtime.instance().evaluate(script.joinToString(""))

        val (postProcessFunction, payload) = result.split(RESULT_DELIM)

        val wrapper = bgConfig.api.generateIntegrityToken(payload)

        val response = RetrofitHelper.get(wrapper) ?: return null

        val (integrityToken, estimatedTtlSecs, mintRefreshThreshold, websafeFallbackToken) = response

        return BotGuardResult(
            Data(asString(integrityToken), asInt(estimatedTtlSecs), asInt(mintRefreshThreshold), asString(websafeFallbackToken)), postProcessFunction
        )
    }

    private fun asInt(intElem: JsonElement?) = if (intElem?.isJsonNull == true) null else intElem?.asInt

    private fun asString(strElem: JsonElement?) = if (strElem?.isJsonNull == true) null else strElem?.asString
}