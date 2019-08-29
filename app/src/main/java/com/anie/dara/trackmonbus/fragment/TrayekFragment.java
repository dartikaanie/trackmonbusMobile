package com.anie.dara.trackmonbus.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.HalteActivity;
import com.anie.dara.trackmonbus.MainActivity;
import com.anie.dara.trackmonbus.R;
import com.anie.dara.trackmonbus.adapter.TrayekAdapter;
import com.anie.dara.trackmonbus.model.Trayeks.Trayeks;
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
    private SwipeRefreshLayout swLayout;
    private LinearLayout llayout;

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


        waiting= view.findViewById(R.id.loadData);
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

        swLayout = (SwipeRefreshLayout) view.findViewById(R.id.swlayout);
        llayout = (LinearLayout) view.findViewById(R.id.ll_swiperefresh);

        swLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorYellow, R.color.colorRed);


        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Handler untuk menjalankan jeda selama 5 detik
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        // Berhenti berputar/refreshing
                        swLayout.setRefreshing(false);

                        // fungsi-fungsi lain yang dijalankan saat refresh berhenti
                        getAllTrayek();
                    }
                }, 1000);
            }
        });
        getAllTrayek();

        return view;
    }

    private void getAllTrayek() {
        load.setText("Memuat Data");
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);
        rvDaftarTrayek.setVisibility(View.INVISIBLE);

        if(((MainActivity)activity).konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<List<Trayeks>> call = client.getAllTrayek();
            call.enqueue(new Callback<List<Trayeks>>() {
                @Override
                public void onResponse(Call<List<Trayeks>> call,  Response<List<Trayeks>> response) {
                    waiting.setVisibility(View.INVISIBLE);

                    rvDaftarTrayek.setVisibility(View.VISIBLE);

                    List<Trayeks> listTrayekItem = response.body();
                    if(listTrayekItem == null){
                        load.setText("Tidak ada data");
                        Toast.makeText(getContext() , "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        load.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext() , "Data Trayek berhasil diload", Toast.LENGTH_SHORT).show();
                        trayekAdapter.setDataTrayek(new ArrayList<Trayeks>(listTrayekItem));
                    }

                }


                @Override
                public void onFailure(Call<List<Trayeks>> call, Throwable t) {
                    Log.e("error get Trayrks",t.toString());
                    waiting.setVisibility(View.INVISIBLE);
                    load.setText("Ada Kesalahan. Silakan Coba lagi");
                    Toast.makeText(getContext() , t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getContext() , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void ItemClicked(Trayeks trayek) {
        Toast.makeText(getContext(), "Item yang diklik adalah : " + trayek.getTrayekId(), Toast.LENGTH_SHORT).show();
        Intent HalteIntent = new Intent(getContext(), HalteActivity.class);
        HalteIntent.putExtra("trayek", trayek);
        startActivity(HalteIntent);
    }

}
