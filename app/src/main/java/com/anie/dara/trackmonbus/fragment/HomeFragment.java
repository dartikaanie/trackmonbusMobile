package com.anie.dara.trackmonbus.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.anie.dara.trackmonbus.MainActivity;
import com.anie.dara.trackmonbus.MypesanActivity;
import com.anie.dara.trackmonbus.PesanActivity;
import com.anie.dara.trackmonbus.PilihJalurActivity;
import com.anie.dara.trackmonbus.R;
import com.anie.dara.trackmonbus.TrayekActivity;
import com.anie.dara.trackmonbus.adapter.pesanAdapter;
import com.anie.dara.trackmonbus.model.Pesan;
import com.anie.dara.trackmonbus.rest.ApiClient;
import com.anie.dara.trackmonbus.rest.dbClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements pesanAdapter.OnItemClicked, View.OnClickListener {

    private static final String DEFAULT = "N/A";
    static RecyclerView rvDaftarPesan;
    static pesanAdapter pesanAdapter;
    static ProgressBar waiting;
    static TextView load;
    static Button BtnlihatAll, BtnBus, BtnPesan, BtnLokasi;
    static ImageView noData;
    static int status_data;
    static Pesan dataPesan;
    private static dbClient client;
    ArrayList<Pesan> pesanList;
    static Activity activity;

    private Toolbar toolbar;
    private ImageView toolbarTitle;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
        dataPesan = new Pesan();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbarTitle = (ImageView) view.findViewById(R.id.toolbar_title);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        waiting=view.findViewById(R.id.progressBar);
        noData = view.findViewById(R.id.noData);
        load = view.findViewById(R.id.load);
        //button bus
        BtnBus = view.findViewById(R.id.btnBus);
        BtnBus.setOnClickListener(this);

        //btn halte
        BtnPesan = view.findViewById(R.id.BtnPesan);
        BtnPesan.setOnClickListener(this);

        //button Pesan
        BtnlihatAll = view.findViewById(R.id.BtnlihatAll);
        BtnlihatAll.setOnClickListener(this);

        BtnLokasi = view.findViewById(R.id.BtnLokasi);
        BtnLokasi.setOnClickListener(this);

        pesanAdapter = new pesanAdapter(getContext());
        pesanAdapter.setClickHandler(this);

        rvDaftarPesan = view.findViewById(R.id.daftar_pesan);
        rvDaftarPesan.setAdapter(pesanAdapter);
        RecyclerView.LayoutManager layoutManager;

        rvDaftarPesan.setVisibility(view.INVISIBLE);
        waiting.setVisibility(view.VISIBLE);
        load.setVisibility(View.VISIBLE);
        BtnlihatAll.setVisibility(View.INVISIBLE);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvDaftarPesan.setLayoutManager(layoutManager);
        rvDaftarPesan.setHasFixedSize(true);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", DEFAULT);
        Log.e("user_id", user_id);


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
                        Toast.makeText(activity, "Data berhasil diload", Toast.LENGTH_SHORT).show();
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
//        Toast.makeText(activity, "Item yang diklik adalah : " + pesan.getKeluhan_id(), Toast.LENGTH_SHORT).show();
        Intent detailIntent = new Intent(activity, DetailPesanActivity.class);
        detailIntent.putExtra("key_pesan_parcelable", pesan);
        startActivity(detailIntent);
    }

    @Override
    public void deletePesan(final Pesan pesan) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder
                .setMessage("Apakah Anda yakin untuk Menghapus Data ini ?")
                .setIcon(R.drawable.trans)
                .setCancelable(false)
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyData", Context.MODE_PRIVATE);

                        final ProgressDialog dialog123 = new ProgressDialog(getContext());
                        dialog123.setMessage("Menghapus Komentar. . .");
                        dialog123.show();
                        Call<ResponseBody>  call = client.hapusPesan(pesan.getKeluhan_id());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                ResponseBody result = response.body();
                                if(result != null){
                                    Toast.makeText(getContext(), "Data dihapus", Toast.LENGTH_SHORT).show();
                                    dialog123.dismiss();
                                    getPesan();
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                dialog123.dismiss();
                                Toast.makeText(getContext(),"Error. Ulangi lagi" + t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Batal", null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBus:
                startActivity(new Intent(getActivity(), BusActivity.class));
                break;

            case R.id.BtnPesan:
                startActivity(new Intent(getActivity(), MypesanActivity.class));
                break;

            case R.id.BtnLokasi:
                startActivity(new Intent(getActivity(), TrayekActivity.class));
                break;

            case R.id.BtnlihatAll :
                Intent intent = new Intent(getActivity(), PesanActivity.class);
                startActivity(intent);
            break;

        }
    }
}
