package com.anie.dara.trackmonbus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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

import com.anie.dara.trackmonbus.adapter.pesanAdapter;
import com.anie.dara.trackmonbus.model.Pesan;
import com.anie.dara.trackmonbus.rest.ApiClient;
import com.anie.dara.trackmonbus.rest.dbClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MypesanActivity extends AppCompatActivity implements com.anie.dara.trackmonbus.adapter.pesanAdapter.OnItemClicked {

    static RecyclerView rvDaftarPesan;
    static com.anie.dara.trackmonbus.adapter.pesanAdapter pesanAdapter;
    static ProgressBar waiting;
    static TextView load;
    dbClient client;
    static Pesan dataPesan = new Pesan();
    ArrayList<Pesan> pesanList;
    static Activity activity;
    SwipeRefreshLayout swLayout;
    LinearLayout llayout;
    String user_id;
    public static  final String DEFAULT ="N/A";
    private Toolbar toolbar;
    private ImageView toolbarTitle, noPesan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = (ImageView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                startActivity(new Intent(MypesanActivity.this, PesanFormActivity.class));
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", DEFAULT);




        waiting= findViewById(R.id.loadData);
        load = findViewById(R.id.memuat_data);
        noPesan = findViewById(R.id.noPesan);
        pesanAdapter = new pesanAdapter(this);
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
        noPesan.setVisibility(View.INVISIBLE);

        Log.e("user", user_id);
        rvDaftarPesan.setVisibility(View.INVISIBLE);

        if(konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<List<Pesan>> call = client.getMyPesan(user_id);
            call.enqueue(new Callback<List<Pesan>>() {
                @Override
                public void onResponse(Call<List<Pesan>> call,  Response<List<Pesan>> response) {
                    waiting.setVisibility(View.INVISIBLE);
                    load.setVisibility(View.INVISIBLE);
                    rvDaftarPesan.setVisibility(View.VISIBLE);

                    List<Pesan> listPesanItem = response.body();
                    if(listPesanItem.size() == 0 || listPesanItem == null){
                        noPesan.setVisibility(View.VISIBLE);
                        rvDaftarPesan.setVisibility(View.INVISIBLE);
                        Toast.makeText(MypesanActivity.this , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MypesanActivity.this , "Data berhasil diload", Toast.LENGTH_SHORT).show();
                        pesanAdapter.setDataPesan(new ArrayList<Pesan>(listPesanItem));
                    }

                }

                @Override
                public void onFailure(Call<List<Pesan>> call, Throwable t) {
                    load.setText(t.toString());
                    Toast.makeText(MypesanActivity.this , t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(MypesanActivity.this , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void ItemClicked(Pesan pesan) {
        Toast.makeText(MypesanActivity.this, "Item yang diklik adalah : " + pesan.getKeluhan_id(), Toast.LENGTH_SHORT).show();
        Intent detailIntent = new Intent(MypesanActivity.this, DetailPesanActivity.class);
        detailIntent.putExtra("key_pesan_parcelable", pesan);
        startActivity(detailIntent);
    }

    @Override
    public void deletePesan(final Pesan pesan) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MypesanActivity.this);
        alertDialogBuilder
                .setMessage("Apakah Anda yakin untuk Menghapus data ini ?")
                .setIcon(R.drawable.trans)
                .setCancelable(false)
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);

                        final ProgressDialog dialog123 = new ProgressDialog(MypesanActivity.this);
                        dialog123.setMessage("Menghapus Komentar. . .");
                        dialog123.show();
//                        Log.e("pesan.getKeluhan_id()", pesan.getKeluhan_id());
                        Call<ResponseBody>  call = client.hapusPesan(pesan.getKeluhan_id());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                ResponseBody result = response.body();
                                if(result != null){
                                    Toast.makeText(MypesanActivity.this, "data dihapus", Toast.LENGTH_SHORT).show();
                                    dialog123.dismiss();
                                    getAllPesan();
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                dialog123.dismiss();
                                Log.e("deletePesan", t.toString());
                                Toast.makeText(MypesanActivity.this,"Error. Ulangi lagi" + t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Batal", null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
