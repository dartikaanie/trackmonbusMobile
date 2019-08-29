package com.anie.dara.trackmonbus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anie.dara.trackmonbus.model.Perihal;
import com.anie.dara.trackmonbus.model.Pesan;
import com.anie.dara.trackmonbus.rest.ApiClient;
import com.anie.dara.trackmonbus.rest.dbClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addPerihalActivity extends AppCompatActivity {
    dbClient client;
    EditText etPerihal;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_perihal);

        etPerihal = findViewById(R.id.etPerihal);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( etPerihal.getText().toString() != null){
                    addPerihal(etPerihal.getText().toString());
                }

            }
        });
    }

    public void addPerihal(String perihal){

        if(konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<Perihal> call = client.addPerihal(perihal);
            call.enqueue(new Callback<Perihal>() {
                @Override
                public void onResponse(Call<Perihal> call,  Response<Perihal> response) {
                    Perihal perihal1 = response.body();
                    if(perihal1 == null){
                        Toast.makeText(addPerihalActivity.this , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(addPerihalActivity.this , "Pesan berhasil diload", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(addPerihalActivity.this, PesanFormActivity.class));
                    }

                }

                @Override
                public void onFailure(Call<Perihal> call, Throwable t) {

                    Toast.makeText(addPerihalActivity.this , t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(addPerihalActivity.this , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
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
