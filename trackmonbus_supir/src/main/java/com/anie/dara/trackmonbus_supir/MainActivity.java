package com.anie.dara.trackmonbus_supir;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus_supir.fragment.BerandaFragment;
import com.anie.dara.trackmonbus_supir.fragment.JadwalFragment;
import com.anie.dara.trackmonbus_supir.fragment.KeluarFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    String user_id;
    private Handler handler = new Handler();
    volatile boolean track = false;
    public static  final String DEFAULT ="tidak aktif";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(new BerandaFragment());
                    return true;
                case R.id.navigation_jadwal:
                    loadFragment(new JadwalFragment());
                    return true;
                case R.id.navigation_keluar:
                    loadFragment(new KeluarFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new BerandaFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        handler.postDelayed(runTrack, 5000);

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", DEFAULT);String status_track = sharedPreferences.getString("status_track", DEFAULT);
        if(user_id.equals(DEFAULT)){
            Toast.makeText(this,"Anda Belum Login", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,user_id, Toast.LENGTH_SHORT).show();
        }

    }



    private boolean loadFragment(android.support.v4.app.Fragment fragment){
        if (fragment != null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public Boolean konekkah(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean konek = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return konek;
    }


    private Runnable runTrack = new Runnable() {
        @Override
        public void run() {
            SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            String status_track = sharedPreferences.getString("status_track", DEFAULT);
            if(status_track.equals(DEFAULT)){
                track = false;
            }else{
                track = true;
            }

            if(track){
                Toast.makeText(MainActivity.this, "Updated in 5 second", Toast.LENGTH_LONG).show();
                handler.postDelayed(this, 5000);
            }
        }
    };
}
