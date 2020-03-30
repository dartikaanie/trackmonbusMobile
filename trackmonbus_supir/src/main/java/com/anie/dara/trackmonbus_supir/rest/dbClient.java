package com.anie.dara.trackmonbus_supir.rest;

import com.anie.dara.trackmonbus_supir.model.Halte;
import com.anie.dara.trackmonbus_supir.model.Rit;
import com.anie.dara.trackmonbus_supir.model.User;
import com.anie.dara.trackmonbus_supir.model.jadwal.DetailTrayeks;
import com.anie.dara.trackmonbus_supir.model.jadwal.Jadwal;
import com.anie.dara.trackmonbus_supir.model.jadwal.JadwalDetail;
import com.anie.dara.trackmonbus_supir.model.noBus;

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



    @GET("api/cekJadwal")
    Call<JadwalDetail> cekJadwal(
            @Query("user_id") String user_id
    );

    @GET("api/cekJadwalSupir")
    Call<List<JadwalDetail>> getJadwalUser(@Query("user_id") String user_id);


    @GET("api/viewHaltes")
    Call<List<Halte>> getAllHalte();

    @FormUrlEncoded
    @POST("api/updateKmAwal")
    Call<Jadwal> updateKmAwal(
            @Field("no_bus") String no_bus,
            @Field("tgl") String tgl,
            @Field("km_awal") Double km_awal
    );

    @FormUrlEncoded
    @POST("api/updateRit")
    Call<Rit> updateRit(
            @Field("no_bus") String no_bus,
            @Field("tgl") String tgl
    );

    @FormUrlEncoded
    @POST("api/getRit")
    Call<List<Rit>> getRit(
            @Field("no_bus") String no_bus,
            @Field("tgl") String tgl
    );

    @GET("api/getDataJadwal")
    Call<JadwalDetail> getDataJadwal(
            @Query("no_bus") String no_bus,
            @Query("tgl") String tgl
    );



    @GET("api/getBusSearah")
    Call<List<noBus>> getBusSearah(@Query("jalur_id") String jalur_id);

    @GET("api/getCurrentJalur")
    Call<String> getCurrentJalur(@Query("no_bus") String no_bus,@Query("tgl") String tgl );

    @FormUrlEncoded
    @POST("api/UbahDataJadwal")
    Call<Jadwal> UbahDataJadwal(
            @Field("no_bus") String no_bus,
            @Field("tgl") String tgl,
            @Field("km_awal") String km_awal,
            @Field("km_akhir") String km_akhir,
            @Field("keterangan") String keterangan
    );

    @GET("api/getContain")
    Call<String> getContain(
            @Query("tgl") String tgl,
            @Query("no_bus") String no_bus,
            @Query("shift_id") String shift_id,
            @Query("waktu") String waktu,
            @Query("posisi") String posisi,
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Query("trayek_id") String trayek_id );

    @GET("api/outArea")
    Call<ResponseBody> pushOutArea(
            @Query("tgl") String tgl,
            @Query("no_bus") String no_bus,
            @Query("shift_id") String shift_id,
            @Query("waktu") String waktu,
            @Query("lat") String lat,
            @Query("lng") String lng );

    @GET("api/stopTime")
    Call<String> stopTime(
            @Query("tgl") String tgl,
            @Query("no_bus") String no_bus,
            @Query("shift_id") String shift_id,
            @Query("waktu_mulai") String waktu_mulai,
            @Query("waktu_akhir") String waktu_akhir,
            @Query("lat") String lat,
            @Query("lng") String lng );

    @GET("api/checkpointHalte")
    Call<Halte> checkpointHalte(
            @Query("tgl") String tgl,
            @Query("no_bus") String no_bus,
            @Query("jalur_id") String jalur_id,
            @Query("waktu") String waktu,
            @Query("lat") String lat,
            @Query("lng") String lng );

}
