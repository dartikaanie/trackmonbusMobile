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
            @Query("mode") String mode,
            @Query("departure_time") String departure_time,
            @Query("durationInTraffic") String durationInTraffic,
            @Query("key") String key
    );

    @GET("json")
    Call<DistanceMatrix> getDistanceInfoWalking(
            @Query("origins") String origins,
            @Query("destinations") String destinations,
            @Query("mode") String mode,
            @Query("key") String key
    );
//    Call<DistanceMatrix> getDistanceInfo(String posisiUser, String s, String s1);
}
