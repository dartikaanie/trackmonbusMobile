package com.anie.dara.trackmonbus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.model.Bus;
import com.anie.dara.trackmonbus.model.Perihal;
import com.anie.dara.trackmonbus.model.Pesan;
import com.anie.dara.trackmonbus.rest.ApiClient;
import com.anie.dara.trackmonbus.rest.dbClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesanFormActivity extends AppCompatActivity implements View.OnClickListener {


    public static  final String DEFAULT ="N/A";
    Spinner spinPerihal, spinnerBus;
    dbClient client;
    TextView nama, noBus;
    Button btnAddBus, btnAddPesan, btn_noAddbus;
    EditText etIsi;
    ProgressDialog loading;
    String user_id,et_perihal, et_isi_keluhan, et_nobus = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_form);

        etIsi = findViewById(R.id.etIsi);
        nama = findViewById(R.id.tvNama);
        noBus = findViewById(R.id.NoBus);
        btnAddPesan = findViewById(R.id.addPesan);
        btnAddPesan.setOnClickListener(this);
        spinnerBus = findViewById(R.id.spinnerBus);
        btnAddBus = findViewById(R.id.btn_addbus);
        btn_noAddbus = findViewById(R.id.btn_nobus);
        btn_noAddbus.setOnClickListener(this);
        btnAddBus.setOnClickListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("name", DEFAULT);
        user_id = sharedPreferences.getString("user_id",DEFAULT);
        if(username.equals(DEFAULT)){
            Toast.makeText(this,"Tidak ada data", Toast.LENGTH_SHORT).show();
        }else{
            nama.setText("Hai, "+username);
        }

        spinPerihal = findViewById(R.id.spin_perihal);

        initSpinnerPerihal();


        spinPerihal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_perihal = parent.getItemAtPosition(position).toString();
                Toast.makeText(PesanFormActivity.this, "Kamu memilih " + et_perihal, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_nobus = parent.getItemAtPosition(position).toString();
                Toast.makeText(PesanFormActivity.this, "Kamu memilih " + et_nobus, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void initSpinnerPerihal(){
       loading = ProgressDialog.show(this, null, "harap tunggu...", true, false);
        client = ApiClient.getClient().create(dbClient.class);
        Call<List<Perihal>> call = client.getAllPerihals();
        call.enqueue(new Callback<List<Perihal>>() {
            @Override
            public void onResponse(Call<List<Perihal>> call, Response<List<Perihal>> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    List<Perihal> perihals = response.body();
                    List<String> listSpinner = new ArrayList<String>();
                    for (int i = 0; i < perihals.size(); i++){
                        listSpinner.add( perihals.get(i).getPerihal_id() +" - "+ perihals.get(i).getPerihal());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PesanFormActivity.this,
                            android.R.layout.simple_spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinPerihal.setAdapter(adapter);
                } else {
                    loading.dismiss();
                    Toast.makeText(PesanFormActivity.this, "Gagal mengambil data dosen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Perihal>> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(PesanFormActivity.this, "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void initSpinnerBus(){
        loading = ProgressDialog.show(this, null, "harap tunggu...", true, false);
        client = ApiClient.getClient().create(dbClient.class);
        Call<List<Bus>> call = client.getAllBus();
        call.enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    List<Bus> buses = response.body();
                    List<String> listSpinner = new ArrayList<String>();
                    for (int i = 0; i < buses.size(); i++){
                        listSpinner.add(buses.get(i).getNo_bus());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PesanFormActivity.this,
                            android.R.layout.simple_spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerBus.setAdapter(adapter);
                } else {
                    loading.dismiss();
                    Toast.makeText(PesanFormActivity.this, "Gagal mengambil data dosen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(PesanFormActivity.this, "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addbus:
                initSpinnerBus();
                noBus.setVisibility(View.VISIBLE);
                spinnerBus.setVisibility(View.VISIBLE);
                btnAddBus.setVisibility(View.INVISIBLE);
                btn_noAddbus.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_nobus:
                noBus.setVisibility(View.INVISIBLE);
                spinnerBus.setVisibility(View.INVISIBLE);
                btnAddBus.setVisibility(View.VISIBLE);
                btn_noAddbus.setVisibility(View.INVISIBLE);
                break;

            case R.id.addPesan:
                et_isi_keluhan = etIsi.getText().toString();
                if(validateForm(et_isi_keluhan,et_perihal)){
                    addFormPesan(et_isi_keluhan,et_perihal,et_nobus);
                }
                break;

        }
    }


    private boolean validateForm(String isiKeluhan, String perihal) {
        if(isiKeluhan == null || isiKeluhan.trim().length() == 0  ){
            Toast.makeText(this, "Isi Keluhan harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(perihal == null || perihal.trim().length() == 0  ){
            Toast.makeText(this, "perihal harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void addFormPesan(final String et_isi_keluhan, String et_perihal, String et_nobus) {
        final ProgressDialog dialog123 = new ProgressDialog(this);
        dialog123.setMessage("Menyimpan Data Pesan. . .");
        dialog123.show();

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String created_at = dateFormat.format(date);

        Call<Pesan>  call = client.AddPesan(user_id, et_perihal,et_isi_keluhan, et_nobus, created_at);
        call.enqueue(new Callback<Pesan>() {
            @Override
            public void onResponse(Call<Pesan> call, Response<Pesan> response) {
                Pesan pesan = response.body();
                Log.e("user", String.valueOf(pesan));
                Toast.makeText(PesanFormActivity.this,"Data disimpan", Toast.LENGTH_SHORT).show();
                startActivity( new Intent(PesanFormActivity.this, PesanActivity.class));
                dialog123.dismiss();
            }
            @Override
            public void onFailure(Call<Pesan> call, Throwable t) {
                dialog123.dismiss();
                Toast.makeText(PesanFormActivity.this,"Error. Ulangi lagi" + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
