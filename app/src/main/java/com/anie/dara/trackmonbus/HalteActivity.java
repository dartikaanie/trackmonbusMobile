package com.anie.dara.trackmonbus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.anie.dara.trackmonbus.adapter.HalteAdapter;
import com.anie.dara.trackmonbus.model.Halte;
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

public class HalteActivity extends AppCompatActivity  implements OnMapReadyCallback, com.anie.dara.trackmonbus.adapter.HalteAdapter.OnItemClicked {
    GoogleMap map;
    RecyclerView rvListHalte;
    HalteAdapter halteAdapter;
    private dbClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halte);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        rvListHalte = findViewById(R.id.rvListHalte);
        halteAdapter = new HalteAdapter();
        halteAdapter.setClickHandler(this);

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

    }

    private void getAllHalte() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Memuat Data . . .");
        dialog.show();

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
                        initMarker(listHalteItem);
                        halteAdapter.setDataHalte(new ArrayList<Halte>(listHalteItem));

                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<Halte>> call, Throwable t) {
                    Toast.makeText(HalteActivity.this , t.toString(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }else{
            Toast.makeText(HalteActivity.this , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        getAllHalte();

    }

    private void initMarker(List<Halte> listData){
        for (int i=0; i<listData.size(); i++){
            LatLng location = new LatLng(Double.parseDouble(listData.get(i).getLat()), Double.parseDouble(listData.get(i).getLng()));
            map.addMarker(new MarkerOptions().position(location).title(listData.get(i).getNama())).showInfoWindow();
        }
        LatLng latLng = new LatLng(Double.parseDouble(listData.get(0).getLat()), Double.parseDouble(listData.get(0).getLng()));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude,latLng.longitude), 14.0f));
    }

    @Override
    public void ItemClicked(Halte halte) {
        Toast.makeText(HalteActivity.this, "Item yang diklik adalah : " + halte.getNama(), Toast.LENGTH_SHORT).show();
        LatLng latLng = new LatLng(Double.parseDouble(halte.getLat()), Double.parseDouble(halte.getLng()));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude,latLng.longitude), 16.0f));
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
