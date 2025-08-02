package com.liskovsoft.googlecommon.common.models.gen

internal data class ErrorResponse(
    val error: ErrorRoot?
) {
    data class ErrorRoot(
        val code: Int?,
        val message: String?,
        val status: String?,
        val errors: List<ErrorItem?>?
    ) {
        data class ErrorItem(
            val message: String?,
            val domain: String?,
            val reason: String?
        )
    }
}

internal data class AuthErrorResponse(
    val error: String?,
    val error_description: String?
)