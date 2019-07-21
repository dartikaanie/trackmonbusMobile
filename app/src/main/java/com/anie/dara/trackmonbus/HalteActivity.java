package com.anie.dara.trackmonbus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.adapter.HalteAdapter;
import com.anie.dara.trackmonbus.model.Halte;
import com.anie.dara.trackmonbus.model.Trayek;
import com.anie.dara.trackmonbus.rest.ApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HalteActivity extends AppCompatActivity  implements OnMapReadyCallback, com.anie.dara.trackmonbus.adapter.HalteAdapter.OnItemClicked {
    GoogleMap map;
    RecyclerView rvListHalte;
    HalteAdapter halteAdapter;
    private dbClient client;
    Trayek trayek;
    HashMap HalteMarkers = new HashMap<>();
    Marker marker;
    TextView tvdaftarHalte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halte);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvdaftarHalte = findViewById(R.id.daftarHalte);
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

        Intent HalteIntent = getIntent();
        if(null != HalteIntent) {
            trayek = HalteIntent.getParcelableExtra("trayek");
        }



    }

    private void getAllHalte(String trayek_id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Memuat Data . . .");
        dialog.show();

        if(konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Log.e("trayek", trayek_id);
            Call<List<Halte>> call = client.getAllHalte(trayek_id);
            call.enqueue(new Callback<List<Halte>>() {
                @Override
                public void onResponse(Call<List<Halte>> call,  Response<List<Halte>> response) {

                    rvListHalte.setVisibility(View.VISIBLE);

                    List<Halte> listHalteItem = response.body();
                    Log.e("trayek_list", String.valueOf(response.body()));
                    if(listHalteItem == null){
                        tvdaftarHalte.setText("Belum Ada halte pada trayek ini");
                        Toast.makeText(HalteActivity.this , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(HalteActivity.this , "Data berhasil diload", Toast.LENGTH_SHORT).show();
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

        getAllHalte(trayek.getTrayek_id());

    }

    private void initMarker(List<Halte> listData){
        if(listData.size() > 0 ){
            for (int i=0; i<listData.size(); i++){
                LatLng location = new LatLng(Double.parseDouble(listData.get(i).getLat()), Double.parseDouble(listData.get(i).getLng()));

                marker =  map.addMarker(new MarkerOptions().position(location).title(listData.get(i).getNama()));
                HalteMarkers.put(listData.get(i).getHalte_id(),marker);
            }
            LatLng latLng = new LatLng(Double.parseDouble(listData.get(0).getLat()), Double.parseDouble(listData.get(0).getLng()));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude,latLng.longitude), 14.0f));
        }else{
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-0.924654, 100.361908), 15.0f));
        }

    }

    @Override
    public void ItemClicked(Halte halte) {
        Toast.makeText(HalteActivity.this, "Item yang diklik adalah : " + halte.getNama(), Toast.LENGTH_SHORT).show();
        marker = (Marker) HalteMarkers.get(halte.getHalte_id());
        marker.showInfoWindow();
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
