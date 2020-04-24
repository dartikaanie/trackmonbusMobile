package com.anie.dara.trackmonbus_supir.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus_supir.MainActivity;
import com.anie.dara.trackmonbus_supir.MonitoringPosisi;
import com.anie.dara.trackmonbus_supir.R;
import com.anie.dara.trackmonbus_supir.SQLiteDatabaseHandler;
import com.anie.dara.trackmonbus_supir.loclistener;
import com.anie.dara.trackmonbus_supir.model.Halte;
import com.anie.dara.trackmonbus_supir.model.jadwal.DetailTrayeks;
import com.anie.dara.trackmonbus_supir.model.jadwal.JadwalDetail;
import com.anie.dara.trackmonbus_supir.rest.ApiClient;
import com.anie.dara.trackmonbus_supir.rest.dbClient;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class BerandaFragment extends Fragment  implements OnMapReadyCallback, View.OnClickListener {
    GoogleMap map;
    MapView mMapView;
    CardView cvChechkin, cvTidakJadwal;
    View mView;
    TextView noBus, no_tnkb, kapasitas, namaSupir, namaJalur, namaTrayek;
    static ProgressBar waiting;
    static TextView load;
    DatabaseReference mDatabase;
    HashMap hashMapMarker = new HashMap<>();
    Marker marker;
    Button btnCheckin;
    static Activity activity;
    JadwalDetail jadwalSupir;
    Double km_awal = null;
    String  user_id;
    public boolean running = false;

    private static  final  int REQUEST_LOCATION =1;
    LocationManager locationManager;

    private dbClient client = ApiClient.getClient().create(dbClient.class);
    private String DEFAULT = "tidak aktif";
    private SQLiteDatabaseHandler db;

    private Toolbar toolbar;
    private ImageView toolbarTitle;
    private Handler handler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_beranda, container, false);

        //bind view
        toolbar = mView.findViewById(R.id.toolbar);
        toolbarTitle = (ImageView) mView.findViewById(R.id.toolbar_title);
        //set toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

         //menghilangkan titlebar bawaan
        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        waiting= mView.findViewById(R.id.loadData);
        load = mView.findViewById(R.id.memuat_data);
        noBus = mView.findViewById(R.id.NoBus);
        no_tnkb = mView.findViewById(R.id.no_tnkb);
        kapasitas = mView.findViewById(R.id.kapasitas);
        namaSupir = mView.findViewById(R.id.namaSupir);
        namaJalur = mView.findViewById(R.id.namaJalur);
        namaTrayek = mView.findViewById(R.id.namaTrayek2);
//        cardview = mView.findViewById(R.id.cvTidakJadwal);

        db = new SQLiteDatabaseHandler(getContext());

        btnCheckin = mView.findViewById(R.id.btnCheckin);
        btnCheckin.setOnClickListener(this);

        cvTidakJadwal = mView.findViewById(R.id.cvTidakJadwal);
        cvTidakJadwal.setOnClickListener(this);
        cvTidakJadwal.setVisibility(View.INVISIBLE);
        cvChechkin = mView.findViewById(R.id.cardViewBus);
        cvChechkin.setVisibility(View.INVISIBLE);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tgl_skrg = dateFormat.format(date);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(tgl_skrg);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", DEFAULT);
        String status_track = sharedPreferences.getString("status_track", DEFAULT);
        mMapView = (MapView) mView.findViewById(R.id.map_supir);
        dataMarker();

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGPS();
        }else{
            if(mMapView !=null){
                mMapView.onCreate(null);
                mMapView.onResume();
                mMapView.getMapAsync(this);
            }

        }

        load.setText("Memuat Data");
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);

        cekJadwalKerja();

               return mView;
    }



    private void cekJadwalKerja() {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Memuat Data. . .");
        dialog.show();
        Call<JadwalDetail> call = client.cekJadwal(user_id);
        call.enqueue(new Callback<JadwalDetail>() {
            @Override
            public void onResponse(Call<JadwalDetail> call, Response<JadwalDetail> response) {
                JadwalDetail jadwal = response.body();
                if(jadwal!=null){
                    cvChechkin.setVisibility(View.VISIBLE);
                    noBus.setText(jadwal.getJadwal().getNoBus());
                    no_tnkb.setText(jadwal.getJadwal().getBuses().getNoTnkb());
                    kapasitas.setText(Integer.toString(jadwal.getJadwal().getBuses().getKapasitas()));
                    namaJalur.setText(jadwal.getJadwal().getDetailTrayeks().getJalurs().getNamaJalur());
                    namaTrayek.setText(jadwal.getJadwal().getDetailTrayeks().getTrayeks().getTrayek());
                    km_awal = Double.valueOf(jadwal.getJadwal().getKmAwal());
                    namaSupir.setText(jadwal.getUsers().getName() + " - "+ jadwal.getPramugaras().getNamaPramugara());
                    jadwalSupir = jadwal;
                    cvTidakJadwal.setVisibility(View.INVISIBLE);
                    Log.e("jadwal cek", jadwal.getJalur().toString());

                }else{
                    cvChechkin.setVisibility(View.INVISIBLE);
                    cvTidakJadwal.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(),"Tidak Ada jadwal", Toast.LENGTH_SHORT).show();
                }
                Log.e("jadwal", String.valueOf(user_id));
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JadwalDetail> call, Throwable t) {
                cvChechkin.setVisibility(View.INVISIBLE);
                cvTidakJadwal.setVisibility(View.VISIBLE);
                dialog.dismiss();

                waiting.setVisibility(View.INVISIBLE);
                load.setVisibility(View.INVISIBLE);
                Log.e("errorJadwal", t.toString());
                Toast.makeText(getContext()," Ada Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
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

    public void getDataAksiHapus(DataSnapshot dataSnapshot){
        String nomorBus = dataSnapshot.getKey().toString();
        marker = (Marker) hashMapMarker.get(nomorBus);
        if(marker != null){
            marker.remove();
            hashMapMarker.remove(nomorBus);
        }
    }

    public void getDataAksi(final DataSnapshot dataSnapshot){
        final BitmapDrawable bitmapdraw= (BitmapDrawable) ((MainActivity)activity).getResources().getDrawable(R.drawable.trans);
        dataSnapshot.child("log").getRef().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotChild) {
                for (DataSnapshot dataPosisi : dataSnapshotChild.getChildren()) {
                    String lat = dataPosisi.child("lat").getValue().toString();
                    String lng = dataPosisi.child("lng").getValue().toString();
                    String nomorBus = dataSnapshot.getKey().toString();

                    double location_lat = Double.parseDouble(lat);
                    double location_lng = Double.parseDouble(lng);
                    LatLng point = new LatLng(location_lat, location_lng);
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        try{
            googleMap.setMapStyle(new MapStyleOptions(getResources()
                    .getString(R.string.style_json)));
            map = googleMap;
//            getAllHalte();
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    getDataPosisiBus(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("error", databaseError.toString());

                }
            });
            try{
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    buildAlertMessageNoGPS();
                }
                else
                {  getLocation();
                    getCurrentlocation();

                }
            }catch(Exception $ex){
                Toast.makeText(getContext()," Ada Kesalahan", Toast.LENGTH_SHORT).show();
            }

            dataMarker();
            waiting.setVisibility(View.INVISIBLE);
            load.setVisibility(View.INVISIBLE);

        }catch (Exception ex){
            load.setText("Ada Kesalahan, Silakan Refresh Halaman ini");
        }

    }


    public void getDataPosisiBus(DataSnapshot dataSnapshot){

        LatLng posisi = null;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.trans);
        for (DataSnapshot noBus : dataSnapshot.getChildren()) {
                for (DataSnapshot child : noBus.child("log").getChildren()) {
                    if(!child.getKey().equals("status")) {
                        String lat = child.child("lat").getValue().toString();
                        String lng = child.child("lng").getValue().toString();
                        String nomorBus = noBus.getKey().toString();

                        double location_lat = Double.parseDouble(lat);
                        double location_lng = Double.parseDouble(lng);
                        posisi = new LatLng(location_lat, location_lng);

                        marker = (Marker) hashMapMarker.get(nomorBus);
                        if (marker != null) {
                            marker.remove();
                            hashMapMarker.remove(nomorBus);
                        }
                        if (map != null) {
                            marker = map.addMarker(new MarkerOptions()
                                    .position(posisi)
                                    .title(nomorBus)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 60, 120))));
                            hashMapMarker.put(nomorBus, marker);
                        }
                    }
                }

        }
    }

    public void getCurrentlocation(){
        if((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )
                &&
                (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions((Activity) getContext(), new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else{


            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
                Toast.makeText(getContext(),"TIDAK TAMPIL LOKASINYA", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGPS(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Hidupkan GPS Anda")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
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

    private void getAllHalte() {



        if(((MainActivity)activity).konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<List<Halte>> call = client.getAllHalte();
            call.enqueue(new Callback<List<Halte>>() {
                @Override
                public void onResponse(Call<List<Halte>> call,  Response<List<Halte>> response) {
                    List<Halte> listHalte = response.body();
                    if(listHalte == null){
                        Toast.makeText(getContext() , "Maaf, Tidak ada Halte", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        for(Halte item : listHalte){
                            db.addHalte(item);
                        }
                        Toast.makeText(getContext() , "Data Halte berhasil diload", Toast.LENGTH_SHORT).show();
                        initMarker(listHalte);
                    }

                }

                @Override
                public void onFailure(Call<List<Halte>> call, Throwable t) {
                    Toast.makeText(getContext() ,"Ada Kesalahan", Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            Toast.makeText(getContext() , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();

        }
    }

    private void initMarker(List<Halte> listData){

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.halte);
        for (int i=0; i<listData.size(); i++){
            LatLng location = new LatLng(Double.parseDouble(listData.get(i).getLat()), Double.parseDouble(listData.get(i).getLng()));
            map.addMarker(new MarkerOptions().position(location).title(listData.get(i).getNama()).icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 100,80)))).showInfoWindow();
        }
        LatLng latLng = new LatLng(Double.parseDouble(listData.get(0).getLat()), Double.parseDouble(listData.get(0).getLng()));
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude,latLng.longitude), 15.0f));

        waiting.setVisibility(View.INVISIBLE);
        load.setVisibility(View.INVISIBLE);
    }

    public Bitmap getIcon (BitmapDrawable bitmapdraw, int tinggi, int lebar){
        int height = tinggi;
        int width = lebar;
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap Icon = Bitmap.createScaledBitmap(b, width, height, false);
        return Icon;
    }

    private void showDialog(String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder
                .setMessage(msg)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cekJadwalKerja();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCheckin:
                final ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Mohon Tunggu. . .");
                dialog.show();
                Calendar now = Calendar.getInstance();
                cekJadwalKerja();
                int hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
                int minute = now.get(Calendar.MINUTE);
                Log.e("jadwal", jadwalSupir.getShifts().getJamAwal());
                Date date = parseDate(hour + ":" + minute);
                Date jamAwal = parseDate(jadwalSupir.getShifts().getJamAwal());
                Date jamAkhir = parseDate(jadwalSupir.getShifts().getJamAkhir());

                if (date.after( jamAwal ) && date.before(jamAkhir)) {
                    if(km_awal != 0){
                        Intent intent=  new Intent(getActivity(), MonitoringPosisi.class);
                        intent.putExtra("jadwal", jadwalSupir);
                        startActivity(intent);

                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("jadwal",jadwalSupir);
                        Fragment moveFragment = new CheckInAwalFragment();
                        moveFragment.setArguments(bundle);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fl_container, moveFragment)
                                .commit();

                    }
                    dialog.dismiss();
                    Toast.makeText(getContext() , "Checkin", Toast.LENGTH_SHORT).show();
                }else{

                    String msg;
                    if(date.after(jamAkhir)){
                        msg = "Shift Anda telah selesai hari ini. Silakan beristirahat";
                    }else{
                        msg = "Shift Anda dimulai pada pukul "+ jadwalSupir.getShifts().getJamAwal()+". Silakan Checkin lagi nanti ya.";
                    }
                    dialog.dismiss();
                    showDialog(msg);
                }
                break;

            case R.id.cvTidakJadwal:
                cekJadwalKerja();
                dataMarker();
                break;
        }
    }

    private Date parseDate(String date) {

        final String inputFormat = "HH:mm";
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }



    public void getLocation(){
        Double latitude = 0.0, longitude;
        String message = "";
        LocationManager mlocManager = null;
        LocationListener mlocListener;
        mlocManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new loclistener(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 16.0f));
//            Toast.makeText(getContext().getApplicationContext(), latitude.toString(), Toast.LENGTH_LONG).show();
            if (latitude == 0.0) {
                Toast.makeText(getContext().getApplicationContext(), "Currently gps has not found your location....", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getContext().getApplicationContext(), "GPS is currently off...", Toast.LENGTH_LONG).show();
        }
    }
}
