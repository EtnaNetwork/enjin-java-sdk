package com.enjin.sdk.schemas;

import com.enjin.sdk.graphql.GraphQLResponse;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface GetOne<T> {

    @POST("/graphql/{schema}")
    @Headers("Content-Type: application/json")
    Call<GraphQLResponse<T>> getOne(@Path("schema") String schema,
                                    @Body JsonObject request);

}
