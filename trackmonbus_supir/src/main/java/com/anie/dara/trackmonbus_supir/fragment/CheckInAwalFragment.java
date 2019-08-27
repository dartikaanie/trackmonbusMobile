package com.anie.dara.trackmonbus_supir.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anie.dara.trackmonbus_supir.MainActivity;
import com.anie.dara.trackmonbus_supir.MonitoringPosisi;
import com.anie.dara.trackmonbus_supir.R;
import com.anie.dara.trackmonbus_supir.model.jadwal.JadwalDetail;
import com.anie.dara.trackmonbus_supir.rest.dbClient;
import com.anie.dara.trackmonbus_supir.rest.ApiClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.substring;


public class CheckInAwalFragment extends Fragment implements View.OnClickListener {

    JadwalDetail jadwal;
    TextView NoBus, no_tnkb, kapasitas, namaJalur, namaSupir, namaTrayek, hari_tgl;
    Button btnMulai;
    View mView;
    EditText etKmAwal;
    String no_bus, tgl;
    private Activity activity;
    private dbClient client = ApiClient.getClient().create(dbClient.class);
    private Toolbar toolbar;
    private ImageView toolbarTitle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_check_in_awal, container, false);
        //set toolbar
        toolbar = mView.findViewById(R.id.toolbar);
        toolbarTitle = (ImageView) mView.findViewById(R.id.toolbar_title);
         ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        NoBus = mView.findViewById(R.id.NoBus);
        no_tnkb = mView.findViewById(R.id.no_tnkb);
        kapasitas = mView.findViewById(R.id.kapasitas);
        namaJalur = mView.findViewById(R.id.namaJalur);
        namaSupir = mView.findViewById(R.id.namaSupir);
        hari_tgl = mView.findViewById(R.id.hari_tgl);
        namaTrayek = mView.findViewById(R.id.namaTrayek2);
        btnMulai = mView.findViewById(R.id.mulai);
        btnMulai.setOnClickListener(this);

        etKmAwal = mView.findViewById(R.id.etKmAwal);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();

        jadwal =  bundle.getParcelable("jadwal");
        if(bundle != null){
            NoBus.setText(jadwal.getJadwal().getNoBus());
            no_tnkb.setText(jadwal.getJadwal().getBuses().getNoTnkb());
            kapasitas.setText(jadwal.getJadwal().getBuses().getKapasitas());
            namaJalur.setText(jadwal.getJadwal().getDetailTrayeks().getJalurs().getNamaJalur());
            namaTrayek.setText(jadwal.getJadwal().getDetailTrayeks().getTrayeks().getTrayek());
            namaSupir.setText(jadwal.getUsers().getName() + " - "+ jadwal.getPramugaras().getNamaPramugara());

            tgl = jadwal.getTgl();
            hari_tgl.setText(substring(jadwal.getTgl(),0,10));
            Toast.makeText(getContext() , "Silakan isi km awal bus", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateKmAwal(String no_bus, String tgl) {
        if (TextUtils.isEmpty(etKmAwal.getText().toString())) {
            etKmAwal.setError(getResources().getString(R.string.msg_cannot_allow_empty_field));
        } else {
            final ProgressDialog prog= new ProgressDialog(getActivity());//Assuming that you are using fragments.
            prog.setMessage("Menyimpan Data");
            prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prog.show();
            if (((MainActivity) activity).konekkah()) {
                Double km = Double.valueOf(etKmAwal.getText().toString());
                Log.e("km", km.toString());
                Call<ResponseBody> call = client.updateKmAwal(no_bus, tgl,km);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody s = response.body();
                        Toast.makeText(activity, "berhasil disimpan", Toast.LENGTH_LONG).show();
                        prog.dismiss();
                        Intent intent=  new Intent(getActivity(), MonitoringPosisi.class);
                        intent.putExtra("jadwal", jadwal);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        prog.dismiss();
                        Toast.makeText(activity, "gagal disimpan", Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                prog.dismiss();
                Toast.makeText(activity, "Koneksi internet anda tidak aktif", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.mulai:
                updateKmAwal(no_bus, tgl);
                break;
        }
    }
}
