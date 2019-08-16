package com.anie.dara.trackmonbus.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.HalteActivity;
import com.anie.dara.trackmonbus.MainActivity;
import com.anie.dara.trackmonbus.R;
import com.anie.dara.trackmonbus.adapter.TrayekAdapter;
import com.anie.dara.trackmonbus.model.Trayek;
import com.anie.dara.trackmonbus.rest.ApiClient;
import com.anie.dara.trackmonbus.rest.dbClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrayekFragment extends Fragment implements TrayekAdapter.OnItemClicked {

     RecyclerView rvDaftarTrayek;
     TrayekAdapter trayekAdapter;
     ProgressBar waiting;
     TextView load;
     Activity activity;
    private dbClient client = ApiClient.getClient().create(dbClient.class);


    private Toolbar toolbar;
    private ImageView toolbarTitle;

    public TrayekFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_trayek, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbarTitle = (ImageView) view.findViewById(R.id.toolbar_title);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        waiting= view.findViewById(R.id.loadDataTrayek);
        load = view.findViewById(R.id.memuat_trayek);

        trayekAdapter = new TrayekAdapter();
        trayekAdapter.setClickHandler(this);

        rvDaftarTrayek = view.findViewById(R.id.rvListTrayek);
        rvDaftarTrayek.setAdapter(trayekAdapter);
        RecyclerView.LayoutManager layoutManager;

        rvDaftarTrayek.setVisibility(View.INVISIBLE);
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);

        layoutManager = new LinearLayoutManager(getContext());

        rvDaftarTrayek.setLayoutManager(layoutManager);
        rvDaftarTrayek.setHasFixedSize(true);

        getAllTrayek();

        return view;
    }

    private void getAllTrayek() {
//        load.setText("Memuat Data");
//        waiting.setVisibility(View.VISIBLE);
//        load.setVisibility(View.VISIBLE);
//        rvDaftarTrayek.setVisibility(View.INVISIBLE);

        if(((MainActivity)activity).konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<List<Trayek>> call = client.getAllTrayek();
            call.enqueue(new Callback<List<Trayek>>() {
                @Override
                public void onResponse(Call<List<Trayek>> call,  Response<List<Trayek>> response) {
                    waiting.setVisibility(View.INVISIBLE);
                    load.setVisibility(View.INVISIBLE);
                    rvDaftarTrayek.setVisibility(View.VISIBLE);

                    List<Trayek> listTrayekItem = response.body();
                    if(listTrayekItem == null){
                        Toast.makeText(getContext() , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext() , "Pesan berhasil diload", Toast.LENGTH_SHORT).show();
                        trayekAdapter.setDataTrayek(new ArrayList<Trayek>(listTrayekItem));
                    }

                }


                @Override
                public void onFailure(Call<List<Trayek>> call, Throwable t) {
//                    load.setText(t.toString());
                    Toast.makeText(getContext() , t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getContext() , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void ItemClicked(Trayek trayek) {
        Toast.makeText(getContext(), "Item yang diklik adalah : " + trayek.getTrayek(), Toast.LENGTH_SHORT).show();
        Intent HalteIntent = new Intent(getContext(), HalteActivity.class);
        HalteIntent.putExtra("trayek", trayek);
        startActivity(HalteIntent);
    }
}
