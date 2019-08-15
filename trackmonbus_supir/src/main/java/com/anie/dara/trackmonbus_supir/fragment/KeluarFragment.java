package com.anie.dara.trackmonbus_supir.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.anie.dara.trackmonbus_supir.LoginActivity;
import com.anie.dara.trackmonbus_supir.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class KeluarFragment extends Fragment {

    View mView;
    private Toolbar toolbar;
    private ImageView toolbarTitle;
    Button keluar;
    public static  final String DEFAULT ="tidak aktif";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_keluar, container, false);


        //set toolbar
        toolbar = mView.findViewById(R.id.toolbar);
        toolbarTitle = (ImageView) mView.findViewById(R.id.toolbar_title);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        keluar = mView.findViewById(R.id.btn_logout);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder
                        .setMessage("Apakah Anda yakin untuk keluar ?")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyData", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("user_id", DEFAULT);
                                editor.putString("name", DEFAULT);

                                String A = sharedPreferences.getString("user_id", DEFAULT);
                                editor.commit();

                                Log.e("s",A);



                                Intent intent =  new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Batal", null);

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });


        return mView;
    }

}
