package com.anie.dara.trackmonbus;

import com.anie.dara.trackmonbus.model.Bus;
import com.anie.dara.trackmonbus.model.Halte;
import com.anie.dara.trackmonbus.model.ListHalte;
import com.anie.dara.trackmonbus.model.Pesan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface dbClient {
    //mengambil data yang sdg tayang
    @GET("api/pesan")
    Call<List<Pesan>> getAllPesan();

    @GET("api/viewBuses")
    Call<List<Bus>> getAllBus();

    @GET("api/viewHaltes")
    Call<List<Halte>> getAllHalte();

    @GET("api/viewHaltes")
    Call<ListHalte> getAllLocation();







}
