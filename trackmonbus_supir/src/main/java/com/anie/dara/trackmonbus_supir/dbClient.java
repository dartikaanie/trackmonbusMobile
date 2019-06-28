package com.anie.dara.trackmonbus_supir;

import com.anie.dara.trackmonbus_supir.model.Halte;
import com.anie.dara.trackmonbus_supir.model.Jadwal;
import com.anie.dara.trackmonbus_supir.model.Login;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface dbClient {
    @FormUrlEncoded
    @POST("api/login_supir")
    Call<Login> Loginv2(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("api/cekJadwal")
    Call<Jadwal> cekJadwal();

    @GET("api/viewHaltes")
    Call<List<Halte>> getAllHalte();

}
