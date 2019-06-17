package com.anie.dara.trackmonbus.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus.BusActivity;
import com.anie.dara.trackmonbus.DetailPesanActivity;
import com.anie.dara.trackmonbus.HalteActivity;
import com.anie.dara.trackmonbus.MainActivity;
import com.anie.dara.trackmonbus.PesanActivity;
import com.anie.dara.trackmonbus.R;
import com.anie.dara.trackmonbus.RegistrasiActivity;
import com.anie.dara.trackmonbus.adapter.pesanAdapter;
import com.anie.dara.trackmonbus.dbClient;
import com.anie.dara.trackmonbus.model.Pesan;
import com.anie.dara.trackmonbus.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.anie.dara.trackmonbus.MainActivity.*;

public class HomeFragment extends Fragment implements pesanAdapter.OnItemClicked{

    static RecyclerView rvDaftarPesan;
    static pesanAdapter pesanAdapter;
    static ProgressBar waiting;
    static TextView load;
    static Button BtnlihatAll, BtnBus, BtnHalte;
    static ImageView noData;
    static int status_data;
    static Pesan dataPesan;
    private static dbClient client;
    ArrayList<Pesan> pesanList;
    static Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
        dataPesan = new Pesan();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        waiting=view.findViewById(R.id.progressBar);
        noData = view.findViewById(R.id.noData);
        load = view.findViewById(R.id.load);
        //button bus
        BtnBus = view.findViewById(R.id.btnBus);
        BtnBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BusActivity.class));
            }
        });

        //btn halte
        BtnHalte = view.findViewById(R.id.BtnHalte);
        BtnHalte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HalteActivity.class));
            }
        });

        //button Pesan
        BtnlihatAll = view.findViewById(R.id.BtnlihatAll);
        BtnlihatAll.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PesanActivity.class);
                startActivity(intent);
            }
        }   );

        pesanAdapter = new pesanAdapter();
        pesanAdapter.setClickHandler(this);

        rvDaftarPesan = view.findViewById(R.id.daftar_pesan);
        rvDaftarPesan.setAdapter(pesanAdapter);
        RecyclerView.LayoutManager layoutManager;

        rvDaftarPesan.setVisibility(view.INVISIBLE);
        waiting.setVisibility(view.VISIBLE);
        load.setVisibility(View.VISIBLE);
        BtnlihatAll.setVisibility(View.INVISIBLE);

        status_data=0;

        int orientasi = getResources().getConfiguration().orientation;
        if(orientasi == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        }else{
            layoutManager = new GridLayoutManager(getContext(),2);

        }
        rvDaftarPesan.setLayoutManager(layoutManager);
        rvDaftarPesan.setHasFixedSize(true);

        getPesan();
        return view;
    }

    public static void getPesan()  {

        load.setText("Mengambil Data");
        waiting.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);
        rvDaftarPesan.setVisibility(View.INVISIBLE);
        noData.setVisibility(View.INVISIBLE);

        if(((MainActivity)activity).konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<List<Pesan>> call = client.getAllPesan();
            call.enqueue(new Callback<List<Pesan>>() {
                @Override
                public void onResponse(Call<List<Pesan>> call,  Response<List<Pesan>> response) {
                    waiting.setVisibility(View.INVISIBLE);
                    load.setVisibility(View.INVISIBLE);
                    rvDaftarPesan.setVisibility(View.VISIBLE);

                    Toast.makeText(activity, "Berhasil", Toast.LENGTH_SHORT).show();

                    List<Pesan> listPesanItem = response.body();
                    if(listPesanItem == null){
                        Toast.makeText(activity, "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(activity, "Pesan berhasil diload", Toast.LENGTH_SHORT).show();
                        pesanAdapter.setDataPesan(new ArrayList<Pesan>(listPesanItem));
                        BtnlihatAll.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<List<Pesan>> call, Throwable t) {
                    load.setText(t.toString());
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(activity, "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void ItemClicked(Pesan pesan) {
        Toast.makeText(activity, "Item yang diklik adalah : " + pesan.getKeluhan_id(), Toast.LENGTH_SHORT).show();
        Intent detailIntent = new Intent(activity, DetailPesanActivity.class);
        detailIntent.putExtra("key_foto_parcelable", pesan);
        startActivity(detailIntent);
    }


}
