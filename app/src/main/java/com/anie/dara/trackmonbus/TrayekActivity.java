package com.anie.dara.trackmonbus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.anie.dara.trackmonbus.adapter.TrayekAdapter;
import com.anie.dara.trackmonbus.model.Bus;
import com.anie.dara.trackmonbus.model.Halte;
import com.anie.dara.trackmonbus.model.Trayek;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrayekActivity extends AppCompatActivity implements com.anie.dara.trackmonbus.adapter.TrayekAdapter.OnItemClicked {

    static RecyclerView rvDaftarTrayek;
    static TrayekAdapter trayekAdapter;
    static ProgressBar waiting;
    static TextView load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trayek);

        waiting= findViewById(R.id.loadDataTrayek);
        load = findViewById(R.id.memuat_trayek);

        trayekAdapter = new TrayekAdapter();
        trayekAdapter.setClickHandler(this);

        rvDaftarTrayek = findViewById(R.id.rvListTrayek);
        rvDaftarTrayek.setAdapter(trayekAdapter);
        RecyclerView.LayoutManager layoutManager;

        rvDaftarTrayek.setVisibility(View.INVISIBLE);
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);

        int orientasi = getResources().getConfiguration().orientation;
        if(orientasi == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new LinearLayoutManager(this);
        }else{
            layoutManager = new GridLayoutManager(this,2);
        }
        rvDaftarTrayek.setLayoutManager(layoutManager);
        rvDaftarTrayek.setHasFixedSize(true);

        getAllTrayek();
    }

    private void getAllTrayek() {
        load.setText("Memuat Data");
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);
        rvDaftarTrayek.setVisibility(View.INVISIBLE);


        if(konekkah()){
            dbClient client = (new Retrofit.Builder()
                    .baseUrl("http://192.168.43.14/trackmonbus/public/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build())
                    .create(dbClient.class);

            Call<List<Trayek>> call = client.getAllTrayek();
            call.enqueue(new Callback<List<Trayek>>() {
                @Override
                public void onResponse(Call<List<Trayek>> call,  Response<List<Trayek>> response) {
                    waiting.setVisibility(View.INVISIBLE);
                    load.setVisibility(View.INVISIBLE);
                    rvDaftarTrayek.setVisibility(View.VISIBLE);


                    List<Trayek> listTrayekItem = response.body();
                    if(listTrayekItem == null){
                        Toast.makeText(TrayekActivity.this , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(TrayekActivity.this , "Pesan berhasil diload", Toast.LENGTH_SHORT).show();
                        trayekAdapter.setDataTrayek(new ArrayList<Trayek>(listTrayekItem));
                    }

                }


                @Override
                public void onFailure(Call<List<Trayek>> call, Throwable t) {
                    load.setText(t.toString());
                    Toast.makeText(TrayekActivity.this , t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(TrayekActivity.this , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
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

    @Override
    public void ItemClicked(Trayek trayek) {
        Toast.makeText(TrayekActivity.this, "Item yang diklik adalah : " + trayek.getTrayek(), Toast.LENGTH_SHORT).show();
        Intent HalteIntent = new Intent(TrayekActivity.this, HalteActivity.class);
        HalteIntent.putExtra("trayek", trayek);
        startActivity(HalteIntent);
    }
}
