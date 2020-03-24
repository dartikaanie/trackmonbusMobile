package com.anie.dara.trackmonbus_supir;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
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
import com.anie.dara.trackmonbus_supir.model.LogPotition;
import com.anie.dara.trackmonbus_supir.model.Posisi;
import com.anie.dara.trackmonbus_supir.model.PosisiTime;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
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

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    JadwalDetail jadwal;
    TextView NoBus, no_tnkb, kapasitas, namaJalur, namaSupir, namaTrayek, hari_tgl, namaPramugara;
    Button btnCheck, btnDetail, btnCekHalte, btnSelesai;
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
    PosisiTime posisiAwal = null, startStopTime = null,endStopTime;
    AlertDialog alertDialog = null;
    private static  final  int REQUEST_LOCATION =1;
    private SQLiteDatabaseHandler db;

    private Handler handler = new Handler();
    private int mInterval = 1000;
    public boolean running = true;
    Toolbar toolbar;
    ImageView toolbarTitle;
    String tgl_skrg;
    public static  final String DEFAULT ="tidak aktif";
    int DEFAULT_NUM_RIT = 0, DEFAULT_STATE_RIT = 1, STATE_DEPARTURE = 2, STATE_ARRIVE = 1;
    String currentJalur;
    int currNoRit = DEFAULT_NUM_RIT, stateOfRit = DEFAULT_STATE_RIT;
    private LocationManager locationManager;

    String[] praListJalur = null;
    ArrayList<String> listJalur;
    boolean first= true;
    int iteratorRitJalur;

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
        btnCekHalte = findViewById(R.id.btnCekHalte);
        btnCekHalte.setOnClickListener(this);
        btnSelesai = findViewById(R.id.btn_selesai);
        btnSelesai.setOnClickListener(this);

        etKmAwal = findViewById(R.id.etKmAwal);
        db = new SQLiteDatabaseHandler(this);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tgl_skrg = dateFormat.format(date);
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
            String s = jadwal.getJalur();
            praListJalur = s.split("-");
            listJalur = new ArrayList<>();
            for (String ss : praListJalur){
                listJalur.add(ss);
            }
            iteratorRitJalur = listJalur.size();
        }
        //cek bus line
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        currentJalur = sharedPreferences.getString("jalur_id", DEFAULT);
        if(currentJalur.equals(DEFAULT)){
            currentJalur = jadwal.getJadwal().getJalurAwalId();
//            // save bus line
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("jalur_id", jadwal.getJadwal().getJalurAwalId());
//            editor.commit();
        }


        mMapView = (MapView) findViewById(R.id.map_monitor);
//        dataMarker();

        if(mMapView !=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        Date tgl = new Date();
        DateFormat detik = new SimpleDateFormat("ss");
        DateFormat miliDetik = new SimpleDateFormat("SSS");
        String time =  detik.format(tgl);
        int mili =  Integer.valueOf(miliDetik.format(tgl));
        int detikSekarang = Integer.valueOf(time);
        if( detikSekarang%10 != 0){
            mInterval = (10 - detikSekarang%10)*1000;
        }else{
            mInterval =0;
        }
        handler.postDelayed(runTrack, mInterval);
        getLocation();

        getCurrentRit();




    }


    public void getCurrentRit(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot datachild : dataSnapshot.getChildren()) {
                //cek is it the same number
                if((datachild.getKey().equals(jadwal.getNoBus()))){
                    //cek nomor RIT saat ini
                    for (DataSnapshot dataRit : datachild.child("RIT").getChildren()) {
                     currNoRit = Integer.parseInt(dataRit.getKey());

                    }

                    Log.e("curr no rit", String.valueOf(currNoRit));
                    //cek Jalur saat ini
                    if(currNoRit!=DEFAULT_NUM_RIT){
                        iteratorRitJalur = listJalur.size();
                        for (DataSnapshot dataChildRit : datachild.child("RIT").child(String.valueOf(currNoRit)).getChildren()) {
                            currentJalur = dataChildRit.getKey();
                            if(dataChildRit.child("waktuDatang").getValue() == null){
                                stateOfRit = STATE_DEPARTURE;
                            }else{
                                stateOfRit = STATE_ARRIVE;
                            }
                            iteratorRitJalur--;
                        }
                        //cek status rit
                         //DEFAULT_STATE_RIT == 0
                        //Set nomor Rit saat ini
                        first = false;
                    }else{
                        currNoRit =1;
                        Log.e("cuurRit get Data", String.valueOf(currNoRit));
                    }

                }
            }
            Log.e("curr stateOfRit", String.valueOf(stateOfRit));

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("error", databaseError.toString());

        }
        });
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
       googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));
        map = googleMap;
        List<Halte> listHalte = db.allHaltes();

        if (listHalte != null) {
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

    public void getDataAksi(DataSnapshot dataSnapshot) {
        LatLng point = null;
        BitmapDrawable bitmapdraw;
        if(dataSnapshot.getKey() == no_bus){
             bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.transsupir);
        }else{
             bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.trans);
        }
         for (DataSnapshot data : dataSnapshot.child("log").getChildren()) {
//             if(!data.getKey().equals("status")) {
                String lat = data.child("lat").getValue().toString();
                String lng = data.child("lng").getValue().toString();
                String nomorBus = dataSnapshot.getKey().toString();

                double location_lat = Double.parseDouble(lat);
                double location_lng = Double.parseDouble(lng);
                point = new LatLng(location_lat, location_lng);
                marker = (Marker) hashMapMarker.get(nomorBus);
                if (marker != null) {
                    marker.remove();
                    hashMapMarker.remove(nomorBus);
                }
                if (map != null) {
                    marker = map.addMarker(new MarkerOptions()
                            .position(point)
                            .title(nomorBus)
                            .icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 60, 120))));
                    hashMapMarker.put(nomorBus, marker);
                }
//            }

        }
    }

    public void dataMarker(){
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(MonitoringPosisi.this , "ada bus yang baru berkendara", Toast.LENGTH_SHORT).show();
                Log.e("bus baru", dataSnapshot.getValue().toString());
                getDataAksi(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getDataAksi(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(MonitoringPosisi.this , "ada bus selesai berkendara", Toast.LENGTH_SHORT).show();
//                getDataAksiHapus(dataSnapshot);
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

    public void checkpointRIT (String no_bus){
        if(konekkah()){
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String timeNow =  dateFormat.format(date);

            SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//            currNoRit = sharedPreferences.getInt("numberOfRit", DEFAULT_NUM_RIT);
//            stateOfRit = sharedPreferences.getInt("stateOfRit", DEFAULT_STATE_RIT);

            //kondisikan untuk menambah waktu datang / waktu berangkat
            if((stateOfRit == DEFAULT_STATE_RIT) || (stateOfRit == STATE_ARRIVE)){ //berangkat
                Rit addRit = new Rit();
                addRit.setWaktuBerangkat(timeNow);
                addRit.setWaktuDatang(null);
                //change jalur
                if(first){
                    currentJalur = jadwal.getJadwal().getJalurAwalId();
                    first = false;
                }else{
                    //get next jalur
                    String tempJalur = null;
                    boolean cekJalur = false;
                    for ( String ss: listJalur) {
                        if(cekJalur){
                            tempJalur = ss;
                            cekJalur = false;
                        }

                        if(ss.equals(currentJalur)){
                            cekJalur = true;
                        }
                    }

                    if(tempJalur != null){
                       currentJalur = tempJalur;
                    }else{
                      //list awal
                      currentJalur = listJalur.get(0);
                    }
                 }

                //get no rit
                Log.e("iteratorRitJalur", String.valueOf(iteratorRitJalur));
                Log.e("cek iteratorRitJalur", String.valueOf(iteratorRitJalur<listJalur.size()));

                if(iteratorRitJalur!=0){
                    iteratorRitJalur--;
                }else{
                    currNoRit++;
                    iteratorRitJalur =listJalur.size()-1;
                }

                //cek jalur has been crossed
                Log.e("stateOfRIt", String.valueOf(stateOfRit));
                Log.e("currentJalur", String.valueOf(currentJalur));
                Log.e("currNoRit", String.valueOf(currNoRit));
                Log.e("iteratorRitJalur", String.valueOf(iteratorRitJalur));

                //get specified database part and save
                mDatabase.child(no_bus).child("RIT").child(String.valueOf(currNoRit)).child(currentJalur).setValue(addRit);
                stateOfRit = STATE_DEPARTURE;
            }else if(stateOfRit == STATE_DEPARTURE){
                //get specified database part and save
                mDatabase.child(no_bus).child("RIT").child(String.valueOf(currNoRit)).child(currentJalur).child("waktuDatang").setValue(timeNow);
                stateOfRit = STATE_ARRIVE;
            }

            // save number and state of rit
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("numberOfRit", currNoRit).commit();
            editor.putInt("stateOfRit", stateOfRit).commit();
            editor.putString("jalur_id", currentJalur).commit();
            editor.commit();
        }
    }
//    public void checkpoint(String no_bus, String tgl){
//        if(konekkah()){
//            client = ApiClient.getClient().create(dbClient.class);
//            Call<Rit> call = client.updateRit(no_bus, tgl);
//            call.enqueue(new Callback<Rit>() {
//                @Override
//                public void onResponse(Call<Rit> call, Response<Rit> response) {
//                    Rit rit = response.body();
//                    namaJalur.setText(rit.getDetailTrayeks().getJalurs().getNamaJalur());
//                    Toast.makeText(MonitoringPosisi.this, "Checkpoint berhasil disimpan", Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onFailure(Call<Rit> call, Throwable t) {
//                    Toast.makeText(MonitoringPosisi.this, "Checkpoint gagal disimpan", Toast.LENGTH_LONG).show();
//
//                }
//            });
//
//        }else{
//            Toast.makeText(MonitoringPosisi.this , "Hidupkan Koneksi Internet", Toast.LENGTH_SHORT).show();
//        }
//
//    }

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
                    Date tgl = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    String time =  dateFormat.format(tgl);
                    DateFormat detik = new SimpleDateFormat("ss");

                    int detikSekarang = Integer.valueOf(detik.format(tgl));
                    if( detikSekarang%10 != 0){
                        mInterval = (10 - detikSekarang%10)*1000;
                        time = time.substring(0,7) +"0";

                    }else{
                        mInterval =9000;
                    }
                    getCurrentPosisi();
                    if(posisiAwal == null){
                        posisiAwal = new PosisiTime(currentPosisi.getLat(), currentPosisi.getLng(), time);
                        startStopTime =  new PosisiTime(currentPosisi.getLat(), currentPosisi.getLng(), time);
                        endStopTime =  new PosisiTime(currentPosisi.getLat(), currentPosisi.getLng(), time);

                    }else{
                        //cek if current bus stop
                        if(posisiAwal.cek(currentPosisi.getLat(), currentPosisi.getLng())) {
                            cekBerhenti(posisiAwal, currentPosisi, time);
                        }else{
                            startStopTime = null;
                            posisiAwal = null;
                        }
                    }

                    addPosisi(currentPosisi, currentLocation, time);
                }
//                Log.e("interval", String.valueOf(mInterval));
                mDatabase.child(no_bus).child("status").setValue(1);
                handler.postDelayed(this, mInterval);
            }
        }
    };

//    public void getBusSearah

    public void cekBerhenti(PosisiTime awal, Posisi current, String time){
        awal.setLat(current.getLat());
        awal.setLng(current.getLng());
        awal.setTime(time);
        endStopTime = awal;
        Toast.makeText(MonitoringPosisi.this, "Bus Berhenti", Toast.LENGTH_LONG).show();

        //Give action if bus stop

    }

    public void getListBusInLine(final Location lokasiBus, final String no_bus){
        //get bus sejalur
        listBusSearah = new ArrayList<>();
        final ArrayList<String> posisiDestination = new ArrayList<>();
        final ArrayList<Posisi> posisiBus = new ArrayList<>();
        final String posisi1 = lokasiBus.getLatitude() + "," + lokasiBus.getLongitude(); //potiton of current bus
        final int n=0;
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datachild : dataSnapshot.getChildren()) {
                    //cek is it the same line and status active (1)
                    if((datachild.child("jalur").getValue().toString().equals(currentJalur))
                            &&  (datachild.child("status").getValue().toString().equals("1"))
                            &&  (!datachild.getKey().equals(jadwal.getNoBus()))
                      ){
                        listBusSearah.add(new noBus(datachild.getKey()));
                        double location_lat = 0, location_lng = 0;

                        //get potition of other bus
                        for (DataSnapshot child : datachild.child("log").getChildren()) {
                            location_lat = Double.parseDouble(child.child("lat").getValue().toString());
                            location_lng = Double.parseDouble(child.child("lng").getValue().toString());
                        }

                        //if potition exist
                        if((location_lat!=0) && (location_lng!=0) ) {
                            String no_bus2 = datachild.getKey();
                            Posisi dataOtherBus = new Posisi(location_lat, location_lng, no_bus2);
                            posisiBus.add(n, dataOtherBus);

                            //made matrix from potition of buses
                            Location lokasi2 = new Location("posisi 2");
                            lokasi2.setLatitude(location_lat);
                            lokasi2.setLongitude(location_lng);
                            String posisi2 = lokasi2.getLatitude() + "," + lokasi2.getLongitude();
                            posisiDestination.add(posisi2);
                        }
                    }
                }

                //if data exist
                if(posisiDestination.size()>0){
                    if(alertDialog != null){
                        alertDialog.dismiss();
                    }
                    calculateDistance(posisi1, convertToString(posisiDestination),posisiBus );
                }

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lokasiBus.getLatitude(), lokasiBus.getLongitude()), 16.0f));

                Log.e("listbus",listBusSearah.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    public void getListBusInLine2(final Location lokasiBus, final String no_bus){
//        Call<String> call2 = client.getCurrentJalur(no_bus,tgl);
//        call2.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call2, Response<String> response) {
//                String jalurGet = response.body();
//                if(jalurGet != null){
//                    listBusSearah = new ArrayList<>();
//                    Call<List<noBus>> call = client.getBusSearah(jalurGet);
//                    call.enqueue(new Callback<List<noBus>>() {
//                        @Override
//                        public void onResponse(Call<List<noBus>> call, Response<List<noBus>> response) {
//                            List<noBus> result = response.body();
//                            if(result != null){
//                                for(noBus item : result){
//                                    listBusSearah.add(new noBus(item.getNo_bus()));
//                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            ArrayList<String> posisiDestination = new ArrayList<>();
//                                            ArrayList<Posisi> posisiBus = new ArrayList<>();
//                                            String posisi1 = lokasiBus.getLatitude() + "," + lokasiBus.getLongitude();
//                                            int n=0;
//                                            for (DataSnapshot datachild : dataSnapshot.getChildren()) {
//                                                for(noBus busSearah : listBusSearah){
//                                                    if(datachild.getKey().equals(busSearah.getNo_bus()) && (!datachild.getKey().equals(no_bus)) && (datachild.child("status").getValue().toString().equals(0))) {
//                                                        double location_lat = 0, location_lng = 0;
//                                                        for (DataSnapshot child : datachild.child("log").getChildren()) {
//                                                            location_lat = Double.parseDouble(child.child("lat").getValue().toString());
//                                                            location_lng = Double.parseDouble(child.child("lng").getValue().toString());
//                                                        }
//                                                        if((location_lat!=0) && (location_lng!=0) ) {
//                                                            String no_bus2 = datachild.getKey().toString();
//                                                            Posisi dataBus = new Posisi(location_lat, location_lng, no_bus2);
//                                                            posisiBus.add(n, dataBus);
//
//                                                            Location lokasi2 = new Location("posisi 2");
//                                                            lokasi2.setLatitude(location_lat);
//                                                            lokasi2.setLongitude(location_lng);
//                                                            String posisi2 = lokasi2.getLatitude() + "," + lokasi2.getLongitude();
//                                                            posisiDestination.add(posisi2);
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                           if(posisiDestination.size()>0){
//                                               if(alertDialog != null){
//                                                    alertDialog.dismiss();
//                                                }
//                                                actionRoute(posisi1, convertToString(posisiDestination),posisiBus );
//                                            }
//
//                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lokasiBus.getLatitude(), lokasiBus.getLongitude()), 16.0f));
//                                        }
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                }
//                            }else{
//                                Log.e("listBus", "DAta tidak ada");
//                            }
//                        }
//                        @Override
//                        public void onFailure(Call<List<noBus>> call, Throwable t) {
//                            Log.e("error listBus", t.toString());
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.e("error jalur", t.toString());
//            }
//        });
//    }

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
//            Log.e("currentPosisi", String.valueOf(location.getLatitude()+ " - "+ location.getLongitude()));
            if (location != null) {
                Double lat = location.getLatitude();
                Double lng = location.getLongitude();
                currentPosisi = new Posisi(lat,lng, no_bus);
                currentLocation = location;
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentPosisi.getLat(), currentPosisi.getLng()), 16.0f));
            }else{
                Toast.makeText(MonitoringPosisi.this,"Lokasi tidak tersimpan", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void addPosisi(final Posisi posisi, Location location, final String time){
        final String useTime = time;
        if(posisi == null){
            Toast.makeText(MonitoringPosisi.this,"Ada KESALAHAN. LOKASINYA TIDAK TAMPIL", Toast.LENGTH_SHORT).show();
        }
        else{
            //get bus serah utk menhitung jarak
            getListBusInLine(location, no_bus);
            final String posisiCek = posisi.getLng() +","+ posisi.getLat();
           //CEK if the point contain of  area
            Call<String> callContain = client.getContain(tgl.substring(0,10),no_bus,jadwal.getShiftId(),time, posisiCek,String.valueOf(currentPosisi.getLat()),String.valueOf(currentPosisi.getLng()), jadwal.getJadwal().getTrayekId());
            callContain.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String contain = response.body();
                    if(contain.equals(1)){
                        Toast.makeText(MonitoringPosisi.this, "Keluar dari Trayek", Toast.LENGTH_LONG).show();
                    }
                    LogPotition logPosisi = new LogPotition(posisi.getLat(), posisi.getLng(), contain, jadwal.getUserIdSupir() );
                    mDatabase.child(no_bus).child("log").child(useTime).setValue(logPosisi);
                    mDatabase.child(no_bus).child("status").setValue(1);
                    mDatabase.child(no_bus).child("jalur").setValue(currentJalur);
                    Toast.makeText(MonitoringPosisi.this, "Update Posisi", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("error contain",t.toString());
//                    mDatabase.child(no_bus).child("log").child(useTime).child("inArea").setValue(0);

                }
            });
        }
    }

    private void calculateDistance(String posisi1 , String posisi2, final ArrayList<Posisi> busPosisi) {
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
                   //cek jarak each other
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

                    //if there is bus near
                    if(!listBusDekat.equals("")){
                        showDialog(listBusDekat);
                    }
                }
            }

            @Override
            public void onFailure(Call<DistanceMatrix> call, Throwable t) {
                Log.e("error api dmatrix", t.toString());
            }
        });
    }

    public void checkpointHalte(){
        getCurrentPosisi();
        Date tgl2 = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time2 =  dateFormat.format(tgl2);
        Call<ResponseBody> calCheckpointHalte = client.checkpointHalte(tgl.substring(0,10),no_bus,jadwal.getShiftId(),time2, String.valueOf(currentPosisi.getLat()),String.valueOf(currentPosisi.getLng()));
        calCheckpointHalte.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MonitoringPosisi.this, "Sukses" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MonitoringPosisi.this, "Gagal" , Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        JadwalDetail jadwalSupir =   getIntent().getExtras().getParcelable("jadwal");
        switch(v.getId()){
            case R.id.btnCheckpoint:
                checkpointRIT(no_bus);
                break;
            case R.id.btnDetail:
                Intent intent =  new Intent(MonitoringPosisi.this, DetailTransActivity.class);
                intent.putExtra("jadwal", jadwalSupir);
                startActivity(intent);
                break;
            case R.id.btnCekHalte:
//                Intent intentUbah =  new Intent(MonitoringPosisi.this, UbahTransActivity.class);
//                intentUbah.putExtra("jadwal", jadwalSupir);
//                startActivity(intentUbah);
                checkpointHalte();
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
        mDatabase.child(no_bus).child("status").setValue(0);
//        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot data: dataSnapshot.getChildren()) {
//                     if(data.getKey().equals(no_bus)) {
//                         data.child("status").
////                         data.getRef().removeValue();
//                     }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createAlar(){

    }
}
