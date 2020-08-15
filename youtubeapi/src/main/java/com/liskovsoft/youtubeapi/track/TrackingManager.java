package com.liskovsoft.youtubeapi.track;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TrackingManager {
    // https://www.youtube.com/api/stats/watchtime?ns=yt&el=leanback&cpn=UNmhTrGRBMRwP9KM&docid=RG_HIZHV9Xk&ver=2&referrer=https%3A%2F%2Fwww.youtube.com%2Ftv&cmt=2.973&ei=YFY4X_mxHYnR7gPKjpIQ&fmt=248&fs=0&rt=9.016&of=umaLXAj0vn73fwT78rCwYA&euri=https%3A%2F%2Fwww.youtube.com%2Ftv%23%2Fzylon-surface%3Fc%3Ddefault%26resume&lact=4797&cl=326301777&state=paused&vm=CAEQARgEKiAxdmJNZmdvMDBlcDlIU2JmbHhFMURuUDk2dkxUMHRDMjoyQUdiNlo4T3ppcEw4eTJoUm0xNmc3Y0Vzc0VEMThqQl9FZk5XYkhfMmRoQ1Z6b3dlSmc&volume=100&c=TVHTML5&cver=6.20180913&cplayer=UNIPLAYER&cbrand=Samsung&cbr=Safari&cbrver&ctheme=CLASSIC&cmodel=SmartTV&cnetwork&cos=Tizen&cosver=2.3&cplatform=TV&hl=en_US&cr=US&len=610.921&rtn=19&feature=g-topic-rec&afmt=251&idpj=-3&ldpj=-22&rti=9&muted=0&st=0&et=2.973
    //    ns: yt
    //    el: leanback
    //    cpn: 5vwuX5jgR93NjoqX
    //    docid: RG_HIZHV9Xk
    //    ver: 2
    //    referrer: https://www.youtube.com/tv
    //    cmt: 119.405
    //    ei: 8F44X7GTKNbF1gKpg6xQ
    //    fmt: 248
    //    fs: 0
    //    rt: 60.001
    //    of: umaLXAj0vn73fwT78rCwYA
    //    euri: https://www.youtube.com/tv#/zylon-surface?c=default&resume
    //    lact: 3309
    //    cl: 326301777
    //    state: playing
    //    vm: CAEQARgEKiAxdmJNZmdvMDBlcDlIU2JmbHhFMURuUDk2dkxUMHRDMjoyQUdiNlo4TlRDdkY2Rkx4bnNoNzQyYk1oSV9xVjN2eW9BWkpIdjJ2M0hpRm85V2dUN1E
    //    volume: 100,100
    //    c: TVHTML5
    //    cver: 6.20180913
    //    cplayer: UNIPLAYER
    //    cbrand: Samsung
    //    cbr: Safari
    //    cbrver:
    //    ctheme: CLASSIC
    //    cmodel: SmartTV
    //    cnetwork:
    //    cos: Tizen
    //    cosver: 2.3
    //    cplatform: TV
    //    hl: en_US
    //    cr: US
    //    len: 610.921
    //    rtn: 105
    //    feature: g-topic-rec
    //    afmt: 251
    //    idpj: -8
    //    ldpj: -3
    //    muted: 0,0
    //    st: 4.841,112.795
    //    et: 6.753,119.405


    @GET("https://www.youtube.com/api/stats/watchtime")
    Call<ResponseBody> updateWatchHistory(
            @Query("docid") String videoId,
            @Query("len") float videoLengthSec,     // 526.91
            @Query("cmt") float positionSec,        // 119.405
            @Query("st") float jumpFromToSec,       // 4.841,112.795
            @Query("et") float jumpFromToSec2,      // 6.753,119.405
            @Header("Authorization") String auth);
}
