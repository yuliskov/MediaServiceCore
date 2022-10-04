package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoUrl;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoFormat;
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
    private static final String DASH_URL2 = "https://rr1---sn-4gxb5u-qo3z.googlevideo.com/videoplayback?expire=1664364889&ei=-dwzY-PoLIGD0u8Plr-iiAY&ip=46.98.137.152&id=5dVG__s_bqk.1&itag=140&aitags=140&source=yt_live_broadcast&requiressl=yes&mh=MG&mm=44%2C29&mn=sn-4gxb5u-qo3z%2Csn-3c27sne7&ms=lva%2Crdu&mv=m&mvi=1&pl=23&hcs=%2Csd&smhost=%2Crr12---sn-3c27sn7d.googlevideo.com&initcwndbps=782500&defrag=1&vprv=1&live=1&hang=1&noclen=1&mime=audio%2Fmp4&ns=LamJI6Uf8u-I0rnIkqcdViQI&gir=yes&mt=1664342931&fvip=15&keepalive=yes&fexp=24001373%2C24007246&c=TVHTML5&n=GjORtRiOffHG-Q&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cdefrag%2Cvprv%2Clive%2Chang%2Cnoclen%2Cmime%2Cns%2Cgir&sig=AOq0QJ8wRgIhAKdbxii2cP4lhsL5bDc-sUNA5NMDa92BV_juUradWdQEAiEAy2JTNUx_2vdMrcqHZk6mzxJP0GeHGTHLZvOidmmmyyw%3D&lsparams=mh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Chcs%2Csmhost%2Cinitcwndbps&lsig=AG3C_xAwRAIgFC4T42BkFeXxcWXDefm_N1HD7PJ8b5Y9eJU6j4TTFBQCIACRWdRu4T3LEYkv9rYRLZQoTBU6o9SdYltUyoE4dtVe&alr=yes&cpn=T-7nsD1507axLAN6&cver=7.20220926.09.00&sq=340297&rn=6&rbuf=0";
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
        Call<DashInfoUrl> dashInfoWrapper = mService.getDashInfoUrl(DASH_URL);

        DashInfoUrl dashInfo = dashInfoWrapper.execute().body();

        assertTrue("start segment not null", dashInfo.getStartSegmentNum() > 0);
    }

    @Test
    public void testDashInfo2NotEmpty() throws IOException {
        Call<DashInfoFormat> dashInfoWrapper = mService.getDashInfoFormat(DASH_URL2);

        DashInfoFormat dashInfo = dashInfoWrapper.execute().body();

        assertTrue("start segment not null", dashInfo.getStartSegmentNum() > 0);
        assertTrue("start segment time not null", dashInfo.getStartTimeMs() > 0);
    }
}