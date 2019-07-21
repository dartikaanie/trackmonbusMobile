package com.anie.dara.trackmonbus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.adapter.pesanAdapter;
import com.anie.dara.trackmonbus.model.Pesan;
import com.anie.dara.trackmonbus.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesanActivity extends AppCompatActivity implements com.anie.dara.trackmonbus.adapter.pesanAdapter.OnItemClicked {


    static RecyclerView rvDaftarPesan;
    static pesanAdapter pesanAdapter;
    static ProgressBar waiting;
    static TextView load;
    dbClient client;
    static Pesan dataPesan = new Pesan();
    ArrayList<Pesan> pesanList;
    static Activity activity;
    SwipeRefreshLayout swLayout;
    LinearLayout llayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);
        //toolbar
        Toolbar ToolBarAtas = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(ToolBarAtas);
        getSupportActionBar().setTitle("TRANS PADANG");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                startActivity(new Intent(PesanActivity.this, PesanFormActivity.class));
            }
        });



        waiting= findViewById(R.id.loadDataTrayek);
        load = findViewById(R.id.memuat_data);

        pesanAdapter = new pesanAdapter();
        pesanAdapter.setClickHandler(this);

        rvDaftarPesan = findViewById(R.id.rvListPesan);
        rvDaftarPesan.setAdapter(pesanAdapter);
        RecyclerView.LayoutManager layoutManager;

        rvDaftarPesan.setVisibility(View.INVISIBLE);
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);

        layoutManager = new LinearLayoutManager(this);

        rvDaftarPesan.setLayoutManager(layoutManager);
        rvDaftarPesan.setHasFixedSize(true);


        swLayout = (SwipeRefreshLayout) findViewById(R.id.swlayout);
        llayout = (LinearLayout) findViewById(R.id.ll_swiperefresh);

        swLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);

        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Handler untuk menjalankan jeda selama 5 detik
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        // Berhenti berputar/refreshing
                        swLayout.setRefreshing(false);

                        // fungsi-fungsi lain yang dijalankan saat refresh berhenti
                        getAllPesan();
                    }
                }, 5000);
            }
        });


        getAllPesan();

    }

    private void getAllPesan() {

        load.setText("Memuat Data");
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);
        rvDaftarPesan.setVisibility(View.INVISIBLE);


        if(konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<List<Pesan>> call = client.getAllPesan();
            call.enqueue(new Callback<List<Pesan>>() {
                @Override
                public void onResponse(Call<List<Pesan>> call,  Response<List<Pesan>> response) {
                    waiting.setVisibility(View.INVISIBLE);
                    load.setVisibility(View.INVISIBLE);
                    rvDaftarPesan.setVisibility(View.VISIBLE);


                    List<Pesan> listPesanItem = response.body();
                    if(listPesanItem == null){
                        Toast.makeText(PesanActivity.this , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(PesanActivity.this , "Pesan berhasil diload", Toast.LENGTH_SHORT).show();
                        pesanAdapter.setDataPesan(new ArrayList<Pesan>(listPesanItem));
                    }

                }

                @Override
                public void onFailure(Call<List<Pesan>> call, Throwable t) {
                    load.setText(t.toString());
                    Toast.makeText(PesanActivity.this , t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(PesanActivity.this , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void ItemClicked(Pesan pesan) {
        Toast.makeText(PesanActivity.this, "Item yang diklik adalah : " + pesan.getKeluhan_id(), Toast.LENGTH_SHORT).show();
        Intent detailIntent = new Intent(PesanActivity.this, DetailPesanActivity.class);
        detailIntent.putExtra("key_pesan_parcelable", pesan);
        startActivity(detailIntent);
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
