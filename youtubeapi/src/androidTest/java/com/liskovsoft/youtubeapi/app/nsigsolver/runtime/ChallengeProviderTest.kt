package com.liskovsoft.youtubeapi.app.nsigsolver.runtime

import androidx.test.platform.app.InstrumentationRegistry
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.app.nsigsolver.impl.V8ChallengeProvider
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.ChallengeInput
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.ChallengeOutput
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeProviderResponse
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeRequest
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeResponse
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class ChallengeProviderTest {
    private val tests = listOf(
        JsChallengeRequest(
            JsChallengeType.N, ChallengeInput(
                "https://www.youtube.com/s/player/3d3ba064/player_ias_tce.vflset/en_US/base.js", listOf(
                    "ZdZIqFPQK-Ty8wId",
                    "4GMrWHyKI5cEvhDO",
                )
            )
        ),
        JsChallengeRequest(
            JsChallengeType.SIG, ChallengeInput(
                "https://www.youtube.com/s/player/3d3ba064/player_ias_tce.vflset/en_US/base.js", listOf(
                    "gN7a-hudCuAuPH6fByOk1_GNXN0yNMHShjZXS2VOgsEItAJz0tipeavEOmNdYN-wUtcEqD3bCXjc0iyKfAyZxCBGgIARwsSdQfJ2CJtt",
                )
            )
        ),
        JsChallengeRequest(
            JsChallengeType.N, ChallengeInput(
                "https://www.youtube.com/s/player/5ec65609/player_ias_tce.vflset/en_US/base.js", listOf(
                    "0eRGgQWJGfT5rFHFj",
                )
            )
        ),
        JsChallengeRequest(
            JsChallengeType.SIG, ChallengeInput(
                "https://www.youtube.com/s/player/5ec65609/player_ias_tce.vflset/en_US/base.js", listOf(
                    "AAJAJfQdSswRQIhAMG5SN7-cAFChdrE7tLA6grH0rTMICA1mmDc0HoXgW3CAiAQQ4=CspfaF_vt82XH5yewvqcuEkvzeTsbRuHssRMyJQ=I",
                )
            )
        ),
        JsChallengeRequest(
            JsChallengeType.N, ChallengeInput(
                "https://www.youtube.com/s/player/6742b2b9/player_ias_tce.vflset/en_US/base.js", listOf(
                    "_HPB-7GFg1VTkn9u",
                    "K1t_fcB6phzuq2SF",
                )
            )
        ),
        JsChallengeRequest(
            JsChallengeType.SIG, ChallengeInput(
                "https://www.youtube.com/s/player/6742b2b9/player_ias_tce.vflset/en_US/base.js", listOf(
                    "MMGZJMUucirzS_SnrSPYsc85CJNnTUi6GgR5NKn-znQEICACojE8MHS6S7uYq4TGjQX_D4aPk99hNU6wbTvorvVVMgIARwsSdQfJAA",
                )
            )
        ),
    )

    private val responses: List<JsChallengeProviderResponse> by lazy {
        val responses = mutableListOf<JsChallengeProviderResponse>()
        for ((test, results) in tests.zip(listOf(
            listOf("qmtUsIz04xxiNW", "N9gmEX7YhKTSmw"),
            listOf("ttJC2JfQdSswRAIgGBCxZyAfKyi0cjXCb3gqEctUw-NYdNmOEvaepit0zJAtIEsgOV2SXZjhSHMNy0NXNG_1kNyBf6HPuAuCduh-a7O"),
            listOf("4SvMpDQH-vBJCw"),
            listOf("AJfQdSswRQIhAMG5SN7-cAFChdrE7tLA6grI0rTMICA1mmDc0HoXgW3CAiAQQ4HCspfaF_vt82XH5yewvqcuEkvzeTsbRuHssRMyJQ=="),
            listOf("qUAsPryAO_ByYg", "Y7PcOt3VE62mog"),
            listOf("AJfQdSswRAIgMVVvrovTbw6UNh99kPa4D_XQjGT4qYu7S6SHM8EjoCACIEQnz-nKN5RgG6iUTnNJC58csYPSrnS_SzricuUMJZGM"),
        ))) {
            responses.add(JsChallengeProviderResponse(test, JsChallengeResponse(
                test.type, ChallengeOutput(test.input.challenges.zip(results).toMap())
            )))
        }
        responses
    }

    @Before
    fun setUp() {
        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun testNonEmptyResponse() {
        val actual = V8ChallengeProvider.bulkSolve(tests).toList()
        assertEquals(responses, actual)
    }

    @Test
    fun testSingleNRequest() {
        val result = V8ChallengeProvider.bulkSolve(listOf(tests.first())).toList()
        assertTrue("not empty", result.isNotEmpty())
    }
}