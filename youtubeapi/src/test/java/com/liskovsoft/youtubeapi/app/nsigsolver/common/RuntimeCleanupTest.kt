package com.liskovsoft.youtubeapi.app.nsigsolver.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class RuntimeCleanupTest {
    @Test
    fun successfulEvaluationReleasesTheRuntime() {
        val events = mutableListOf<String>()

        val result = withRuntimeCleanup({ events.add("release") }) {
            events.add("initialise")
            events.add("evaluate")
            "synthetic result"
        }

        assertEquals("synthetic result", result)
        assertEquals(listOf("initialise", "evaluate", "release"), events)
    }

    @Test
    fun initialisationFailureReleasesTheRuntimeBeforeRetry() {
        assertRecoveryAfterFailure(failDuringInitialisation = true)
    }

    @Test
    fun evaluationFailureReleasesTheRuntimeBeforeRetry() {
        assertRecoveryAfterFailure(failDuringInitialisation = false)
    }

    @Test
    fun cleanupFailureDoesNotHideTheOriginalError() {
        val evaluationError = IllegalArgumentException("Synthetic evaluation failure")
        val cleanupError = IllegalStateException("Synthetic cleanup failure")

        val actual = captureFailure {
            withRuntimeCleanup({ throw cleanupError }) {
                throw evaluationError
            }
        }

        assertSame(evaluationError, actual)
        assertEquals(listOf(cleanupError), actual.suppressed.toList())
    }

    @Test
    fun cleanupFailureAfterSuccessIsReported() {
        val cleanupError = IllegalStateException("Synthetic cleanup failure")

        val actual = captureFailure {
            withRuntimeCleanup({ throw cleanupError }) { "synthetic result" }
        }

        assertSame(cleanupError, actual)
    }

    @Test
    fun cleanupRunsForErrorsAsWellAsExceptions() {
        val evaluationError = AssertionError("Synthetic runtime failure")
        var released = false

        val actual = captureFailure {
            withRuntimeCleanup({ released = true }) { throw evaluationError }
        }

        assertSame(evaluationError, actual)
        assertTrue(released)
    }

    @Test
    fun rethrowingTheSameErrorDuringCleanupPreservesIt() {
        val error = IllegalStateException("Synthetic shared failure")

        val actual = captureFailure {
            withRuntimeCleanup({ throw error }) { throw error }
        }

        assertSame(error, actual)
        assertTrue(actual.suppressed.isEmpty())
    }

    private fun assertRecoveryAfterFailure(failDuringInitialisation: Boolean) {
        val error = IllegalStateException("Synthetic runtime failure")
        var runtime: Int? = null
        var created = 0
        var shouldFail = true
        val released = mutableListOf<Int>()

        fun evaluate(): Int = withRuntimeCleanup({
            runtime?.let { released.add(it) }
            runtime = null
        }) {
            if (runtime == null) {
                runtime = ++created
                if (shouldFail && failDuringInitialisation) {
                    throw error
                }
            }
            if (shouldFail && !failDuringInitialisation) {
                throw error
            }
            runtime!!
        }

        assertSame(error, captureFailure { evaluate() })
        assertNull(runtime)
        assertEquals(listOf(1), released)

        shouldFail = false
        assertEquals(2, evaluate())
        assertNull(runtime)
        assertEquals(listOf(1, 2), released)
    }

    private fun captureFailure(block: () -> Unit): Throwable {
        try {
            block()
        } catch (error: Throwable) {
            return error
        }
        throw AssertionError("Expected a failure")
    }
}
