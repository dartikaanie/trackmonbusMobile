package com.anie.dara.trackmonbus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.adapter.JalurAdapter;
import com.anie.dara.trackmonbus.model.Trayeks.JalurItem;
import com.anie.dara.trackmonbus.model.Trayeks.Trayeks;
import com.anie.dara.trackmonbus.rest.ApiClient;
import com.anie.dara.trackmonbus.rest.dbClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PilihJalurActivity extends AppCompatActivity implements JalurAdapter.jalurClick {

    static RecyclerView rvDaftarJalur;
    static JalurAdapter jalurAdapter;
    static ProgressBar waiting;
    static TextView load;
    ArrayList<JalurItem> jalurItems = new ArrayList<>();
    private dbClient client = ApiClient.getClient().create(dbClient.class);
    Toolbar toolbar;
    ImageView toolbarTitle;
    private SwipeRefreshLayout swLayout;
    Trayeks trayek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_jalur);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = (ImageView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swLayout = (SwipeRefreshLayout) findViewById(R.id.swlayout);
        LinearLayout llayout = (LinearLayout) findViewById(R.id.ll_swiperefresh);

        waiting= findViewById(R.id.loadData);
        load = findViewById(R.id.memuat_data);

        jalurAdapter = new JalurAdapter(jalurItems, this);
        jalurAdapter.SetClickJalur(this);

        rvDaftarJalur = findViewById(R.id.rvListJalur);
        rvDaftarJalur.setAdapter(jalurAdapter);
        RecyclerView.LayoutManager layoutManager;

        rvDaftarJalur.setVisibility(View.INVISIBLE);
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);
        layoutManager = new LinearLayoutManager(this);
        rvDaftarJalur.setLayoutManager(layoutManager);
        rvDaftarJalur.setHasFixedSize(true);

        Intent HalteIntent = getIntent();
        if (null != HalteIntent) {
            trayek = HalteIntent.getParcelableExtra("trayek");
        }

        getAllJalur();

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
                        getAllJalur();
                    }
                }, 5000);
            }
        });
    }

    private void getAllJalur() {
        load.setText("Memuat Data");
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);
        rvDaftarJalur.setVisibility(View.INVISIBLE);

        if(konekkah()){
            Call<List<JalurItem>> call = client.getAllJalur(trayek.getTrayekId());
            call.enqueue(new Callback<List<JalurItem>>() {
                @Override
                public void onResponse(Call<List<JalurItem>> call,  Response<List<JalurItem>> response) {
                    waiting.setVisibility(View.INVISIBLE);
                    load.setVisibility(View.INVISIBLE);
                    rvDaftarJalur.setVisibility(View.VISIBLE);
                    List<JalurItem> listItem = response.body();
                    if(listItem == null){
                        Toast.makeText(PilihJalurActivity.this , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(PilihJalurActivity.this , "Data berhasil diload", Toast.LENGTH_SHORT).show();
                        jalurAdapter.setJalurList(new ArrayList<JalurItem>(listItem));
                    }

                }

                @Override
                public void onFailure(Call<List<JalurItem>> call, Throwable t) {
                    load.setText(t.toString());
                    Toast.makeText(PilihJalurActivity.this , t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(PilihJalurActivity.this , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean konekkah(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean konek = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return konek;
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void OnKlikJalur(JalurItem jalurItem) {
       if(checkLocationPermission() == false){
           Toast.makeText(PilihJalurActivity.this , "Berikan Akses Lokasi terlebih dahulu", Toast.LENGTH_SHORT).show();

           ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            Intent moveActivity = new Intent(PilihJalurActivity.this, LokasiActivity.class);
            moveActivity.putExtra("jalur", jalurItem);
            startActivity(moveActivity);
        }
    }
}
