package com.anie.dara.trackmonbus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anie.dara.trackmonbus.rest.ApiClient;
import com.anie.dara.trackmonbus.rest.dbClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    Button btn_masuk, btn_daftar;
    EditText etEmail, etPassword, etNama,ETpassword2;
    private dbClient client = ApiClient.getClient().create(dbClient.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_masuk = findViewById(R.id.btn_masuk);
        btn_daftar = findViewById(R.id.btn_daftar);
        btn_masuk.setOnClickListener(this);
        btn_daftar.setOnClickListener(this);

        etEmail = findViewById(R.id.ETemail);
        ETpassword2 = findViewById(R.id.ETpassword2);
        etPassword = findViewById(R.id.ETpassword);
        etNama = findViewById(R.id.ETNama);
    }


    private boolean validateForm(String nama, String email, String password, String password2) {
        if(email == null || email.trim().length() == 0  ){
            Toast.makeText(this, "email harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0  ){
            Toast.makeText(this, "password harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(nama == null || nama.trim().length() == 0  ){
            Toast.makeText(this, "Nama harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password == password2 ){
            Toast.makeText(this, "Password yang diulang tidak sama", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void doRegister(final String nama, String email, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Menyimpan Data . . .");
        dialog.show();

        Call<Response> call = client.register(nama,email,password);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, Response<Response> response) {
                Log.e("register" , String.valueOf(response.body()));

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"Error. Ulangi lagi", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_masuk:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                break;

            case R.id.btn_daftar:
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String nama = etNama.getText().toString();
                String password2 = ETpassword2.getText().toString();
                if(validateForm(nama, email,password, password2 )){
                    doRegister(nama,email,password);
                }
                break;
        }
    }
}
