package com.liskovsoft.youtubeapi.app.nsigsolver.provider

import com.liskovsoft.googlecommon.common.api.FileApi
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper

internal abstract class JsChallengeProvider {
    private val fileApi = RetrofitHelper.create(FileApi::class.java)
    protected abstract val supportedTypes: List<JsChallengeType>

    private fun validateRequest(request: JsChallengeRequest) {
        // Validate request using built-in settings
        if (request.type !in supportedTypes) {
            throw JsChallengeProviderRejectedRequest("JS Challenge type ${request.type} is not supported by the provider ${this::class.simpleName}")
        }
    }

    /**
     * Solve multiple JS challenges and return the results
     */
    fun bulkSolve(requests: List<JsChallengeRequest>): Sequence<JsChallengeProviderResponse> = sequence {
        val validatedRequests: MutableList<JsChallengeRequest> = mutableListOf()
        for (request in requests) {
            try {
                validateRequest(request)
                validatedRequests.add(request)
            } catch (e: JsChallengeProviderRejectedRequest) {
                yield(JsChallengeProviderResponse(request=request, error=e))
            }
        }
        yieldAll(realBulkSolve(validatedRequests))
    }

    /**
     * Subclasses can override this method to handle bulk solving
     */
    protected abstract fun realBulkSolve(validatedRequests: List<JsChallengeRequest>): Sequence<JsChallengeProviderResponse>

    protected fun getPlayer(videoId: String, playerUrl: String): String? {
        return loadPlayer(playerUrl)
    }

    private fun loadPlayer(playerUrl: String): String? {
        return try {
            RetrofitHelper.get(fileApi.getContent(playerUrl))?.content
        } catch (e: Exception) {
            throw JsChallengeProviderError("Failed to load player for JS challenge: $playerUrl", e)
        }
    }
}