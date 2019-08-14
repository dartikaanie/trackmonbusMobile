package com.anie.dara.trackmonbus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.adapter.BusAdapter;
import com.anie.dara.trackmonbus.model.Bus;
import com.anie.dara.trackmonbus.rest.dbClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusActivity extends AppCompatActivity {

    static RecyclerView rvDaftarBus;
    static BusAdapter busAdapter;
    static ProgressBar waiting;
    static TextView load;
//    static Pesan dataBus = new Pesan();
//    ArrayList<Pesan> pesanList;
//    static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);


        waiting= findViewById(R.id.loadDataTrayek);
        load = findViewById(R.id.memuat_bus);

        busAdapter = new BusAdapter();

        rvDaftarBus = findViewById(R.id.rvListBus);
        rvDaftarBus.setAdapter(busAdapter);
        RecyclerView.LayoutManager layoutManager;

        rvDaftarBus.setVisibility(View.INVISIBLE);
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);

        int orientasi = getResources().getConfiguration().orientation;
        if(orientasi == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new LinearLayoutManager(this);
        }else{
            layoutManager = new GridLayoutManager(this,2);
        }
        rvDaftarBus.setLayoutManager(layoutManager);
        rvDaftarBus.setHasFixedSize(true);

        getAllBus();
    }

    private void getAllBus() {
        load.setText("Memuat Data");
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);
        rvDaftarBus.setVisibility(View.INVISIBLE);


        if(konekkah()){
            dbClient client = (new Retrofit.Builder()
                    .baseUrl("http://192.168.43.14/trackmonbus/public/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build())
                    .create(dbClient.class);

            Call<List<Bus>> call = client.getAllBus();
            call.enqueue(new Callback<List<Bus>>() {
                @Override
                public void onResponse(Call<List<Bus>> call,  Response<List<Bus>> response) {
                    waiting.setVisibility(View.INVISIBLE);
                    load.setVisibility(View.INVISIBLE);
                    rvDaftarBus.setVisibility(View.VISIBLE);


                    List<Bus> listBusItem = response.body();
                    if(listBusItem == null){
                        Toast.makeText(BusActivity.this , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(BusActivity.this , "Pesan berhasil diload", Toast.LENGTH_SHORT).show();
                        busAdapter.setDataBus(new ArrayList<Bus>(listBusItem));
                    }

                }


                @Override
                public void onFailure(Call<List<Bus>> call, Throwable t) {
                    load.setText(t.toString());
                    Toast.makeText(BusActivity.this , t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(BusActivity.this , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
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
}
