package com.anie.dara.trackmonbus_supir.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.anie.dara.trackmonbus_supir.MainActivity;
import com.anie.dara.trackmonbus_supir.R;
import com.anie.dara.trackmonbus_supir.adapter.JadwalAdapter;
import com.anie.dara.trackmonbus_supir.model.jadwal.JadwalDetail;
import com.anie.dara.trackmonbus_supir.rest.ApiClient;
import com.anie.dara.trackmonbus_supir.rest.dbClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JadwalFragment extends Fragment {

    View mView;
    private Toolbar toolbar;
    private ImageView toolbarTitle;

    static RecyclerView rvDaftarJadwal;
    static JadwalAdapter jadwalAdapter;
    String user_id;
    private String DEFAULT = "tidak aktif";
    private dbClient client = ApiClient.getClient().create(dbClient.class);
    static Activity activity;

    SwipeRefreshLayout swLayout;
    LinearLayout llayout;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_jadwal, container, false);


        //set toolbar
        toolbar = mView.findViewById(R.id.toolbar);
        toolbarTitle = (ImageView) mView.findViewById(R.id.toolbar_title);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }



        jadwalAdapter = new JadwalAdapter();

        rvDaftarJadwal = mView.findViewById(R.id.rvListJadwal);
        rvDaftarJadwal.setAdapter(jadwalAdapter);
        RecyclerView.LayoutManager layoutManager;

        rvDaftarJadwal.setVisibility(View.INVISIBLE);
        layoutManager = new LinearLayoutManager(getContext());

        rvDaftarJadwal.setLayoutManager(layoutManager);
        rvDaftarJadwal.setHasFixedSize(true);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", DEFAULT);
        GetJadwal(user_id);


        // Inisialisasi SwipeRefreshLayout
        swLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swlayout);

        llayout = (LinearLayout) mView.findViewById(R.id.ll_swiperefresh);
        swLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);

        // Mengeset listener yang akan dijalankan saat layar di refresh/swipe
        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetJadwal(user_id);
                swLayout.setRefreshing(false);
            }
        });

        return mView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void GetJadwal(String user_id){
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Memuat Data . . .");
        dialog.show();

        if(((MainActivity)activity).konekkah()){
            client = ApiClient.getClient().create(dbClient.class);
            Call<List<JadwalDetail>> call = client.getJadwalUser(user_id);
            call.enqueue(new Callback<List<JadwalDetail>>() {

                @Override
                public void onResponse(Call<List<JadwalDetail>> call, Response<List<JadwalDetail>> response) {
                    rvDaftarJadwal.setVisibility(View.VISIBLE);
                    List<JadwalDetail> listItem = response.body();
                                    Toast.makeText(getContext() , "Jadwal berhasil diload", Toast.LENGTH_SHORT).show();
                    jadwalAdapter.setData(new ArrayList<JadwalDetail>(listItem));
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<JadwalDetail>> call, Throwable t) {
                    Log.e("jadwal error", t.toString());
                    dialog.dismiss();
                }
            });
        }else{
            Toast.makeText(getContext() , "Hidupkan koneksi internet anda", Toast.LENGTH_SHORT).show();
        }
    }

}
