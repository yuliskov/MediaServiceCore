package com.liskovsoft.youtubeapi.subscriptions;

import com.liskovsoft.youtubeapi.subscriptions.models.NextSubscriptions;
import com.liskovsoft.youtubeapi.subscriptions.models.Subscriptions;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SubscriptionsManager {
    @Headers({"Content-Type: application/json",
            "Authorization: Bearer ya29.Gl1-B33-hnaYkpj4N8VKwzc16goY-Fm8DBVDV53M7n8MVAM0GAJxgFk3Ko06oDWeOvkVaPIzqi-vulQvwYBERwYrRcEBN8YYDjxXbG6M3-kRD_-LdUZ8R2qzLMzCyOE"})
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<Subscriptions> getSubscriptions(@Body String subscriptionsQuery);

    @Headers({"Content-Type: application/json",
            "Authorization: Bearer ya29.Gl1-B33-hnaYkpj4N8VKwzc16goY-Fm8DBVDV53M7n8MVAM0GAJxgFk3Ko06oDWeOvkVaPIzqi-vulQvwYBERwYrRcEBN8YYDjxXbG6M3-kRD_-LdUZ8R2qzLMzCyOE"})
    @POST("https://www.youtube.com/youtubei/v1/browse")
    Call<NextSubscriptions> getNextSubscriptions(@Body String subscriptionsQuery);
}
