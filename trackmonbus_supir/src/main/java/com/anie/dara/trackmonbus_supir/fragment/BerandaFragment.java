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
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus_supir.MainActivity;
import com.anie.dara.trackmonbus_supir.MonitoringPosisi;
import com.anie.dara.trackmonbus_supir.R;
import com.anie.dara.trackmonbus_supir.SQLiteDatabaseHandler;
import com.anie.dara.trackmonbus_supir.model.Halte;
import com.anie.dara.trackmonbus_supir.model.Jadwal;
import com.anie.dara.trackmonbus_supir.rest.ApiClient;
import com.anie.dara.trackmonbus_supir.rest.dbClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
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
    DatabaseReference mDatabase;
    HashMap hashMapMarker = new HashMap<>();
    Marker marker;
    Button btnCheckin;
    static Activity activity;
    Jadwal jadwalSupir;
    Double km_awal = null;
    CardView cardview;
    List<Halte> listHalteItem;
    String jam_awal ="00:00:00" ,jam_akhir = "00:00:00", user_id;

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

        cekJadwalKerja();

               return mView;
    }



    private void cekJadwalKerja() {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Memuat Data. . .");
        dialog.show();
        Call<Jadwal> call = client.cekJadwal(user_id);
        call.enqueue(new Callback<Jadwal>() {
            @Override
            public void onResponse(Call<Jadwal> call, Response<Jadwal> response) {
                Jadwal jadwal = response.body();
                if(jadwal!=null){
                    cvChechkin.setVisibility(View.VISIBLE);
                    noBus.setText(jadwal.getNo_bus());
                    no_tnkb.setText(jadwal.getNo_tnkb());
                    kapasitas.setText(jadwal.getKapasitas());
                    namaJalur.setText(jadwal.getJalur());
                    namaTrayek.setText(jadwal.getTrayek());
                    km_awal = Double.valueOf(jadwal.getKm_awal());
                    namaSupir.setText(jadwal.getNama_supir() + " - "+ jadwal.getNama_pramugara());

                    jam_awal = jadwal.getJam_awal();
                    jam_akhir = jadwal.getJam_akhir();
                    jadwalSupir = jadwal;
                    cvTidakJadwal.setVisibility(View.INVISIBLE);

                }else{
                    cvChechkin.setVisibility(View.INVISIBLE);
                    cvTidakJadwal.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(),"Tidak Ada jadwal", Toast.LENGTH_SHORT).show();
                }
                Log.e("jadwal", String.valueOf(user_id));
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Jadwal> call, Throwable t) {
                cvChechkin.setVisibility(View.INVISIBLE);
                cvTidakJadwal.setVisibility(View.VISIBLE);
                dialog.dismiss();
                Toast.makeText(getContext()," Tidak ada Jadwal", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext() , "ada bus yang baru berkendara", Toast.LENGTH_SHORT).show();
                getDataAksi(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
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

    public void getDataAksi(DataSnapshot dataSnapshot){
        LatLng point = null;
        BitmapDrawable bitmapdraw= (BitmapDrawable) this.getResources().getDrawable(R.drawable.trans);

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

        if(map != null){
            marker = map.addMarker(new MarkerOptions()
                    .position(point)
                    .title(nomorBus)
                    .icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 60,120))));
            hashMapMarker.put(nomorBus,marker);
        }

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getAllHalte();
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

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGPS();
        }
        else
        {
            getLocation();
        }

        dataMarker();

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

            marker = map.addMarker(new MarkerOptions()
                    .position(posisi)
                    .title(nomorBus)
                    .icon(BitmapDescriptorFactory.fromBitmap(getIcon(bitmapdraw, 60,120))));
            hashMapMarker.put(nomorBus,marker);
        }
    }

    public void getLocation(){
        if((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )
                &&
                (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions((Activity) getContext(), new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
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
                    Toast.makeText(getContext() , t.toString(), Toast.LENGTH_SHORT).show();

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

                int hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
                int minute = now.get(Calendar.MINUTE);

                Date date = parseDate(hour + ":" + minute);
                Date jamAwal = parseDate(jadwalSupir.getJam_awal());
                Date jamAkhir = parseDate(jadwalSupir.getJam_akhir());

                Log.e("cek", String.valueOf(date.after( jamAwal )));
                Log.e("cek", String.valueOf( jadwalSupir.getJam_akhir()));
                Log.e("cek", String.valueOf( date));
                Log.e("cek", String.valueOf( date.before(jamAkhir)));

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
                        msg = "Shift Anda dimulai pada pukul "+ jadwalSupir.getJam_awal()+". Silakan Checkin lagi nanti ya.";
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


}
