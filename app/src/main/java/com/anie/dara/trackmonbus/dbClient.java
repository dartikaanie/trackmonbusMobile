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
    @GET("pesan")
    Call<List<Pesan>> getAllPesan();

    @GET("viewBuses")
    Call<List<Bus>> getAllBus();

    @GET("viewHaltes")
    Call<List<Halte>> getAllHalte();

    @GET("viewHaltes")
    Call<ListHalte> getAllLocation();





}
