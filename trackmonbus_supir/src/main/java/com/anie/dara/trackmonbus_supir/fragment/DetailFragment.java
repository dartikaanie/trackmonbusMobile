package com.anie.dara.trackmonbus_supir.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anie.dara.trackmonbus_supir.R;
import com.anie.dara.trackmonbus_supir.model.Jadwal;

import static android.text.TextUtils.substring;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    Jadwal jadwal;
    View mView;
    Bundle bundle;
    TextView NoBus, no_tnkb, kapasitas, namaHalte, namaSupir, namaTrayek, hari_tgl, km_awal, km_akhir, keterangan;
    Button btnPeta, btnUbah;

    private Toolbar toolbar;
        private ImageView toolbarTitle;

    public DetailFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment

            //set toolbar
            toolbar = mView.findViewById(R.id.toolbar);
            toolbarTitle = (ImageView) mView.findViewById(R.id.toolbar_title);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            if (toolbar != null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            }

        mView =  inflater.inflate(R.layout.fragment_detail, container, false);
        NoBus = mView.findViewById(R.id.NoBus);
        no_tnkb = mView.findViewById(R.id.no_tnkb);
        kapasitas = mView.findViewById(R.id.kapasitas);
        namaHalte = mView.findViewById(R.id.namaHalte);
        namaSupir = mView.findViewById(R.id.namaSupir);
        hari_tgl = mView.findViewById(R.id.tgl);
        namaTrayek = mView.findViewById(R.id.namaTrayek2);
        km_awal = mView.findViewById(R.id.TVkm_awal);
        km_akhir = mView.findViewById(R.id.TVKM_akhir);
        keterangan = mView.findViewById(R.id.TVKeterangan);
        btnPeta = mView.findViewById(R.id.btn_peta);
        btnPeta.setOnClickListener(this);
        btnUbah = mView.findViewById(R.id.btn_ubah);
        btnUbah.setOnClickListener(this);


        return  mView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = this.getArguments();
        Log.e("jkl", String.valueOf(bundle));
        jadwal =  bundle.getParcelable("jadwal");

        if(bundle != null){
            NoBus.setText(jadwal.getNo_bus());
            no_tnkb.setText(jadwal.getNo_tnkb());
            kapasitas.setText(jadwal.getKapasitas());
            namaHalte.setText(jadwal.getNama_halte());
            namaSupir.setText(jadwal.getNama_supir());
            namaTrayek.setText(jadwal.getTrayek());
            hari_tgl.setText(substring(jadwal.getTgl(),0,10));
            km_akhir.setText(jadwal.getKm_akhir());
            km_awal.setText(jadwal.getKm_awal());
            keterangan.setText(jadwal.getKeterangan());
        }else{
            Fragment moveFragment = new BerandaFragment();
            moveFragment.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container, moveFragment)
                    .commit();

        }
    }


    public void moveFragment(Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putParcelable("jadwal", this.jadwal);
        fragment.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_peta:
                moveFragment(new MonitorPosisiFragment());
                break;
            case R.id.btn_ubah:
                moveFragment(new UbahDataJadwalFragment());
                break;

        }
    }
}
