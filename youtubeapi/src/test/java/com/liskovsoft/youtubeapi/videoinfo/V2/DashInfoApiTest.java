package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class DashInfoApiTest {
    private static final String DASH_URL = "https://manifest.googlevideo.com/api/manifest/dash/expire/1663394208/ei/QA0lY8PPG87JyAXejrLgBg/ip/46.98.136.44/id/YG0lEwYLaxk.2/source/yt_live_broadcast/requiressl/yes/tx/24268153/txs/24268153%2C24268154%2C24268155/as/fmp4_audio_clear%2Cwebm_audio_clear%2Cwebm2_audio_clear%2Cfmp4_sd_hd_clear%2Cwebm2_sd_hd_clear/spc/yR2vp-_1lfmEB9ByjNPjRkdtdwGrsJoa0D7ozXfABSib/vprv/1/pacing/0/keepalive/yes/fexp/24001373%2C24007246/itag/0/playlist_type/DVR/sparams/expire%2Cei%2Cip%2Cid%2Csource%2Crequiressl%2Ctx%2Ctxs%2Cas%2Cspc%2Cvprv%2Citag%2Cplaylist_type/sig/AOq0QJ8wRgIhALFhmJZZhUtrIei9W-fpxacS9-CCsFglimyezLDAUtwhAiEAgQW8EadW-ejfA6Cyq9chY4qC1ffPUWmHU0dp2ZrdzU8%3D";
    private DashInfoApi mService;

    @Before
    public void setUp() throws Exception {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        RetrofitHelper.sForceEnableProfiler = true;

        mService = RetrofitHelper.withRegExp(DashInfoApi.class);
    }

    @Test
    public void testDashInfoNotEmpty() throws IOException {
        Call<DashInfo> dashInfoWrapper = mService.getDashInfo(DASH_URL);

        DashInfo dashInfo = dashInfoWrapper.execute().body();

        assertTrue("start segment not null", dashInfo.getStartSegmentNum() > 0);
    }
}