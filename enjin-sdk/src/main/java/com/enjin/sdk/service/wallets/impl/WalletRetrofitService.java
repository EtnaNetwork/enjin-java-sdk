package com.enjin.sdk.service.wallets.impl;

import com.enjin.sdk.graphql.GraphQLRequest;
import com.enjin.sdk.graphql.GraphQLResponse;
import com.enjin.sdk.graphql.GraphQuery;
import com.enjin.sdk.model.service.wallets.Wallet;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WalletRetrofitService {

    @POST("graphql")
    @GraphQuery("GetWallet")
    @Headers("Content-Type: application/json")
    Call<GraphQLResponse<Wallet>> getWallet(@Body GraphQLRequest request);

}
