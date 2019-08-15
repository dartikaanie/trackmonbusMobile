package com.anie.dara.trackmonbus_supir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anie.dara.trackmonbus_supir.model.User;
import com.anie.dara.trackmonbus_supir.rest.ApiClient;
import com.anie.dara.trackmonbus_supir.rest.dbClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    private dbClient client = ApiClient.getClient().create(dbClient.class);
    public static  final String DEFAULT ="tidak aktif";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.ETemail);
        etPassword = findViewById(R.id.ETpassword);

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

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", DEFAULT);
        if(user_id.equals(DEFAULT)){
            Toast.makeText(this,"Anda Belum Login", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent =  new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void doLogin(final String email, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Memuat Data . . .");
        dialog.show();

        Call<User> call = client.Loginv2(email,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if(user.getMessage().equals("true")){

                    SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_id", user.getUser_id());
                    editor.putString("name", user.getName());
                    editor.commit();

                    Intent intent =  new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("user_id", user.getUser_id());
                    startActivity(intent);
                    dialog.dismiss();
                }else{
                    Toast.makeText(LoginActivity.this,"Email / Password Anda Salah", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Error. Ulangi lagi", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
