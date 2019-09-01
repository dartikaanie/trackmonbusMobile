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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.adapter.HalteAdapter;
import com.anie.dara.trackmonbus.model.Bus;
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

public class LokasiActivity extends AppCompatActivity implements OnMapReadyCallback, HalteAdapter.OnHalteListener {

    GoogleMap map;
    MapView mMapView;

    DatabaseReference mDatabase;
    TextView textView4;
    JalurItem jalur;
    HashMap hashMapMarker = new HashMap<>();
    Marker marker, markerBus;
    HalteAdapter halteAdapter;
    RecyclerView rvDaftarHalte;
    ArrayList<HalteItem> ListHalte;
    HashMap HalteMarkers = new HashMap<>();

    ProgressDialog dialog ;

    CardView cardBus;
    TextView no_bus, no_tnkb, kapasitas, Tvdurasi;

    private static  final  int REQUEST_LOCATION =1;
    private LocationManager locationManager;
    Location currentLocation;
    Posisi currentPosisi;

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

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getDataPosisiBus(dataSnapshot);
                Log.e("get mdatabase", String.valueOf(dataSnapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.toString());

            }
        });

        dataMarker();

        //map ke lokasi terkini
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGPS();
        }
        else
        {
            getLocation();
            getCurrentPosisi();
        }






    }

    public void dataMarker(){
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                 getDataAksi(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getDataAksi(dataSnapshot);
                Log.e("bus change", s);
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

        if((ActivityCompat.checkSelfPermission(LokasiActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )
                &&
                (ActivityCompat.checkSelfPermission(LokasiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions((Activity) LokasiActivity.this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            Toast.makeText(LokasiActivity.this,"Aktifkan GPS anda", Toast.LENGTH_SHORT).show();
        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                Double lat = location.getLatitude();
                Double lng = location.getLongitude();
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16.0f));
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

    private void initMarker(ArrayList<HalteItem> listData){
        BitmapDrawable bitmapdraw= (BitmapDrawable) getResources().getDrawable(R.drawable.halte);
        if(listData.size() > 0 ){
            for (int i=0; i<listData.size(); i++){
                LatLng location = new LatLng(Double.parseDouble(listData.get(i).getLat()), Double.parseDouble(listData.get(i).getLng()));

                marker =  map.addMarker(new MarkerOptions().position(location).title(listData.get(i).getNama()).icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 100,80))));
                HalteMarkers.put(listData.get(i).getHalteId(),marker);
            }
            LatLng latLng = new LatLng(Double.parseDouble(listData.get(0).getLat()), Double.parseDouble(listData.get(0).getLng()));
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
        LatLng latLng = new LatLng(Double.parseDouble(halteItem.getLat()), Double.parseDouble(halteItem.getLng()));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude,latLng.longitude), 14.0f));

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
                                    Log.e("cek posisi", posisiDestination.toString());
                                }else{
                                    Toast.makeText(LokasiActivity.this,"TIDAK ADA BUS YANG BERKENDARA", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }else{
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

    private void actionRoute(String posisiHalte , String posisiBusCek, final ArrayList<Posisi> busPosisi) {
        dialog.setMessage("Memuat Data . . .");
        dialog.show();
        Call<DistanceMatrix> call = distanceApi.getDistanceInfo(posisiHalte,posisiBusCek, "AIzaSyDZ-N9it_JFpboG3R3LfxakMiAkUdF12bU");
        call.enqueue(new Callback<DistanceMatrix>() {
            @Override
            public void onResponse(Call<DistanceMatrix> call, Response<DistanceMatrix> response) {
                DistanceMatrix data = response.body();
                if(data.getRows().get(0).getElements().size() > 0){
                    List<ElementsItem> row = data.getRows().get(0).getElements();
                   int durasiMin=0, durasiInt=0;
                    String durasi = null;
                    int n=0;
//                    String noBusMin = null;
                    Posisi noBusMinPosisi = null;
                    for(ElementsItem item : row){
                        if(n==0){
                            durasiInt= item.getDuration().getValue();
                            durasi = item.getDuration().getText();
//                            noBusMin = busPosisi.get(n).getNo_bus();
                            noBusMinPosisi = new Posisi(busPosisi.get(n).getLat(), busPosisi.get(n).getLng(),busPosisi.get(n).getNo_bus());
                            durasiMin=durasiInt;
                            n++;
                        }else{
                            durasiInt= item.getDuration().getValue();

                            if(durasiInt  < durasiMin){
                                durasiMin = durasiInt;
                                durasi = item.getDuration().getText();
//                                noBusMin = busPosisi.get(n).getNo_bus();
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

    public void getLocation(){
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
           Log.e("eror", latitude +" - - " + longitude);
            if (latitude == 0.0) {
                Toast.makeText(getApplicationContext(), "Currently gps has not found your location....", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "GPS is currently off...", Toast.LENGTH_LONG).show();
        }
    }



}
