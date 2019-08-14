package com.anie.dara.trackmonbus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anie.dara.trackmonbus.model.User;
import com.anie.dara.trackmonbus.rest.ApiClient;
import com.anie.dara.trackmonbus.rest.dbClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin, btn_daftar;
    private dbClient client = ApiClient.getClient().create(dbClient.class);
    public static  final String DEFAULT ="N/A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);;

        btn_daftar = findViewById(R.id.btn_daftar);
        btn_daftar.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrasiActivity.class);
                startActivity(intent);
            }
        }   );

        etEmail = findViewById(R.id.ETemail);
        etPassword = findViewById(R.id.ETpassword);

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("name", DEFAULT);
        if(username.equals(DEFAULT)){
            Toast.makeText(this,"Anda Belum Login", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent =  new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if(validateLogin(email,password)){
                    doLogin(email,password);
                }
            }
        });

    }

    private void doLogin(final String email, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Memuat Data . . .");
        dialog.show();

        Call<User> call = client.LoginUser(email,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if(user.getMessage().equals("true")){
                    SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_id", user.getId());
                    editor.putString("name", user.getName());
                    editor.commit();
                    Intent intent =  new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"Error. Ulangi lagi", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Error. Ulangi lagi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateLogin(String email, String password) {
        if(email == null || email.trim().length() == 0  ){
            Toast.makeText(this, "email harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0  ){
            Toast.makeText(this, "password harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
