package com.anie.dara.trackmonbus_supir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus_supir.model.jadwal.Jadwal;
import com.anie.dara.trackmonbus_supir.model.jadwal.JadwalDetail;
import com.anie.dara.trackmonbus_supir.rest.ApiClient;
import com.anie.dara.trackmonbus_supir.rest.dbClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.substring;

public class UbahTransActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ImageView toolbarTitle;

    TextView NoBus, no_tnkb, kapasitas, namaSupir, namaTrayek, hari_tgl, namaJalur;
    EditText ETKmAwal, ETkmAkhir, ETketerangan;
    Button btnSimpan, btnBatal;
    JadwalDetail jadwal;
    String no_bus, tgl;
    private dbClient client = ApiClient.getClient().create(dbClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_trans);

        //set toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        NoBus = findViewById(R.id.NoBus);
        no_tnkb = findViewById(R.id.no_tnkb);
        kapasitas = findViewById(R.id.kapasitas);
        namaJalur = findViewById(R.id.namaJalur);
        namaSupir = findViewById(R.id.namaSupir);
        hari_tgl = findViewById(R.id.tgl);
        namaTrayek = findViewById(R.id.namaTrayek2);


        btnSimpan = findViewById(R.id.btn_simpan);
        btnSimpan.setOnClickListener(this);
        btnBatal = findViewById(R.id.btn_batal);
        btnBatal.setOnClickListener(this);

        ETKmAwal = findViewById(R.id.etKmAwal);
        ETkmAkhir = findViewById(R.id.etKmAkhir);
        ETketerangan = findViewById(R.id.etKeterangan);

        jadwal =   getIntent().getExtras().getParcelable("jadwal");

        if(jadwal != null){
            NoBus.setText(jadwal.getJadwal().getBuses().getNoBus());
            no_tnkb.setText(jadwal.getJadwal().getBuses().getNoTnkb());
            kapasitas.setText(Integer.toString(jadwal.getJadwal().getBuses().getKapasitas()));
            namaJalur.setText(jadwal.getJadwal().getDetailTrayeks().getJalurs().getNamaJalur());
            namaSupir.setText(jadwal.getUsers().getName() + " - "+ jadwal.getPramugaras().getNamaPramugara() );
            namaTrayek.setText(jadwal.getJadwal().getDetailTrayeks().getTrayeks().getTrayek());
            no_bus =jadwal.getJadwal().getBuses().getNoBus();

            if(jadwal.getJadwal().getKmAwal() != 0){
                ETKmAwal.setText(Float.toString(jadwal.getJadwal().getKmAwal()));
            }
            Log.e("jadwal", jadwal.getJadwal().toString());

            if(jadwal.getJadwal().getKmAkhir()!=0){
                ETkmAkhir.setText(Float.toString(jadwal.getJadwal().getKmAkhir()));
            }

           if(jadwal.getJadwal().getKeterangan() != null){
                ETketerangan.setText(jadwal.getJadwal().getKeterangan());
            }

            hari_tgl.setText(substring(jadwal.getTgl(),0,10));
            no_bus = jadwal.getJadwal().getNoBus();
            tgl = jadwal.getTgl();
        }else{
            startActivity(new Intent(UbahTransActivity.this, MainActivity.class));
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.btn_simpan:
                String kmAwal = ETKmAwal.getText().toString();
                String kmAkhir = ETkmAkhir.getText().toString();
                String keterangan = ETketerangan.getText().toString();
                simpanPerubahan(kmAwal, kmAkhir, keterangan);

                break;

            case R.id.btn_batal:
                intent =  new Intent(UbahTransActivity.this, DetailTransActivity.class);
                intent.putExtra("jadwal", jadwal);
                startActivity(intent);
                break;
        }
    }

    private void simpanPerubahan(String kmAwal, String kmAkhir, String keterangan) {
        if(kmAwal == null || kmAwal.trim().length() == 0  ){
            kmAwal ="0";
        }
        if(kmAkhir == null || kmAkhir.trim().length() == 0  ){
            kmAkhir= "0" ;
        }

        if(keterangan == null || keterangan.trim().length() == 0  ){
            keterangan="";
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Menyimpan Data . . .");
        dialog.show();

        Call<Jadwal> call = client.UbahDataJadwal(no_bus, tgl, kmAwal, kmAkhir, keterangan);
        call.enqueue(new Callback<Jadwal>() {
            @Override
            public void onResponse(Call<Jadwal> call, Response<Jadwal> response) {
                   Jadwal dataJadwal = response.body();
                    Toast.makeText(UbahTransActivity.this,"Perubahan berhasil disimpan", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Intent intent =  new Intent(UbahTransActivity.this, DetailTransActivity.class);
                    intent.putExtra("jadwal", jadwal);
                    startActivity(intent);
            }

            @Override
            public void onFailure(Call<Jadwal> call, Throwable t) {
                Toast.makeText(UbahTransActivity.this,"Perubahan gagal disimpan", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
