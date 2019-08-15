package com.anie.dara.trackmonbus_supir.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anie.dara.trackmonbus_supir.R;
import com.anie.dara.trackmonbus_supir.model.Jadwal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JadwalAdapter extends  RecyclerView.Adapter<JadwalAdapter.JadwalHolder> {

    private ArrayList<Jadwal> dataJadwal;
    Context context;

    public void setData(ArrayList<Jadwal> data){
        dataJadwal = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JadwalAdapter.JadwalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengambil data dalam bentuk objek
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.daftar_jadwal, parent, false);
        JadwalAdapter.JadwalHolder holder = new JadwalHolder(v);
        context = parent.getContext();
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull JadwalAdapter.JadwalHolder holder, int position) {
        Jadwal jadwal = dataJadwal.get(position);
        holder.NoBus.setText(jadwal.getNo_bus());
        holder.kapasitas.setText(jadwal.getKapasitas() + " orang");
        holder.no_tnkb.setText(jadwal.getNo_tnkb());
        holder.namaTrayek2.setText(jadwal.getTrayek());
        holder.namaHalte.setText(jadwal.getNama_halte());
        holder.namaSupir.setText(jadwal.getNama_supir());

        Date date1= null;
        try {
            date1 = new SimpleDateFormat("Y-m-d").parse(jadwal.getTgl());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat = new SimpleDateFormat("d-m-Y");
        String strDate = dateFormat.format(date1);
        holder.tgl.setText(jadwal.getTgl().substring(0,10));
    }


    @Override
    public int getItemCount() {
        //mengembalikan jumlah data yang dimiliki
        if(dataJadwal!=null){
            return dataJadwal.size();
        }else {
            return 0;
        }
    }

    // Inner CLASS
    public class JadwalHolder extends RecyclerView.ViewHolder{
        TextView NoBus,kapasitas, tgl, namaTrayek2, namaHalte, namaSupir,no_tnkb;

        public JadwalHolder(@NonNull View itemView){
            super(itemView);
            NoBus = itemView.findViewById(R.id.NoBus);
            kapasitas = itemView.findViewById(R.id.kapasitas);
            tgl = itemView.findViewById(R.id.tgl);
            no_tnkb = itemView.findViewById(R.id.no_tnkb);
            namaTrayek2 = itemView.findViewById(R.id.namaTrayek2);
            namaHalte = itemView.findViewById(R.id.namaHalte);
            namaSupir = itemView.findViewById(R.id.namaSupir);
        }
    }



}
