package com.anie.dara.trackmonbus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.fragment.HomeFragment;
import com.anie.dara.trackmonbus.fragment.TrayekFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    public static  final String DEFAULT ="N/A";


    private Toolbar toolbar;
    private ImageView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = (ImageView) findViewById(R.id.toolbar_title);
         setSupportActionBar(toolbar);
        if (toolbar != null) {
           getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        loadFragment(new HomeFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user", DEFAULT);
        if(user_id.equals(DEFAULT)){
            Toast.makeText(this,"Tidak ada data", Toast.LENGTH_SHORT).show();
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(new HomeFragment());
                    return true;
//                case R.id.navigation_trayek:
//                    loadFragment(new TrayekFragment());
//                    return true;
                case R.id.navigation_keluar:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder
                            .setMessage("Apakah Anda yakin untuk keluar ?")
                            .setIcon(R.drawable.trans)
                            .setCancelable(false)
                            .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("user_id", DEFAULT);
                                    editor.putString("name", DEFAULT);
                                    editor.commit();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                }
                            }).setNegativeButton("Batal", null);

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    return true;
            }
            return false;
        }
    };


    public Boolean konekkah(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean konek = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return konek;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
