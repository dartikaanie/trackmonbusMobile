package com.anie.dara.trackmonbus_supir;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus_supir.adapter.RitAdapter;
import com.anie.dara.trackmonbus_supir.model.Rit;
import com.anie.dara.trackmonbus_supir.model.jadwal.JadwalDetail;
import com.anie.dara.trackmonbus_supir.rest.ApiClient;
import com.anie.dara.trackmonbus_supir.rest.dbClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.substring;

public class DetailTransActivity extends AppCompatActivity implements View.OnClickListener {

    JadwalDetail jadwal;
    TextView NoBus, no_tnkb, kapasitas, namaJalur, namaSupir, namaTrayek, hari_tgl, km_awal, km_akhir, keterangan;
    Button btnPeta, btnUbah, btn_checkpoint;
    String no_bus, tgl;

    static RecyclerView rvDaftarRit;
    static RitAdapter ritAdapter;
    Toolbar toolbar;
    ImageView toolbarTitle;
    private dbClient client = ApiClient.getClient().create(dbClient.class);
    SwipeRefreshLayout swLayout;
    private LinearLayout llayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trans);

        //set toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NoBus = findViewById(R.id.NoBus);
        no_tnkb = findViewById(R.id.no_tnkb);
        kapasitas = findViewById(R.id.kapasitas);
        namaJalur = findViewById(R.id.namaJalur);
        namaSupir = findViewById(R.id.namaSupir);
        hari_tgl = findViewById(R.id.tgl);
        namaTrayek = findViewById(R.id.namaTrayek2);
        km_awal = findViewById(R.id.TVkm_awal);
        km_akhir = findViewById(R.id.TVKM_akhir);
        keterangan = findViewById(R.id.TVKeterangan);
        btnPeta = findViewById(R.id.btn_peta);
        btnPeta.setOnClickListener(this);
        btnUbah = findViewById(R.id.btn_ubah);
        btnUbah.setOnClickListener(this);
        btn_checkpoint = findViewById(R.id.btnCheckpoint);
        btn_checkpoint.setOnClickListener(this);

        jadwal =   getIntent().getExtras().getParcelable("jadwal");

        if(jadwal != null){

            NoBus.setText(jadwal.getJadwal().getBuses().getNoBus());
            no_tnkb.setText(jadwal.getJadwal().getBuses().getNoTnkb());
            kapasitas.setText(Integer.toString(jadwal.getJadwal().getBuses().getKapasitas()));
            namaJalur.setText(jadwal.getJadwal().getDetailTrayeks().getJalurs().getNamaJalur());
//            namaSupir.setText(jadwal.getUsers().getName() + " - "+ jadwal.getPramugaras().getNamaPramugara() );
            namaTrayek.setText(jadwal.getJadwal().getDetailTrayeks().getTrayeks().getTrayek());
            no_bus =jadwal.getJadwal().getBuses().getNoBus();

            hari_tgl.setText(substring(jadwal.getTgl(),0,10));
            km_akhir.setText(Float.toString(jadwal.getJadwal().getKmAkhir()));
            km_awal.setText(Float.toString(jadwal.getJadwal().getKmAwal()));
            keterangan.setText(jadwal.getJadwal().getKeterangan());


            no_bus =jadwal.getJadwal().getBuses().getNoBus();
            tgl = jadwal.getTgl();
        }else{
            Intent intent =  new Intent(DetailTransActivity.this, MonitoringPosisi.class);
            intent.putExtra("jadwal", jadwal);
            startActivity(intent);
        }
        getJadwal();
        //detail RIT

        ritAdapter = new RitAdapter();

        rvDaftarRit = findViewById(R.id.rvDaftarRit);
        rvDaftarRit.setAdapter(ritAdapter);
        RecyclerView.LayoutManager layoutManager;

        rvDaftarRit.setVisibility(View.INVISIBLE);
//        waiting.setVisibility(View.VISIBLE);
//        load.setVisibility(View.VISIBLE);

        layoutManager = new LinearLayoutManager(this);

        rvDaftarRit.setLayoutManager(layoutManager);
        rvDaftarRit.setHasFixedSize(true);


        swLayout = (SwipeRefreshLayout) findViewById(R.id.swlayout);
        llayout = (LinearLayout) findViewById(R.id.ll_swiperefresh);
        swLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorYellow, R.color.colorRed);

        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        swLayout.setRefreshing(false);
                        getJadwal();
                        getDataRit();
                    }
                }, 1000);
            }
        });

       getDataRit();
    }


    private void getJadwal() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Memuat Data. . .");
        dialog.show();
        Call<JadwalDetail> call = client.getDataJadwal(no_bus,tgl);
        call.enqueue(new Callback<JadwalDetail>() {
            @Override
            public void onResponse(Call<JadwalDetail> call, Response<JadwalDetail> response) {
                JadwalDetail getJadwal = response.body();
                if(getJadwal!=null){
                    NoBus.setText(getJadwal.getJadwal().getBuses().getNoBus());
                    no_tnkb.setText(getJadwal.getJadwal().getBuses().getNoTnkb());
                    kapasitas.setText(Integer.toString(getJadwal.getJadwal().getBuses().getKapasitas()));
                    namaJalur.setText(getJadwal.getJadwal().getDetailTrayeks().getJalurs().getNamaJalur());
//                    namaSupir.setText(getJadwal.getUsers().getName() + " - "+ getJadwal.getPramugaras().getNamaPramugara() );
                    namaTrayek.setText(getJadwal.getJadwal().getDetailTrayeks().getTrayeks().getTrayek());
                    no_bus =getJadwal.getJadwal().getBuses().getNoBus();

                    hari_tgl.setText(substring(getJadwal.getTgl(),0,10));
                    km_akhir.setText(Float.toString(getJadwal.getJadwal().getKmAkhir()));
                    km_awal.setText(Float.toString(getJadwal.getJadwal().getKmAwal()));
                    keterangan.setText(getJadwal.getJadwal().getKeterangan());

                    no_bus =getJadwal.getJadwal().getBuses().getNoBus();
                    tgl = getJadwal.getTgl();
                    jadwal = getJadwal;
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JadwalDetail> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(DetailTransActivity.this,"Ada Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getDataRit(){
        final ProgressDialog dialog = new ProgressDialog(DetailTransActivity.this);
        dialog.setMessage("Memuat Data . . .");
        dialog.show();

        if(konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<List<Rit>> call = client.getRit(no_bus, tgl);
            call.enqueue(new Callback<List<Rit>>() {

                @Override
                public void onResponse(Call<List<Rit>> call, Response<List<Rit>> response) {
                    rvDaftarRit.setVisibility(View.VISIBLE);
                    List<Rit> listItem = response.body();

                        Toast.makeText(DetailTransActivity.this , "Pesan berhasil diload", Toast.LENGTH_SHORT).show();
                        ritAdapter.setDataRit(new ArrayList<Rit>(listItem));
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<Rit>> call, Throwable t) {
                    Log.e("error detailActivity",  t.toString());
                    dialog.dismiss();

                }
            });
        }else{
            Toast.makeText(DetailTransActivity.this , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
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

    public void checkpoint(String no_bus, String tgl){
        if(konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<Rit> call = client.updateRit(no_bus, tgl);
            call.enqueue(new Callback<Rit>() {
                @Override
                public void onResponse(Call<Rit> call, Response<Rit> response) {
                    Rit rit = response.body();
                    namaJalur.setText(rit.getDetailTrayeks().getJalurs().getNamaJalur());
                    Toast.makeText(DetailTransActivity.this, "Checkpoint berhasil disimpan", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Rit> call, Throwable t) {
                    Toast.makeText(DetailTransActivity.this, "Checkpoint gagal disimpan", Toast.LENGTH_LONG).show();

                }
            });

        }else{
            Toast.makeText(DetailTransActivity.this , "Hidupkan Koneksi Internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_peta:
                Intent intent =  new Intent(DetailTransActivity.this, MonitoringPosisi.class);
                intent.putExtra("jadwal", jadwal);
                startActivity(intent);
                break;

            case R.id.btnCheckpoint:
               checkpoint(no_bus, tgl);
               getDataRit();
                break;

            case R.id.btn_ubah:
                Intent intent2 =  new Intent(DetailTransActivity.this, UbahTransActivity.class);
                intent2.putExtra("jadwal", jadwal);
                startActivity(intent2);
                break;

        }
    }
}
