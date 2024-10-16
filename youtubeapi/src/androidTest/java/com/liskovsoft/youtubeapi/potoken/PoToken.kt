package com.liskovsoft.youtubeapi.potoken

import com.google.gson.JsonElement
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.js.V8Runtime

private const val RESULT_DELIM = "%RESULT_DELIM%"
private const val DOM_WRAPPER =
    """
        const base64urlCharRegex = /[-_.]/g;
        const base64urlToBase64Map = {
            '-': '+',
            _: '/',
            '.': '='
        };
        function base64ToU8(base64) {
            let base64Mod;
            if (base64urlCharRegex.test(base64)) {
                base64Mod = base64.replace(base64urlCharRegex, function (match) {
                    return base64urlToBase64Map[match];
                });
            }
            else {
                base64Mod = base64;
            }
            base64Mod = atob(base64Mod);
            const result = new Uint8Array([...base64Mod].map((char) => char.charCodeAt(0)));
            return result;
        }
        function u8ToBase64(u8, base64url = false) {
            const result = btoa(String.fromCharCode(...u8));
            if (base64url) {
                return result
                    .replace(/\+/g, '-')
                    .replace(/\//g, '_');
            }
            return result;
        }
        function btoa(input) {
          const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
          let str = String(input);
          let output = '';
          let i = 0;
        
          while (i < str.length) {
            const block = (str.charCodeAt(i++) << 16) |
                          (str.charCodeAt(i++) << 8) |
                          (str.charCodeAt(i++) || 0);
        
            output += chars[(block >> 18) & 0x3F] +
                      chars[(block >> 12) & 0x3F] +
                      (i > str.length + 1 ? '=' : chars[(block >> 6) & 0x3F]) +
                      (i > str.length ? '=' : chars[block & 0x3F]);
          }
        
          return output;
        }
        window = {}; window.document = {}; document = {};
        window.performance = { now: () => 261855.69999999553 };
        window.btoa = btoa;
        document.hidden = false;
    """

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

    fun generate(args: Arguments): Result? {
        val bgResult = invokeBotGuard(args.privateScript, args.program, args.globalName, args.bgConfig) ?: return null

        if (bgResult.postProcessFunction?.isEmpty() == true) throw IllegalStateException("Couldn't get postProcessFunction")

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

    fun generatePlaceholder(visitorData: String): String? {
        return null
    }

    private fun invokeBotGuard(privateScript: String?, program: String?, globalName: String?, bgConfig: BotGuardConfig): BotGuardResult? {
        val script = listOf(
            DOM_WRAPPER.trimIndent(),
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