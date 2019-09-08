package com.liskovsoft.youtubeapi.subscriptions;

import com.liskovsoft.youtubeapi.subscriptions.models.NextSubscriptions;
import com.liskovsoft.youtubeapi.subscriptions.models.Subscriptions;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SubscriptionsManager {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<Subscriptions> getSubscriptions(@Body String subscriptionsQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<NextSubscriptions> getNextSubscriptions(@Body String subscriptionsQuery);
}
