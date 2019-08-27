package com.anie.dara.trackmonbus_supir.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anie.dara.trackmonbus_supir.R;
import com.anie.dara.trackmonbus_supir.model.Jadwal;
import com.anie.dara.trackmonbus_supir.model.jadwal.JadwalDetail;

import java.util.ArrayList;

public class JadwalAdapter extends  RecyclerView.Adapter<JadwalAdapter.JadwalHolder> {

    private ArrayList<JadwalDetail> dataJadwal;
    Context context;

    public void setData(ArrayList<JadwalDetail> data){
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
        JadwalDetail jadwal = dataJadwal.get(position);
        Log.e("jadwal", String.valueOf(jadwal));
        holder.NoBus.setText(jadwal.getJadwal().getNoBus());
        holder.tgl.setText(jadwal.getTgl());
        holder.kapasitas.setText(Integer.toString(jadwal.getJadwal().getBuses().getKapasitas()) + " orang");
        holder.no_tnkb.setText(jadwal.getJadwal().getBuses().getNoTnkb());
        holder.namaTrayek2.setText(jadwal.getJadwal().getDetailTrayeks().getTrayeks().getTrayek());
        holder.namaHalte.setText(jadwal.getJadwal().getDetailTrayeks().getJalurs().getNamaJalur());
        holder.namaSupir.setText(jadwal.getPramugaras().getNamaPramugara());
        holder.tgl.setText(jadwal.getTgl().substring(0,10));
        holder.shift.setText("Waktu : " +jadwal.getShifts().getJamAwal()+ " - "+ jadwal.getShifts().getJamAkhir());
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
        TextView NoBus,kapasitas, tgl, namaTrayek2, namaHalte, namaSupir,no_tnkb, shift;

        public JadwalHolder(@NonNull View itemView){
            super(itemView);
            NoBus = itemView.findViewById(R.id.NoBus);
            kapasitas = itemView.findViewById(R.id.kapasitas);
            tgl = itemView.findViewById(R.id.tgl);
            no_tnkb = itemView.findViewById(R.id.no_tnkb);
            namaTrayek2 = itemView.findViewById(R.id.namaTrayek2);
            namaHalte = itemView.findViewById(R.id.namaJalur);
            namaSupir = itemView.findViewById(R.id.namaSupir);
            shift = itemView.findViewById(R.id.shift);
        }
    }



}
