package com.anie.dara.trackmonbus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.model.Halte;
import com.anie.dara.trackmonbus.rest.ApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LokasiActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("2019-05-27");
    private dbClient client;
    TextView textView4;
    Button btnCurrentLok;
    HashMap hashMapMarker = new HashMap<>();
    Marker marker;

    private static  final  int REQUEST_LOCATION =1;
    LocationManager locationManager;
    String latitude, longtitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi);
        textView4 = findViewById(R.id.textView4);
        btnCurrentLok = findViewById(R.id.btnCurrentLok);
        //klik button_location
        btnCurrentLok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) LokasiActivity.this.getSystemService(Context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    buildAlertMessageNoGPS();
                }
                else
                {
                    getLocation();
                }

            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_lokasi);
        mapFragment.getMapAsync(this);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LatLng posisi = null;
//                int height = 60;
//                int width = 120;
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.trans);
                    String lat = dataSnapshot.child("lat").getValue().toString();
                    String lng = dataSnapshot.child("lng").getValue().toString();
                    String nomorBus = dataSnapshot.getKey().toString();
                    Log.e("datasnapsot2", lat);

                    double location_lat = Double.parseDouble(lat);
                    double location_lng = Double.parseDouble(lng);
                    posisi = new LatLng(location_lat, location_lng);
                marker = (Marker) hashMapMarker.get(nomorBus);
                marker.remove();
                hashMapMarker.remove(nomorBus);
                marker = map.addMarker(new MarkerOptions()
                        .position(posisi)
                        .title(nomorBus)
                        .icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 60,120))));
                hashMapMarker.put(nomorBus,marker);
                }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getAllHalte();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LatLng posisi = null;
//                int height = 60;
//                int width = 120;
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.trans);

//                Bitmap b=bitmapdraw.getBitmap();
//                Bitmap BusMarker = Bitmap.createScaledBitmap(b, width, height, false);

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    String lat = child.child("lat").getValue().toString();
                    String lng = child.child("lng").getValue().toString();
                    String nomorBus = child.getKey().toString();
                    Log.e("datasnapsot", lat);

                    double location_lat = Double.parseDouble(lat);
                    double location_lng = Double.parseDouble(lng);
                    posisi = new LatLng(location_lat, location_lng);
//                    map.addMarker(new MarkerOptions().position(cod).title(nomorBus));
//                    map.addMarker(new MarkerOptions()
//                            .position(posisi)
//                            .title(nomorBus)
//                            .icon(BitmapDescriptorFactory.fromBitmap(BusMarker))
//                    ).showInfoWindow();

                    marker = map.addMarker(new MarkerOptions()
                            .position(posisi)
                            .title(nomorBus)
                            .icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 60,120))));
                    hashMapMarker.put(nomorBus,marker);
                }
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(posisi.latitude,posisi.longitude), 16.0f));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.toString());

            }
        });

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


                    List<Halte> listHalteItem = response.body();
                    if(listHalteItem == null){
                        Toast.makeText(LokasiActivity.this , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LokasiActivity.this , "Data Halte berhasil diload", Toast.LENGTH_SHORT).show();
                        initMarker(listHalteItem);
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<Halte>> call, Throwable t) {
                    Toast.makeText(LokasiActivity.this , t.toString(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }else{
            Toast.makeText(LokasiActivity.this , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private void initMarker(List<Halte> listData){
//        int height = 100;
//        int width = 80;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.halte);
//        Bitmap b=bitmapdraw.getBitmap();
//        Bitmap HalteMarker = Bitmap.createScaledBitmap(b, width, height, false);
        for (int i=0; i<listData.size(); i++){
            LatLng location = new LatLng(Double.parseDouble(listData.get(i).getLat()), Double.parseDouble(listData.get(i).getLng()));
            map.addMarker(new MarkerOptions().position(location).title(listData.get(i).getNama()).icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 100,80)))).showInfoWindow();
        }
        LatLng latLng = new LatLng(Double.parseDouble(listData.get(0).getLat()), Double.parseDouble(listData.get(0).getLng()));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude,latLng.longitude), 15.0f));
    }

    public Bitmap getIcon (BitmapDrawable bitmapdraw, int tinggi, int lebar){
        int height = tinggi;
        int width = lebar;
//        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(drawable);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap Icon = Bitmap.createScaledBitmap(b, width, height, false);
        return Icon;
    }


    public void getLocation(){
        if((ActivityCompat.checkSelfPermission(LokasiActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )
                &&
                (ActivityCompat.checkSelfPermission(LokasiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(LokasiActivity.this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else{
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null){

                double lat = location.getLatitude();
                double lng = location.getLongitude();
                LatLng currentPosisi = new LatLng(lat, lng);
                map.addMarker(new MarkerOptions()
                        .position(currentPosisi)
                        .title("Anda"));
                 map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentPosisi.latitude,currentPosisi.longitude), 16.0f));
            }
            else{
                Toast.makeText(LokasiActivity.this,"TIDAK TAMPIL LOKASINYA YA", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGPS(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(LokasiActivity.this);
        builder.setMessage("Hidupkan GPS mu")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
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
