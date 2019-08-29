package com.anie.dara.trackmonbus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.adapter.HalteAdapter;
import com.anie.dara.trackmonbus.adapter.JalurHalteAdapter;
import com.anie.dara.trackmonbus.model.Trayeks.HalteItem;
import com.anie.dara.trackmonbus.model.Trayeks.JalurItem;
import com.anie.dara.trackmonbus.model.Trayeks.Trayeks;
import com.anie.dara.trackmonbus.rest.dbClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HalteActivity extends AppCompatActivity  implements OnMapReadyCallback {
    GoogleMap map;
    RecyclerView rvListJalur;
    JalurHalteAdapter jalurAdapter;
    HalteAdapter halteAdapter;
    private dbClient client;
    Trayeks trayek;
    HashMap HalteMarkers = new HashMap<>();
    Marker marker;
    TextView tvdaftarHalte;
    android.support.v7.widget.Toolbar toolbar;
    ImageView toolbarTitle;
    List<HalteItem> halteItem;
    List<JalurItem> jalurItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halte);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = (ImageView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tvdaftarHalte = findViewById(R.id.daftarHalte);
        rvListJalur = findViewById(R.id.rvListJalur);
        jalurAdapter = new JalurHalteAdapter();
        rvListJalur.setAdapter(jalurAdapter);

        RecyclerView.LayoutManager layoutManager;


        layoutManager = new LinearLayoutManager(this);
        rvListJalur.setLayoutManager(layoutManager);
        rvListJalur.setHasFixedSize(true);

        Intent HalteIntent = getIntent();
        if (null != HalteIntent) {
            trayek = HalteIntent.getParcelableExtra("trayek");
        }

        getAllHalte();

        jalurAdapter.setClickHandler(new JalurHalteAdapter.Onlistener() {
            @Override
            public void HalteClickItem(HalteItem halte) {
                marker = (Marker) HalteMarkers.get(halte.getHalteId());
                marker.showInfoWindow();
                LatLng latLng = new LatLng(Double.parseDouble(halte.getLat()), Double.parseDouble(halte.getLng()));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude,latLng.longitude), 16.0f));
            }
        });
    }

    public void getAllHalte(){
        jalurItems= new ArrayList<>();
        halteItem =new ArrayList<>();
        for(JalurItem jalurItem : trayek.getJalur()){
            jalurItems.add(jalurItem);
            halteItem.addAll(jalurItem.getHalte());
        }
        Log.e("halte", String.valueOf(halteItem));
        jalurAdapter.setDataJalur(new ArrayList<JalurItem>(jalurItems));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        if(halteItem.size() >0){
            initMarker(halteItem);
        }
    }

    private void initMarker(List<HalteItem> listData){
        BitmapDrawable bitmapdraw= (BitmapDrawable) getResources().getDrawable(R.drawable.halte);
        if(listData.size() > 0 ){
            for (int i=0; i<listData.size(); i++){
                LatLng location = new LatLng(Double.parseDouble(listData.get(i).getLat()), Double.parseDouble(listData.get(i).getLng()));

                marker =  map.addMarker(new MarkerOptions().position(location).title(listData.get(i).getNama()).icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 100,80))));
                HalteMarkers.put(listData.get(i).getHalteId(),marker);
            }
            LatLng latLng = new LatLng(Double.parseDouble(listData.get(0).getLat()), Double.parseDouble(listData.get(0).getLng()));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude,latLng.longitude), 14.0f));
        }else{
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-0.924654, 100.361908), 15.0f));
        }

    }

    public Bitmap getIcon (BitmapDrawable bitmapdraw, int tinggi, int lebar){
        int height = tinggi;
        int width = lebar;
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap Icon = Bitmap.createScaledBitmap(b, width, height, false);
        return Icon;
    }


}
