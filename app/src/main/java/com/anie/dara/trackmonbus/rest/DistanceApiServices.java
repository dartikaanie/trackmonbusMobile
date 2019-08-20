package com.anie.dara.trackmonbus.rest;

import com.anie.dara.trackmonbus.model.distanceMatrix.DistanceMatrix;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DistanceApiServices {
    @GET("json")
    Call<DistanceMatrix> getDistanceInfo(
            @Query("origins") String origins,
            @Query("destinations") String destinations,
            @Query("key") String key
    );
}
