package com.anie.dara.trackmonbus_supir;

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
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus_supir.distanceMatrix.DistanceMatrix;
import com.anie.dara.trackmonbus_supir.distanceMatrix.ElementsItem;
import com.anie.dara.trackmonbus_supir.model.Halte;
import com.anie.dara.trackmonbus_supir.model.Posisi;
import com.anie.dara.trackmonbus_supir.model.Rit;
import com.anie.dara.trackmonbus_supir.model.jadwal.JadwalDetail;
import com.anie.dara.trackmonbus_supir.model.noBus;
import com.anie.dara.trackmonbus_supir.rest.ApiClient;
import com.anie.dara.trackmonbus_supir.rest.DistanceApiServices;
import com.anie.dara.trackmonbus_supir.rest.dbClient;
import com.anie.dara.trackmonbus_supir.rest.initLibrary;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.substring;

public class MonitoringPosisi extends AppCompatActivity implements  View.OnClickListener, OnMapReadyCallback {


    JadwalDetail jadwal;
    TextView NoBus, no_tnkb, kapasitas, namaJalur, namaSupir, namaTrayek, hari_tgl, namaPramugara;
    Button btnCheck, btnDetail, btnUbah, btnSelesai;
    GoogleMap map;
    MapView mMapView;
    HashMap hashMapMarker = new HashMap<>();
    Marker marker;
    DatabaseReference mDatabase;
    EditText etKmAwal;
    String no_bus, tgl;
    private List<noBus> listBusSearah;
    private dbClient client = ApiClient.getClient().create(dbClient.class);
    private DistanceApiServices distanceApi = initLibrary.getClient().create(DistanceApiServices.class);
//    LocationManager locationManager;
    Location currentLocation;
    Posisi currentPosisi;
    AlertDialog alertDialog = null;
    private static  final  int REQUEST_LOCATION =1;
    private SQLiteDatabaseHandler db;

    private Handler handler = new Handler();
    public boolean running = true;
    Toolbar toolbar;
    ImageView toolbarTitle;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_posisi);

        //set toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NoBus = findViewById(R.id.NoBus);
        no_tnkb = findViewById(R.id.no_tnkb);
        kapasitas = findViewById(R.id.kapasitas);
        namaJalur = findViewById(R.id.namaJalur);
        namaSupir = findViewById(R.id.namaSupir);
        hari_tgl = findViewById(R.id.tgl);
        namaTrayek = findViewById(R.id.namaTrayek2);

        btnCheck = findViewById(R.id.btnCheckpoint);
        btnCheck.setOnClickListener(this);
        btnDetail = findViewById(R.id.btnDetail);
        btnDetail.setOnClickListener(this);
        btnUbah = findViewById(R.id.btnUbah);
        btnUbah.setOnClickListener(this);
        btnSelesai = findViewById(R.id.btn_selesai);
        btnSelesai.setOnClickListener(this);

        etKmAwal = findViewById(R.id.etKmAwal);
        db = new SQLiteDatabaseHandler(this);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tgl_skrg = dateFormat.format(date);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(tgl_skrg);
        jadwal =   getIntent().getExtras().getParcelable("jadwal");
        if(jadwal != null){
            NoBus.setText(jadwal.getJadwal().getBuses().getNoBus());
            no_tnkb.setText(jadwal.getJadwal().getBuses().getNoTnkb());
            kapasitas.setText(Integer.toString(jadwal.getJadwal().getBuses().getKapasitas()));
            namaJalur.setText(jadwal.getJadwal().getDetailTrayeks().getJalurs().getNamaJalur());
            namaSupir.setText(jadwal.getUsers().getName() + " - "+ jadwal.getPramugaras().getNamaPramugara() );
            namaTrayek.setText(jadwal.getJadwal().getDetailTrayeks().getTrayeks().getTrayek());
            no_bus =jadwal.getJadwal().getBuses().getNoBus();
            tgl = jadwal.getTgl();
            hari_tgl.setText(substring(jadwal.getTgl(),0,10));
        }

        mMapView = (MapView) findViewById(R.id.map_monitor);
//        dataMarker();

        if(mMapView !=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        handler.postDelayed(runTrack, 5000);
        getLocation();
    }

    public void getDataPosisiBus(DataSnapshot dataSnapshot){

        LatLng posisi = null;
        BitmapDrawable bitmapdraw;



        for (DataSnapshot child : dataSnapshot.getChildren()) {

            String lat = child.child("lat").getValue().toString();
            String lng = child.child("lng").getValue().toString();
            String nomorBus = child.getKey().toString();

            if(nomorBus.equals(no_bus)){
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.transsupir);
            }else{
                bitmapdraw= (BitmapDrawable) getResources().getDrawable(R.drawable.trans);
            }

            double location_lat = Double.parseDouble(lat);
            double location_lng = Double.parseDouble(lng);
            posisi = new LatLng(location_lat, location_lng);

            marker = map.addMarker(new MarkerOptions()
                    .position(posisi)
                    .title(nomorBus)
                    .icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 60,120))));
            hashMapMarker.put(nomorBus,marker);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        map = googleMap;
        List<Halte> listHalte = db.allHaltes();

        if (listHalte != null) {
            Log.e("data db halte", listHalte.toString());
                initMarker(listHalte);
        }else{
            getAllHalte();
        }


        //map ke lokasi terkini
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGPS();
        }
        else
        {
            getCurrentPosisi();
        }

//        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                getDataPosisiBus(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("error", databaseError.toString());
//
//            }
//        });

        dataMarker();


    }


    public void getDataAksiHapus(DataSnapshot dataSnapshot){
        String nomorBus = dataSnapshot.getKey().toString();
        marker = (Marker) hashMapMarker.get(nomorBus);
        if(marker != null){
            marker.remove();
            hashMapMarker.remove(nomorBus);
        }
    }

    public void getDataAksi(DataSnapshot dataSnapshot){
        LatLng point = null;
        BitmapDrawable bitmapdraw;

        String lat = dataSnapshot.child("lat").getValue().toString();
        String lng = dataSnapshot.child("lng").getValue().toString();
        String nomorBus = dataSnapshot.getKey().toString();

        if(nomorBus.equals(no_bus)){
            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.transsupir);

        }else{
            bitmapdraw= (BitmapDrawable) getResources().getDrawable(R.drawable.trans);
        }
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

    public void dataMarker(){
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(MonitoringPosisi.this , "ada bus yang baru berkendara", Toast.LENGTH_SHORT).show();
                getDataAksi(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getDataAksi(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(MonitoringPosisi.this , "ada bus selesai berkendara", Toast.LENGTH_SHORT).show();
                getDataAksiHapus(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getAllHalte() {

        final ProgressDialog dialog = new ProgressDialog(MonitoringPosisi.this);
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
                        Toast.makeText(MonitoringPosisi.this , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MonitoringPosisi.this , "Data Halte berhasil diload", Toast.LENGTH_SHORT).show();
                        initMarker(listHalteItem);
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<Halte>> call, Throwable t) {
                    Toast.makeText(MonitoringPosisi.this , t.toString(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }else{
            Toast.makeText(MonitoringPosisi.this , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private void initMarker(List<Halte> listData){

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.halte);
        for (int i=0; i<listData.size(); i++){
            LatLng location = new LatLng(Double.parseDouble(listData.get(i).getLat()), Double.parseDouble(listData.get(i).getLng()));
            map.addMarker(new MarkerOptions().position(location).title(listData.get(i).getNama()).icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 100,80)))).showInfoWindow();
        }
    }

    public Bitmap getIcon (BitmapDrawable bitmapdraw, int tinggi, int lebar){
        int height = tinggi;
        int width = lebar;
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap Icon = Bitmap.createScaledBitmap(b, width, height, false);
        return Icon;
    }

    public void checkpoint(String no_bus, String tgl){
        if(konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<Rit> call = client.updateRit(no_bus, tgl);
            call.enqueue(new Callback<Rit>() {
                @Override
                public void onResponse(Call<Rit> call, Response<Rit> response) {
                    Rit rit = response.body();
                    Log.e("rit", response.body().toString());
                    namaJalur.setText(rit.getDetailTrayeks().getJalurs().getNamaJalur());
                    Toast.makeText(MonitoringPosisi.this, "Checkpoint berhasil disimpan", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Rit> call, Throwable t) {
                    Toast.makeText(MonitoringPosisi.this, "Checkpoint gagal disimpan", Toast.LENGTH_LONG).show();

                }
            });

        }else{
            Toast.makeText(MonitoringPosisi.this , "Hidupkan Koneksi Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private Runnable runTrack = new Runnable() {
        @Override
        public void run() {
            if(running){
                locationManager = (LocationManager) MonitoringPosisi.this.getSystemService(Context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    buildAlertMessageNoGPS();
                }
                else
                {
                    getCurrentPosisi();
                    getJarak(currentPosisi, currentLocation);
                }

                handler.postDelayed(this, 10000);
            }
        }
    };

//    public void getBusSearah


    public void cekJarak(final Location lokasiBus, final String no_bus){

        Call<String> call2 = client.getCurrentJalur(no_bus,tgl);
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call2, Response<String> response) {
                String jalurGet = response.body();
                Log.e("cek cekJrak", jalurGet.toString());
                if(jalurGet != null){
                    listBusSearah = new ArrayList<>();
                    Call<List<noBus>> call = client.getBusSearah(jalurGet);
                    call.enqueue(new Callback<List<noBus>>() {
                        @Override
                        public void onResponse(Call<List<noBus>> call, Response<List<noBus>> response) {
                            List<noBus> result = response.body();
                            if(result != null){
                                for(noBus item : result){
                                    listBusSearah.add(new noBus(item.getNo_bus()));
                                    Log.e("listBus cek item", listBusSearah.toString());
                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            ArrayList<String> posisiDestination = new ArrayList<>();
                                            ArrayList<Posisi> posisiBus = new ArrayList<>();

                                            String posisi1 = lokasiBus.getLatitude() + "," + lokasiBus.getLongitude();
                                            int n=0;
                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                for(noBus busSearah : listBusSearah){
                                                    if(child.getKey().equals(busSearah.getNo_bus()) && (!child.getKey().equals(no_bus))){
                                                        double location_lat = Double.parseDouble(child.child("lat").getValue().toString());
                                                        double location_lng = Double.parseDouble(child.child("lng").getValue().toString());

                                                        String no_bus2 = child.getKey().toString();
                                                        Posisi dataBus = new Posisi(location_lat,location_lng,no_bus2);
                                                        posisiBus.add(n,dataBus);

                                                        Location lokasi2 = new Location("posisi 2");
                                                        lokasi2.setLatitude(location_lat);
                                                        lokasi2.setLongitude(location_lng);
                                                        String posisi2 = lokasi2.getLatitude() + "," + lokasi2.getLongitude();
                                                        posisiDestination.add(posisi2);
                                                    }
                                                }
                                            }
                                            Log.e("posisiDestination",posisiDestination.toString());
                                            if(posisiDestination.size()>0){
                                                Log.e("posisiString",convertToString(posisiDestination));
                                                if(alertDialog != null){
                                                    alertDialog.dismiss();
                                                }
                                                actionRoute(posisi1, convertToString(posisiDestination),posisiBus );
                                            }

                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lokasiBus.getLatitude(), lokasiBus.getLongitude()), 16.0f));
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
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("error jalur", t.toString());
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

    private void showDialog(String noBus){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MonitoringPosisi.this);
        final MediaPlayer mediaPlayer = MediaPlayer.create(MonitoringPosisi.this,R.raw.alert_tone);
        mediaPlayer.start();

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Jarak Anda terlalu dekat dengan Bus " + noBus)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mediaPlayer.stop();
                    }
                });

        // membuat alert dialog dari builder
        alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }


    protected void buildAlertMessageNoGPS(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MonitoringPosisi.this);
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

    public void getCurrentPosisi(){

        if((ActivityCompat.checkSelfPermission(MonitoringPosisi.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )
                &&
                (ActivityCompat.checkSelfPermission(MonitoringPosisi.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions((Activity) MonitoringPosisi.this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            Toast.makeText(MonitoringPosisi.this,"Aktifkan GPS anda", Toast.LENGTH_SHORT).show();
        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.e("currentPosisi", String.valueOf(location.getLatitude()+ " - "+ location.getLongitude()));
            if (location != null) {
                Double lat = location.getLatitude();
                Double lng = location.getLongitude();
                currentPosisi = new Posisi(lat,lng, no_bus);
                currentLocation = location;
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentPosisi.getLat(), currentPosisi.getLng()), 16.0f));
            }
        }

    }

    public void getJarak(Posisi posisi, Location location){
        if(posisi == null){
            Toast.makeText(MonitoringPosisi.this,"Ada KESALAHAN. LOKASINYA TIDAK TAMPIL", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.e("posisi_update lokasi", String.valueOf(posisi.getLat()+ " - "+ posisi.getLng()));
            mDatabase.child(no_bus).setValue(posisi);
            Log.e("posisi_update", posisi.toString());
            Toast.makeText(MonitoringPosisi.this, "Update Posisi", Toast.LENGTH_LONG).show();
            cekJarak(location, no_bus);
        }
    }

    private void actionRoute(String posisi1 , String posisi2, final ArrayList<Posisi> busPosisi) {
            Call<DistanceMatrix> call = distanceApi.getDistanceInfo(posisi1,posisi2,"AIzaSyDZ-N9it_JFpboG3R3LfxakMiAkUdF12bU");
            call.enqueue(new Callback<DistanceMatrix>() {
            @Override
            public void onResponse(Call<DistanceMatrix> call, Response<DistanceMatrix> response) {
                DistanceMatrix data = response.body();
               if(data.getRows().size() != 0){
                    List<ElementsItem> row = data.getRows().get(0).getElements();
                    double jarak=0, jarakMin=0;
                   int n=0;
                   Posisi bus;
                   String listBusDekat="";
                    for(ElementsItem item : row){
                        try{
                            jarak = Double.parseDouble(String.valueOf(item.getDistance().getValue()));
                            Log.e("jarak direction", String.valueOf(jarak));
                            if(jarak  < 500){
                                bus = busPosisi.get(n);
                                listBusDekat = listBusDekat +" - "+busPosisi.get(n).getNo_bus();
                                if(alertDialog != null){
                                    alertDialog.dismiss();
                                }
                                marker = (Marker) hashMapMarker.get(bus.getNo_bus());
                                marker.showInfoWindow();
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bus.getLat(), bus.getLng()), 16.0f));

                        }
                            n++;
                        }catch (Exception ex){
                            Log.e("error jarak", ex.toString());
                            Toast.makeText(MonitoringPosisi.this, "Ada yang error" , Toast.LENGTH_LONG).show();
                        }

                    }

                    if(!listBusDekat.equals("")){
                        showDialog(listBusDekat);
                    }
                }
            }

            @Override
            public void onFailure(Call<DistanceMatrix> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        JadwalDetail jadwalSupir =   getIntent().getExtras().getParcelable("jadwal");
        switch(v.getId()){
            case R.id.btnCheckpoint:
                checkpoint(no_bus, tgl);
                break;
            case R.id.btnDetail:

                Intent intent =  new Intent(MonitoringPosisi.this, DetailTransActivity.class);
                intent.putExtra("jadwal", jadwalSupir);
                startActivity(intent);
                break;
            case R.id.btnUbah:
                Intent intentUbah =  new Intent(MonitoringPosisi.this, UbahTransActivity.class);
                intentUbah.putExtra("jadwal", jadwalSupir);
                startActivity(intentUbah);
                break;
            case R.id.btn_selesai:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder
                        .setMessage("Apakah Anda sudah selesai ?")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                runTrackMati();


                            }
                        }).setNegativeButton("Batal", null);

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
    }

    public void runTrackMati(){
        running = false;
        handler.removeCallbacks(runTrack);
        btnCheck.setEnabled(!btnCheck.isEnabled());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                     if(data.getKey().equals(no_bus)) {
                         data.getRef().removeValue();
                     }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Intent intent = new Intent(MonitoringPosisi.this, MainActivity.class);
        intent.putExtra("jadwal", jadwal);
        startActivity(intent);

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
        String message = "";
        LocationManager mlocManager = null;
        LocationListener mlocListener;
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new loclistener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            latitude = loclistener.latitude;
            longitude = loclistener.longitude;

            Toast.makeText(getApplicationContext(), latitude.toString(), Toast.LENGTH_LONG).show();
            if (latitude == 0.0) {
                Toast.makeText(getApplicationContext(), "Currently gps has not found your location....", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "GPS is currently off...", Toast.LENGTH_LONG).show();
        }
    }
}
