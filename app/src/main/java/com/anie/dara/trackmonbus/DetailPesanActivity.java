package com.anie.dara.trackmonbus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.adapter.KomentarAdapter;
import com.anie.dara.trackmonbus.model.Komentar;
import com.anie.dara.trackmonbus.model.Pesan;
import com.anie.dara.trackmonbus.rest.ApiClient;
import com.anie.dara.trackmonbus.rest.dbClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPesanActivity extends AppCompatActivity implements View.OnClickListener {

    TextView user,tgl, perihal,isi, jumKomentar, userKomentar;
    Pesan pesan;
    public static  final String DEFAULT ="N/A";
    RecyclerView rvDaftarKomentar;
    KomentarAdapter komentarAdapter;
    dbClient client;
    SwipeRefreshLayout swLayout;
    Button btnAddKomentar;
    EditText etIsiKomentar;
    String isi_komentar, user_id, keluhan_id;

    Toolbar toolbar;
    ImageView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesan);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = (ImageView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = findViewById(R.id.user);
        tgl = findViewById(R.id.tgl);
        perihal = findViewById(R.id.perihal);
        isi = findViewById(R.id.isi_pesan);
        jumKomentar = findViewById(R.id.komentar);
        userKomentar = findViewById(R.id.user_komentar);
        swLayout = (SwipeRefreshLayout) findViewById(R.id.swlayout);
        btnAddKomentar = findViewById(R.id.btn_add_komenar);
        btnAddKomentar.setOnClickListener(this);
        etIsiKomentar = findViewById(R.id.etIsiKomentar);

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("name", DEFAULT);
       user_id = sharedPreferences.getString("user_id", DEFAULT);
        userKomentar.setText(username);

        Intent detailIntent = getIntent();
        if(null != detailIntent) {
            pesan = detailIntent.getParcelableExtra("key_pesan_parcelable");
        }

        if(pesan != null) {
                getSupportActionBar().setTitle(pesan.getPerihal());
                user.setText(String.valueOf(pesan.getName()));
                tgl.setText(pesan.getCreated_at());
                perihal.setText(pesan.getPerihal());
                isi.setText(pesan.getIsi_keluhan());
                jumKomentar.setText(String.valueOf(pesan.getJumlah_komentar()));
                keluhan_id = pesan.getKeluhan_id();
            }else{
                Toast.makeText(this, "no Data", Toast.LENGTH_SHORT).show();
            }

        komentarAdapter = new KomentarAdapter();


        rvDaftarKomentar = findViewById(R.id.rvKomentarPesan);
        rvDaftarKomentar.setAdapter(komentarAdapter);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);

        rvDaftarKomentar.setLayoutManager(layoutManager);
        rvDaftarKomentar.setHasFixedSize(true);


        swLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);

        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        swLayout.setRefreshing(false);

                        getPesanKomentar();
                    }
                }, 1000);
            }
        });
        getPesanKomentar();

    }

    private void getPesanKomentar() {
        if(konekkah()){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Memuat Data . . .");
            dialog.show();

            client = ApiClient.getClient().create(dbClient.class);
            Call<List<Komentar>> call = client.getPesanKomentar(pesan.getKeluhan_id());
            call.enqueue(new Callback<List<Komentar>>() {
                @Override
                public void onResponse(Call<List<Komentar>> call,  Response<List<Komentar>> response) {

                    List<Komentar> listItem = response.body();
                    jumKomentar.setText(String.valueOf(listItem.size()));
                    if(listItem == null){
                        Toast.makeText(DetailPesanActivity.this , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(DetailPesanActivity.this , "Komentar berhasil diload", Toast.LENGTH_SHORT).show();
                        komentarAdapter.setDataKomentar(new ArrayList<Komentar>(listItem));
                    }
                    dialog.dismiss();

                }

                @Override
                public void onFailure(Call<List<Komentar>> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(DetailPesanActivity.this ,"Gagal Memuat Data", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(DetailPesanActivity.this , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public Boolean konekkah(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean konek = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return konek;
    }

    private boolean validateForm(String isiKomentar) {
        if(isiKomentar == null || isiKomentar.trim().length() == 0  ){
            Toast.makeText(this, "Isi Komentar harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }


    private void addKomentarPesan(String isi_komentar) {
        final ProgressDialog dialog123 = new ProgressDialog(this);
        dialog123.setMessage("Menyimpan Data Komentar. . .");
        dialog123.show();
        Call<Komentar>  call = client.AddKomentarPesan(keluhan_id, user_id, isi_komentar);
        call.enqueue(new Callback<Komentar>() {
            @Override
            public void onResponse(Call<Komentar> call, Response<Komentar> response) {
                Komentar komentar = response.body();
                Toast.makeText(DetailPesanActivity.this,"Data disimpan", Toast.LENGTH_SHORT).show();
                dialog123.dismiss();
                getPesanKomentar();
            }
            @Override
            public void onFailure(Call<Komentar> call, Throwable t) {
                dialog123.dismiss();
                Toast.makeText(DetailPesanActivity.this,"Error. Ulangi lagi" + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_komenar:
                String isi_komentar = etIsiKomentar.getText().toString();
                if(validateForm(isi_komentar)){
                    addKomentarPesan(isi_komentar);
                }
                etIsiKomentar.getText().clear();
                break;
        }
    }
}
