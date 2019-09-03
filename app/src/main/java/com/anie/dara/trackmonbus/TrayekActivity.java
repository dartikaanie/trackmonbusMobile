package com.anie.dara.trackmonbus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.adapter.TrayekAdapter;
import com.anie.dara.trackmonbus.model.Trayeks.Trayeks;
import com.anie.dara.trackmonbus.rest.ApiClient;
import com.anie.dara.trackmonbus.rest.dbClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrayekActivity extends AppCompatActivity implements TrayekAdapter.OnItemClicked {

    RecyclerView rvDaftarTrayek;
    TrayekAdapter trayekAdapter;
    ProgressBar waiting;
    TextView load;
    Activity activity;
    private dbClient client = ApiClient.getClient().create(dbClient.class);


    private Toolbar toolbar;
    private ImageView toolbarTitle;
    private SwipeRefreshLayout swLayout;
    private LinearLayout llayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trayek);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = (ImageView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        waiting= findViewById(R.id.loadData);
        load = findViewById(R.id.memuat_trayek);

        trayekAdapter = new TrayekAdapter();
        trayekAdapter.setClickHandler(this);

        rvDaftarTrayek = findViewById(R.id.rvListTrayek);
        rvDaftarTrayek.setAdapter(trayekAdapter);
        RecyclerView.LayoutManager layoutManager;

        rvDaftarTrayek.setVisibility(View.INVISIBLE);
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);

        layoutManager = new LinearLayoutManager(this);

        rvDaftarTrayek.setLayoutManager(layoutManager);
        rvDaftarTrayek.setHasFixedSize(true);

        swLayout = (SwipeRefreshLayout) findViewById(R.id.swlayout);
        llayout = (LinearLayout) findViewById(R.id.ll_swiperefresh);

        swLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorYellow, R.color.colorRed);


        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swLayout.setRefreshing(false);
                        getAllTrayek();
                    }
                }, 1000);
            }
        });
        getAllTrayek();
    }

    private void getAllTrayek() {
        load.setText("Memuat Data");
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);
        rvDaftarTrayek.setVisibility(View.INVISIBLE);

        if(konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<List<Trayeks>> call = client.getAllTrayek();
            call.enqueue(new Callback<List<Trayeks>>() {
                @Override
                public void onResponse(Call<List<Trayeks>> call,  Response<List<Trayeks>> response) {
                    waiting.setVisibility(View.INVISIBLE);

                    rvDaftarTrayek.setVisibility(View.VISIBLE);

                    List<Trayeks> listTrayekItem = response.body();
                    if(listTrayekItem == null){
                        load.setText("Tidak ada data");
                        Toast.makeText(TrayekActivity.this , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        load.setVisibility(View.INVISIBLE);
                        Toast.makeText(TrayekActivity.this  , "Data Trayek berhasil diload", Toast.LENGTH_SHORT).show();
                        trayekAdapter.setDataTrayek(new ArrayList<Trayeks>(listTrayekItem));
                    }

                }


                @Override
                public void onFailure(Call<List<Trayeks>> call, Throwable t) {
                    Log.e("error get Trayrks",t.toString());
                    waiting.setVisibility(View.INVISIBLE);
                    load.setText("Ada Kesalahan. Silakan Coba lagi");
                    Toast.makeText(TrayekActivity.this , t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(TrayekActivity.this  , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void ItemClicked(Trayeks trayek) {
        Toast.makeText(TrayekActivity.this , "Item yang diklik adalah : " + trayek.getTrayekId(), Toast.LENGTH_SHORT).show();
        Intent HalteIntent = new Intent(TrayekActivity.this , PilihJalurActivity.class);
        HalteIntent.putExtra("trayek", trayek);
        startActivity(HalteIntent);
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
