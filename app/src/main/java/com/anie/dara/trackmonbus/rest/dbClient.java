package com.anie.dara.trackmonbus.rest;

import com.anie.dara.trackmonbus.model.Bus;
import com.anie.dara.trackmonbus.model.Halte;
import com.anie.dara.trackmonbus.model.Komentar;
import com.anie.dara.trackmonbus.model.Perihal;
import com.anie.dara.trackmonbus.model.Pesan;
import com.anie.dara.trackmonbus.model.Result;
import com.anie.dara.trackmonbus.model.Trayeks.JalurItem;
import com.anie.dara.trackmonbus.model.Trayeks.Trayeks;
import com.anie.dara.trackmonbus.model.User;
import com.anie.dara.trackmonbus.model.noBus;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface dbClient {

    @FormUrlEncoded
    @POST("api/login_user")
    Call<User> LoginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("api/cekUser")
    Call<User> cekUser(
            @Query( "user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api/register")
    Call<User> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("role_id") String role_id
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
    @POST("api/addPerihal")
    Call<Perihal> addPerihal(
            @Field("perihal") String perihal
    );

    @FormUrlEncoded
    @POST("api/TambahPesan")
    Call<Result> AddPesan(
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

    @FormUrlEncoded
    @POST("api/hapusKomentar")
    Call<ResponseBody> hapusKomentar(
            @Field("keluhan_komentar_id") String keluhan_komentar_id
    );

    @FormUrlEncoded
    @POST("api/hapusPesan")
    Call<ResponseBody> hapusPesan(
            @Field("keluhan_id") String keluhan_id
    );


    @GET("api/viewBuses")
    Call<List<Bus>> getAllBus();

    @GET("api/getDataBus/{no_bus}")
    Call<Bus> getDataBus(@Path("no_bus") String no_bus);

    @GET("api/viewHaltes/{trayek_id}")
    Call<List<Halte>> getAllHalte(@Path("trayek_id") String trayek_id);

    @GET("api/viewHaltes")
    Call<List<Halte>> getAllHalte();

    @GET("api/viewTrayeks")
    Call<List<Trayeks>> getAllTrayek();

    @GET("api/viewAllJalur")
    Call<List<JalurItem>> getAllJalur();

    @GET("api/getBusSearah")
    Call<List<noBus>> getBusSearah(@Query("jalur_id") String jalur_id);






}
