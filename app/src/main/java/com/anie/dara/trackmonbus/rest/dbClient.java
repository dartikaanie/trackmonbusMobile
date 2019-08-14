package com.anie.dara.trackmonbus.rest;

import com.anie.dara.trackmonbus.model.Bus;
import com.anie.dara.trackmonbus.model.Halte;
import com.anie.dara.trackmonbus.model.Komentar;
import com.anie.dara.trackmonbus.model.Perihal;
import com.anie.dara.trackmonbus.model.Pesan;
import com.anie.dara.trackmonbus.model.Trayek;
import com.anie.dara.trackmonbus.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface dbClient {

    @FormUrlEncoded
    @POST("api/login_user")
    Call<User> LoginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("api/pesan")
    Call<List<Pesan>> getAllPesan();

    @GET("api/perihals")
    Call<List<Perihal>> getAllPerihals();

    @GET("api/MyPesan/{user_id}")
    Call<List<Pesan>> getMyPesan(@Path("user_id") String user_id);

    @GET("api/viewKomentar/{keluhan_id}")
    Call<List<Komentar>> getPesanKomentar(@Path("keluhan_id") String keluhan_id);

    @FormUrlEncoded
    @POST("api/TambahPesan")
    Call<Pesan> AddPesan(
            @Field("user_id") String user_id,
            @Field("perihal_id") String perihal_id,
            @Field("isi_keluhan") String isi_keluhan,
            @Field("no_bus") String no_bus,
            @Field("created_at") String created_at
    );


    @FormUrlEncoded
    @POST("api/TambahKomentar")
    Call<Komentar> AddKomentarPesan(
            @Field("keluhan_id") String keluhan_id,
            @Field("user_id") String user_id,
            @Field("isi_komentar") String isi_komentar
    );

    @GET("api/viewBuses")
    Call<List<Bus>> getAllBus();

    @GET("api/viewHaltes/{trayek_id}")
    Call<List<Halte>> getAllHalte(@Path("trayek_id") String trayek_id);

    @GET("api/viewHaltes")
    Call<List<Halte>> getAllHalte();

    @GET("api/viewTrayeks")
    Call<List<Trayek>> getAllTrayek();







}
