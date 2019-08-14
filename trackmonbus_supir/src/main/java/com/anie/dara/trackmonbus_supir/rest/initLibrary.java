package com.anie.dara.trackmonbus_supir.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class initLibrary {
    //https://maps.googleapis.com/maps/api/directions/json?origin=Cirebon,ID&destination=Jakarta,ID&api_key=YOUR_API_KEY
//   json?origins=-0.4653437,100.3840054&destinations=-0.930059%2C100.425318%7C-0.466%2C100.38316&key=AIzaSyDZ-N9it_JFpboG3R3LfxakMiAkUdF12bU
    public static String BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
