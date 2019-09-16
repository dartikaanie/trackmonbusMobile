package com.anie.dara.trackmonbus;

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
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.adapter.HalteAdapter;
import com.anie.dara.trackmonbus.model.Bus;
import com.anie.dara.trackmonbus.model.Halte;
import com.anie.dara.trackmonbus.model.Posisi;
import com.anie.dara.trackmonbus.model.Trayeks.HalteItem;
import com.anie.dara.trackmonbus.model.Trayeks.JalurItem;
import com.anie.dara.trackmonbus.model.distanceMatrix.DistanceMatrix;
import com.anie.dara.trackmonbus.model.distanceMatrix.ElementsItem;
import com.anie.dara.trackmonbus.model.noBus;
import com.anie.dara.trackmonbus.rest.ApiClient;
import com.anie.dara.trackmonbus.rest.DistanceApiServices;
import com.anie.dara.trackmonbus.rest.dbClient;
import com.anie.dara.trackmonbus.rest.initLibrary;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LokasiActivity extends AppCompatActivity implements OnMapReadyCallback, HalteAdapter.OnHalteListener, View.OnClickListener {

    GoogleMap map;
    MapView mMapView;

    DatabaseReference mDatabase;
    TextView textView4, namaHalteTerdekat;
    JalurItem jalur;
    HashMap hashMapMarker = new HashMap<>();
    Marker marker, markerBus;
    HalteAdapter halteAdapter;
    RecyclerView rvDaftarHalte;
    ArrayList<HalteItem> ListHalte;
    HashMap HalteMarkers = new HashMap<>();
    Posisi noBusMinPosisi = null;
    ProgressDialog dialog ;
    Button btnCekHalte;
    Double currLat, currLng;
    CardView cv_halteTerdekat;

    CardView cardBus;
    TextView no_bus, no_tnkb, kapasitas, Tvdurasi;

    private static  final  int REQUEST_LOCATION =1;
    private LocationManager locationManager;
    Toolbar toolbar;
    ImageView toolbarTitle;

    HalteItem halte;
    private ArrayList<noBus> listBusSearah;
    AlertDialog alertDialog = null;
    private dbClient client = ApiClient.getClient().create(dbClient.class);
    private DistanceApiServices distanceApi = initLibrary.getClient().create(DistanceApiServices.class);
    private SwipeRefreshLayout swLayout;
    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = (ImageView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tgl_skrg = dateFormat.format(date);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(tgl_skrg);

        rvDaftarHalte = findViewById(R.id.rvDaftarHalte);
        cardBus = findViewById(R.id.cardBus);
        Tvdurasi = findViewById(R.id.TVdurasi);
        no_bus = findViewById(R.id.no_bus);
        no_tnkb = findViewById(R.id.no_tnkb);
        kapasitas = findViewById(R.id.kapasitas);
        namaHalteTerdekat = findViewById(R.id.namaHalteTerdekat);
        cv_halteTerdekat = findViewById(R.id.cv_halteTerdekat);
        btnCekHalte = findViewById(R.id.btn_halte_dekat);
        btnCekHalte.setOnClickListener(this);
        dialog = new ProgressDialog(LokasiActivity.this);

        swLayout = (SwipeRefreshLayout) findViewById(R.id.swlayout);
        LinearLayout llayout = (LinearLayout) findViewById(R.id.ll_swiperefresh);

        Intent IntentMove = getIntent();
        jalur = IntentMove.getParcelableExtra("jalur");
        halteAdapter = new HalteAdapter(jalur.getHalte(), this);
        ListHalte = new ArrayList<>();
        ListHalte.addAll(jalur.getHalte());
        rvDaftarHalte.setAdapter(halteAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(LokasiActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvDaftarHalte.setLayoutManager(layoutManager);
        rvDaftarHalte.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(tgl_skrg);


        swLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorYellow, R.color.colorRed);

        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Handler untuk menjalankan jeda selama 5 detik
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        // Berhenti berputar/refreshing
                        swLayout.setRefreshing(false);

                        // fungsi-fungsi lain yang dijalankan saat refresh berhenti
                        getCurrentPosisi();
                        Intent IntentMove = getIntent();
                        jalur = IntentMove.getParcelableExtra("jalur");
                        ListHalte = new ArrayList<>();
                        ListHalte.addAll(jalur.getHalte());
                        rvDaftarHalte.setAdapter(halteAdapter);
                        cardBus.setVisibility(View.INVISIBLE);
                    }
                }, 5000);
            }
        });
        mMapView = (MapView) findViewById(R.id.map_monitor);
//        dataMarker();

        if(mMapView !=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

//        getLocation();
    }



    public void getDataPosisiBus(DataSnapshot dataSnapshot){

        LatLng posisi = null;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.trans);

        for (DataSnapshot child : dataSnapshot.getChildren()) {

            String lat = child.child("lat").getValue().toString();
            String lng = child.child("lng").getValue().toString();
            String nomorBus = child.getKey().toString();

            double location_lat = Double.parseDouble(lat);
            double location_lng = Double.parseDouble(lng);
            posisi = new LatLng(location_lat, location_lng);

            markerBus = map.addMarker(new MarkerOptions()
                    .position(posisi)
                    .title(nomorBus)
                    .icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 60,120))));
            hashMapMarker.put(nomorBus,markerBus);

            Log.e("noBus", nomorBus);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        map = googleMap;
        if(ListHalte.size() >0){
            initMarker(ListHalte);
        }

        getCurrentPosisi();

        dataMarker();

    }

    public void dataMarker(){
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                 getDataAksi(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.e("bus change", String.valueOf(dataSnapshot));
                getDataAksi(dataSnapshot);

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



    public void getCurrentPosisi(){
        Double latitude = 0.0, longitude;
        LocationManager mlocManager = null;
        LocationListener mlocListener;
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new loclistener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            latitude = loclistener.latitude;
            longitude = loclistener.longitude;
            Toast.makeText(getApplicationContext(), latitude.toString(), Toast.LENGTH_LONG).show();
            if (latitude == 0.0) {
                Toast.makeText(getApplicationContext(), "GPS tidak bisa mendeteksi lokasi anda", Toast.LENGTH_LONG).show();
            }else{
                currLng = longitude;
                currLat = latitude;
                LatLng currentPosisi = new LatLng(latitude, longitude);
                map.addMarker(new MarkerOptions()
                        .position(currentPosisi)
                        .title("Anda")).showInfoWindow();
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16.0f));
            }

        } else {
            buildAlertMessageNoGPS();
            Toast.makeText(getApplicationContext(), "GPS Anda mati. Silakan Aktifkan GPS Anda.", Toast.LENGTH_LONG).show();
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

    private void initMarker(ArrayList<HalteItem> listData){
        BitmapDrawable bitmapdraw= (BitmapDrawable) getResources().getDrawable(R.drawable.halte);
        if(listData.size() > 0 ){
            for (int i=0; i<listData.size(); i++){
                LatLng location = new LatLng(listData.get(i).getLat(), listData.get(i).getLng());

                marker =  map.addMarker(new MarkerOptions().position(location).title(listData.get(i).getNama()).icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 100,80))));
                HalteMarkers.put(listData.get(i).getHalteId(),marker);
            }
            LatLng latLng = new LatLng(listData.get(0).getLat(), listData.get(0).getLng());
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(16));
        }
    }

    public Bitmap getIcon (BitmapDrawable bitmapdraw, int tinggi, int lebar){
        int height = tinggi;
        int width = lebar;
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap Icon = Bitmap.createScaledBitmap(b, width, height, false);
        return Icon;
    }

    @Override
    public void onHalteClick(final HalteItem halteItem) {
        cardBus.setVisibility(View.INVISIBLE);
        marker = (Marker) HalteMarkers.get(halteItem.getHalteId());
        marker.showInfoWindow();
        LatLng latLng = new LatLng(halteItem.getLat(),halteItem.getLng());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LokasiActivity.this);
        alertDialogBuilder
                .setMessage("Hitung waktu kedatangan bus dari halte "+ halteItem.getNama() +" ? ")
                .setIcon(R.drawable.trans)
                .setCancelable(false)
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDurasi(halteItem, jalur.getJalurId());

                    }
                }).setNegativeButton("Batal", null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    public void getDurasi(final HalteItem halteItem, String jalurId){

        dialog.setMessage("Memuat Data . . .");
        dialog.show();
        listBusSearah = new ArrayList<>();
        Call<List<noBus>> call = client.getBusSearah(jalurId);
        call.enqueue(new Callback<List<noBus>>() {
            @Override
            public void onResponse(Call<List<noBus>> call, Response<List<noBus>> response) {
                List<noBus> result = response.body();
                if(result.size() > 0){
                    for(noBus item : result){
                        listBusSearah.add(new noBus(item.getNo_bus()));
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<String> posisiDestination = new ArrayList<>();
                                ArrayList<Posisi> posisiBus = new ArrayList<>();

                                String posisiHalte = halteItem.getLat() + "," + halteItem.getLng();
                                int n=0;

                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    for(noBus busSearah : listBusSearah){

                                        if(child.getKey().equals(busSearah.getNo_bus())){
                                            double location_lat = Double.parseDouble(child.child("lat").getValue().toString());
                                            double location_lng = Double.parseDouble(child.child("lng").getValue().toString());

                                            String noBuscek = child.getKey().toString();
                                            Posisi dataBus = new Posisi(location_lat,location_lng,noBuscek);
                                            posisiBus.add(n,dataBus);
                                            n++;

                                            String posisiBusCek = location_lat + "," + location_lng;
                                            posisiDestination.add(posisiBusCek);
                                        }

                                    }


                                }

                                if(posisiDestination.size()>0){
                                    if(alertDialog != null){
                                        alertDialog.dismiss();
                                    }
                                    actionRoute(posisiHalte, convertToString(posisiDestination),posisiBus );

                                }else{
                                    showAlert("Tidak ada bus yang berkendara pada jalur ini saat ini");
                                }
                                dialog.dismiss();

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }else{
                    showAlert("Tidak ada bus yang berkendara pada saat ini");
                    Log.e("listBus", "DAta tidak ada");
                }
            }
            @Override
            public void onFailure(Call<List<noBus>> call, Throwable t) {
                Log.e("error listBus", t.toString());
            }
        });

    }

    static String convertToString(ArrayList<String> strings) {
        StringBuilder builder = new StringBuilder();
        for (String item : strings) {
            builder.append(item);
            builder.append("|");
        }
        builder.setLength(builder.length()-1);
        return builder.toString();
    }

    public void showAlert(String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LokasiActivity.this);
        alertDialogBuilder
                .setMessage(msg)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void actionRoute(final String posisiHalte , String posisiBusCek, final ArrayList<Posisi> busPosisi) {
        dialog.setMessage("Memuat Data . . .");
        dialog.show();
        Log.e("cekmarker", posisiBusCek);
        Call<DistanceMatrix> call = distanceApi.getDistanceInfo(posisiHalte,posisiBusCek, "now","true","AIzaSyDZ-N9it_JFpboG3R3LfxakMiAkUdF12bU");
        call.enqueue(new Callback<DistanceMatrix>() {
            @Override
            public void onResponse(Call<DistanceMatrix> call, Response<DistanceMatrix> response) {
                DistanceMatrix data = response.body();
                if(data.getRows().get(0).getElements().size() > 0){
                    List<ElementsItem> row = data.getRows().get(0).getElements();
                   int durasiMin=0, durasiInt=0;
                    String durasi = null;
                    int n=0;
                    Log.e("durasi min", posisiHalte);
                    for(ElementsItem item : row){
                        if(n==0){
                            durasiInt= item.getDurationInTraffic().getValue();
                            durasi = item.getDurationInTraffic().getText();
                            noBusMinPosisi = new Posisi(busPosisi.get(n).getLat(), busPosisi.get(n).getLng(),busPosisi.get(n).getNo_bus());
                            durasiMin=durasiInt;
                            n++;
                        }else{
                            durasiInt= item.getDuration().getValue();

                            if(durasiInt  < durasiMin){
                                durasiMin = durasiInt;
                                durasi = item.getDurationInTraffic().getText();
                                noBusMinPosisi = new Posisi(busPosisi.get(n).getLat(), busPosisi.get(n).getLng(),busPosisi.get(n).getNo_bus());
                            }
                            n++;
                        }
                    }
                    if(noBusMinPosisi != null){
                        if(alertDialog != null){
                            alertDialog.dismiss();
                        }
                        dialog.dismiss();
                        marker = (Marker) hashMapMarker.get(noBusMinPosisi.getNo_bus());
                        Log.e("cekmarker", String.valueOf(hashMapMarker.get(noBusMinPosisi.getNo_bus())));
                        if(marker != null){
                            marker.showInfoWindow();
                        }
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(noBusMinPosisi.getLat(), noBusMinPosisi.getLng()), 16.0f));

                        ShowDurasi(durasi, noBusMinPosisi.getNo_bus());
                    }
                }

            }

            @Override
            public void onFailure(Call<DistanceMatrix> call, Throwable t) {
                Log.e("ggl", String.valueOf("ggl"));
            }
        });
    }

//    public void getJarakHalteBus(){
//        String PosisiBus = noBusMinPosisi.getLat() + "," + noBusMinPosisi.getLng();
//        for(HalteItem halte : ListHalte){
//            String PosisiHalte = halte.getLat()+ "," + halte.getLng();
//            Call<DistanceMatrix> call = distanceApi.getDistanceInfo(PosisiHalte,PosisiBus, "AIzaSyDZ-N9it_JFpboG3R3LfxakMiAkUdF12bU");
//            call.enqueue(new Callback<DistanceMatrix>() {
//                @Override
//                public void onResponse(Call<DistanceMatrix> call, Response<DistanceMatrix> response) {
//                    DistanceMatrix data = response.body();
//
//                }
//
//                @Override
//                public void onFailure(Call<DistanceMatrix> call, Throwable t) {
//                    Log.e("ggl", String.valueOf("ggl"));
//                }
//            });
//        }
//
//
//    }


    private void ShowDurasi(String durasi, String noBus){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LokasiActivity.this);
        alertDialogBuilder
                .setMessage("Waktu tunggu kedatangan bus : " + durasi)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        dialog.setMessage("Memuat Data . . .");
        dialog.show();

        Tvdurasi.setText(durasi);
        Call<Bus> call = client.getDataBus(noBus);
        call.enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                if(konekkah()){
                    Bus dataBus = response.body();
                    if(dataBus != null) {
                        no_bus.setText(dataBus.getNo_bus());
                        no_tnkb.setText(dataBus.getNo_tnkb());
                        kapasitas.setText(Integer.toString(dataBus.getKapasitas()) + " orang");
                        cardBus.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(LokasiActivity.this, "Maaf Ada kesalahan", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Aktifkan Jaringan Internet Anda", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }

            @Override
            public void onFailure(Call<Bus> call, Throwable t) {
               Log.e("error get data bus", t.toString());
            }
        });

        dialog.dismiss();
    }


    public void getDataAksi(DataSnapshot dataSnapshot){
        LatLng point = null;
        BitmapDrawable bitmapdraw= (BitmapDrawable) getResources().getDrawable(R.drawable.trans);

        String lat = dataSnapshot.child("lat").getValue().toString();
        String lng = dataSnapshot.child("lng").getValue().toString();
        String nomorBus = dataSnapshot.getKey().toString();

        double location_lat = Double.parseDouble(lat);
        double location_lng = Double.parseDouble(lng);
        point = new LatLng(location_lat, location_lng);
        marker = (Marker) hashMapMarker.get(nomorBus);
        if(marker != null){
            marker.remove();
            hashMapMarker.remove(nomorBus);
        }

        marker = map.addMarker(new MarkerOptions()
                .position(point)
                .title(nomorBus)
                .icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 60,120))));
        hashMapMarker.put(nomorBus,marker);
    }
    public Boolean konekkah(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean konek = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return konek;
    }

    public void cekHalteTerdekat(Double currLat, Double currLng){
        if(ListHalte.size() >0){
            dialog.setMessage("Memuat Data . . .");
            dialog.show();
            ArrayList<String> daftarPosisiHalte = new ArrayList<>();;
            for(HalteItem itemHalte : ListHalte){
                String posisiHalte = itemHalte.getLat() + "," + itemHalte.getLng();
                daftarPosisiHalte.add(posisiHalte);
            }
            String posisiUser = currLat +","+ currLng;
            Call<DistanceMatrix> call = distanceApi.getDistanceInfo(posisiUser,convertToString(daftarPosisiHalte), "DRIVING", "true","AIzaSyDZ-N9it_JFpboG3R3LfxakMiAkUdF12bU");
            call.enqueue(new Callback<DistanceMatrix>() {
                @Override
                public void onResponse(Call<DistanceMatrix> call, Response<DistanceMatrix> response) {
                    DistanceMatrix data = response.body();
                    Log.e("cekdata", data.toString());
                    if(data.getRows().get(0).getElements().size() > 0){
                        List<ElementsItem> row = data.getRows().get(0).getElements();
                        int jarakMin = 0;
                        int jarak;
                        HalteItem halteMin = null;
                        int n=0;
                        for(ElementsItem item : row){
                            if(item.getDistance() != null){
                                if(n==0){
                                    jarak= item.getDistance().getValue();
                                    jarakMin=jarak;
                                    halteMin = ListHalte.get(n);
                                    n++;
                                }else{
                                    jarak= item.getDistance().getValue();
                                    if(jarak  < jarakMin){
                                        jarakMin = jarak;
                                        halteMin = ListHalte.get(n);
                                    }
                                    n++;
                                }   
                            }
                        }
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(halteMin.getLat(), halteMin.getLng()), 16.0f));
                        namaHalteTerdekat.setText(halteMin.getNama());
                        cv_halteTerdekat.setVisibility(View.VISIBLE);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LokasiActivity.this);
                        final HalteItem finalHalteMin = halteMin;
                        alertDialogBuilder
                                .setMessage("Hitung waktu kedatangan bus dari halte "+ halteMin.getNama() +" ? ")
                                .setIcon(R.drawable.trans)
                                .setCancelable(false)
                                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        getDurasi(finalHalteMin, jalur.getJalurId());
                                        dialog.dismiss();

                                    }
                                }).setNegativeButton("Batal", null);
                        dialog.dismiss();
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }
                }

                @Override
                public void onFailure(Call<DistanceMatrix> call, Throwable t) {
                    Log.e("ggl", String.valueOf(t));
                    dialog.dismiss();
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_halte_dekat:
                getCurrentPosisi();
                cekHalteTerdekat(currLat, currLng);
                break;
        }
    }
}
