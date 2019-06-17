package com.anie.dara.trackmonbus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PesanActivity extends AppCompatActivity implements com.anie.dara.trackmonbus.adapter.pesanAdapter.OnItemClicked {


    static RecyclerView rvDaftarPesan;
    static pesanAdapter pesanAdapter;
    static ProgressBar waiting;
    static TextView load;
    dbClient client;
    static Pesan dataPesan = new Pesan();
    ArrayList<Pesan> pesanList;
    static Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);
        //toolbar
        Toolbar ToolBarAtas = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(ToolBarAtas);
        getSupportActionBar().setTitle("TRANS PADANG");
//        ToolBarAtas.setSubtitle("https://badoystudio.com");
//        ToolBarAtas.setLogo(R.drawable.trans);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        waiting= findViewById(R.id.loadDataBus);
        load = findViewById(R.id.memuat_data);

        pesanAdapter = new pesanAdapter();
        pesanAdapter.setClickHandler(this);

        rvDaftarPesan = findViewById(R.id.rvListPesan);
        rvDaftarPesan.setAdapter(pesanAdapter);
        RecyclerView.LayoutManager layoutManager;

        rvDaftarPesan.setVisibility(View.INVISIBLE);
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);

        int orientasi = getResources().getConfiguration().orientation;
        if(orientasi == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new LinearLayoutManager(this);
        }else{
            layoutManager = new GridLayoutManager(this,2);
        }
        rvDaftarPesan.setLayoutManager(layoutManager);
        rvDaftarPesan.setHasFixedSize(true);

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
