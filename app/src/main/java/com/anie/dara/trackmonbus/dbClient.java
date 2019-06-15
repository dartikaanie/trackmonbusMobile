package com.anie.dara.trackmonbus;

import com.anie.dara.trackmonbus.model.Pesan;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface dbClient {
    //mengambil data yang sdg tayang
    @GET("pesan")
    Call<List<Pesan>> getAllPesan();





}
