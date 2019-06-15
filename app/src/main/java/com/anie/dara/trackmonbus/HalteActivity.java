package com.anie.dara.trackmonbus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.anie.dara.trackmonbus.adapter.HalteAdapter;
import com.anie.dara.trackmonbus.adapter.pesanAdapter;
import com.anie.dara.trackmonbus.model.Halte;
import com.anie.dara.trackmonbus.model.Pesan;
import com.anie.dara.trackmonbus.rest.ApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HalteActivity extends AppCompatActivity  implements OnMapReadyCallback {
    GoogleMap map;
    RecyclerView rvListHalte;
    HalteAdapter halteAdapter;
    private dbClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halte);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setTitle("Trans Padang");
//        toolbar.setSubtitle("Halte");
//        toolbar.setLogo(R.drawable.trans);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        rvListHalte = findViewById(R.id.rvListHalte);
        halteAdapter = new HalteAdapter();

        rvListHalte.setAdapter(halteAdapter);
        RecyclerView.LayoutManager layoutManager;

        rvListHalte.setVisibility(View.INVISIBLE);

        int orientasi = getResources().getConfiguration().orientation;
        if(orientasi == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new LinearLayoutManager(this);
        }else{
            layoutManager = new GridLayoutManager(this,2);
        }
        rvListHalte.setLayoutManager(layoutManager);
        rvListHalte.setHasFixedSize(true);
        getAllHalte();
    }

    private void getAllHalte() {
        if(konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<List<Halte>> call = client.getAllHalte();
            call.enqueue(new Callback<List<Halte>>() {
                @Override
                public void onResponse(Call<List<Halte>> call,  Response<List<Halte>> response) {

                    rvListHalte.setVisibility(View.VISIBLE);


                    List<Halte> listHalteItem = response.body();
                    if(listHalteItem == null){
                        Toast.makeText(HalteActivity.this , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(HalteActivity.this , "Pesan berhasil diload", Toast.LENGTH_SHORT).show();
                        halteAdapter.setDataHalte(new ArrayList<Halte>(listHalteItem));
                    }

                }

                @Override
                public void onFailure(Call<List<Halte>> call, Throwable t) {
                    Toast.makeText(HalteActivity.this , t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(HalteActivity.this , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        LatLng posisi = new LatLng(-0.926256, 100.431219);
        map.addMarker(new MarkerOptions().position(posisi).title("IRIGASI"));
        map.moveCamera(CameraUpdateFactory.newLatLng(posisi));
        map.animateCamera(CameraUpdateFactory.zoomTo(16));

    }
    public Boolean konekkah(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean konek = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return konek;
    }

}
