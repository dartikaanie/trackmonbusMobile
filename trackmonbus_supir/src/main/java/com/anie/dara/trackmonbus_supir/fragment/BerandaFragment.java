package com.anie.dara.trackmonbus_supir.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus_supir.MainActivity;
import com.anie.dara.trackmonbus_supir.R;
import com.anie.dara.trackmonbus_supir.dbClient;
import com.anie.dara.trackmonbus_supir.model.Halte;
import com.anie.dara.trackmonbus_supir.model.Jadwal;
import com.anie.dara.trackmonbus_supir.model.Login;
import com.anie.dara.trackmonbus_supir.rest.ApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
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
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.anie.dara.trackmonbus_supir.R.id.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class BerandaFragment extends Fragment  implements OnMapReadyCallback, View.OnClickListener {
    GoogleMap map;
    MapView mMapView;
    CardView cvChechkin, cvTidakJadwal;
    View mView;
    TextView noBus, no_tnkb, kapasitas, namaSupir, namaHalte, namaTrayek;
    DatabaseReference mDatabase;
    HashMap hashMapMarker = new HashMap<>();
    Marker marker;
    Button btnCheckin;
    static Activity activity;
    private dbClient client = ApiClient.getClient().create(dbClient.class);


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_beranda, container, false);
        cekJadwalKerja();

        noBus = mView.findViewById(R.id.NoBus);
        no_tnkb = mView.findViewById(R.id.no_tnkb);
        kapasitas = mView.findViewById(R.id.kapasitas);
        namaSupir = mView.findViewById(R.id.namaSupir2);
        namaHalte = mView.findViewById(R.id.namaHalte);
        namaTrayek = mView.findViewById(R.id.namaTrayek2);
        btnCheckin = mView.findViewById(R.id.btnCheckin);
        btnCheckin.setOnClickListener(this);

        cvTidakJadwal = mView.findViewById(R.id.cvTidakJadwal);
        cvTidakJadwal.setVisibility(View.INVISIBLE);
        cvChechkin = mView.findViewById(R.id.cardView);
        cvChechkin.setVisibility(View.INVISIBLE);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("2019-05-27");
//        setHasOptionsMenu(true);
        return mView;
    }



    private void cekJadwalKerja() {
        Call<Jadwal> call = client.cekJadwal();
        call.enqueue(new Callback<Jadwal>() {
            @Override
            public void onResponse(Call<Jadwal> call, Response<Jadwal> response) {
                Jadwal jadwal = response.body();
                    cvChechkin.setVisibility(View.VISIBLE);
                    noBus.setText(jadwal.getNo_bus());
                    no_tnkb.setText(jadwal.getNo_tnkb());
                    kapasitas.setText(jadwal.getKapasitas());
                    namaHalte.setText(jadwal.getNama());
                    namaTrayek.setText(jadwal.getTrayek());
                    cvTidakJadwal.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(),"Ada jadwal", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Jadwal> call, Throwable t) {
                cvChechkin.setVisibility(View.INVISIBLE);
                cvTidakJadwal.setVisibility(View.VISIBLE);
                Toast.makeText(getContext()," Tidak ada Jadwal", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onViewCreated(@NonNull View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map_supir);
       dataMarker();

        if(mMapView !=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }


    public void dataMarker(){
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LatLng posisi = null;
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
        MapsInitializer.initialize(getContext());
        map = googleMap;
        getAllHalte();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LatLng posisi = null;
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.trans);

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    String lat = child.child("lat").getValue().toString();
                    String lng = child.child("lng").getValue().toString();
                    String nomorBus = child.getKey().toString();
                    Log.e("datasnapsot", lat);

                    double location_lat = Double.parseDouble(lat);
                    double location_lng = Double.parseDouble(lng);
                    posisi = new LatLng(location_lat, location_lng);

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

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Memuat Data . . .");
        dialog.show();

        if(((MainActivity)activity).konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<List<Halte>> call = client.getAllHalte();
            call.enqueue(new Callback<List<Halte>>() {
                @Override
                public void onResponse(Call<List<Halte>> call,  Response<List<Halte>> response) {


                    List<Halte> listHalteItem = response.body();
                    if(listHalteItem == null){
                        Toast.makeText(getContext() , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext() , "Data Halte berhasil diload", Toast.LENGTH_SHORT).show();
                        initMarker(listHalteItem);
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<Halte>> call, Throwable t) {
                    Toast.makeText(getContext() , t.toString(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }else{
            Toast.makeText(getContext() , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private void initMarker(List<Halte> listData){

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.halte);
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
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap Icon = Bitmap.createScaledBitmap(b, width, height, false);
        return Icon;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCheckin:
                Toast.makeText(getContext() , "cek in", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
