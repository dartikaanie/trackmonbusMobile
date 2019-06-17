package com.anie.dara.trackmonbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.model.Pesan;
import com.bumptech.glide.Glide;

public class DetailPesanActivity extends AppCompatActivity {

    TextView user,tgl, perihal,isi;
    Pesan pesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesan);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TRANS PADANG");

        user = findViewById(R.id.user);
        tgl = findViewById(R.id.tgl);
        perihal = findViewById(R.id.perihal);
        isi = findViewById(R.id.isi_pesan);

        Intent detailIntent = getIntent();
        if(null != detailIntent) {
            pesan = detailIntent.getParcelableExtra("key_foto_parcelable");
        }

        if(pesan != null) {
                getSupportActionBar().setTitle(pesan.getPerihal());
                user.setText(String.valueOf(pesan.getName()));
                tgl.setText(pesan.getCreated_at());
                perihal.setText(pesan.getPerihal());
                isi.setText(pesan.getIsi_keluhan());
            }else{
                Toast.makeText(this, "no Data", Toast.LENGTH_SHORT).show();
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
}
