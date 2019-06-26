package com.anie.dara.trackmonbus_supir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anie.dara.trackmonbus_supir.model.Login;
import com.anie.dara.trackmonbus_supir.rest.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    private dbClient client = ApiClient.getClient().create(dbClient.class);

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
    }

    private void doLogin(final String email, String password) {
        Call<Login> call = client.Loginv2(email,password);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login login = response.body();
                if(login.getMessage().equals("true")){
                    Intent intent =  new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("user_id", login.getUser_id());
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"Error. Ulangi lagi", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
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
