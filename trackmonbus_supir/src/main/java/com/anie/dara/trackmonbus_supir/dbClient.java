package com.anie.dara.trackmonbus_supir;

import com.anie.dara.trackmonbus_supir.model.Halte;
import com.anie.dara.trackmonbus_supir.model.Jadwal;
import com.anie.dara.trackmonbus_supir.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface dbClient {
    @FormUrlEncoded
    @POST("api/login_supir")
    Call<User> Loginv2(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("api/cekJadwal")
    Call<Jadwal> cekJadwal();

    @GET("api/viewHaltes")
    Call<List<Halte>> getAllHalte();

    @FormUrlEncoded
    @PUT("api/updateKmAwal")
    Call<ResponseBody> updateKmAwal(
            @Field("no_bus") String no_bus,
            @Field("tgl") String tgl,
            @Field("km_awal") Double km_awal
    );

}
