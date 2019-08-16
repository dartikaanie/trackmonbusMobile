package com.anie.dara.trackmonbus_supir.rest;

import com.anie.dara.trackmonbus_supir.model.Halte;
import com.anie.dara.trackmonbus_supir.model.Jadwal;
import com.anie.dara.trackmonbus_supir.model.User;
import com.anie.dara.trackmonbus_supir.model.noBus;
import com.anie.dara.trackmonbus_supir.model.rit;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface dbClient {
    @FormUrlEncoded
    @POST("api/login_supir")
    Call<User> Loginv2(
            @Field("email") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("api/cekJadwal")
    Call<Jadwal> cekJadwal(
            @Field("user_id") String user_id
    );

    @GET("api/cekJadwalSupir")
    Call<List<Jadwal>> getJadwalUser(@Query("user_id") String user_id);


    @GET("api/viewHaltes")
    Call<List<Halte>> getAllHalte();

    @FormUrlEncoded
    @PUT("api/updateKmAwal")
    Call<ResponseBody> updateKmAwal(
            @Field("no_bus") String no_bus,
            @Field("tgl") String tgl,
            @Field("km_awal") Double km_awal
    );

    @FormUrlEncoded
    @POST("api/updateRit")
    Call<ResponseBody> updateRit(
            @Field("no_bus") String no_bus,
            @Field("tgl") String tgl
    );

    @FormUrlEncoded
    @POST("api/getRit")
    Call<List<rit>> getRit(
            @Field("no_bus") String no_bus,
            @Field("tgl") String tgl
    );


    @GET("api/getBusSearah")
    Call<List<noBus>> getBusSearah(@Query("halte_tujuan_id") String halte_tujuan_id);


}
