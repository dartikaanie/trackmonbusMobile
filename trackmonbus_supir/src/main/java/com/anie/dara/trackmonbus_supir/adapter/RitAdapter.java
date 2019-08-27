package com.anie.dara.trackmonbus_supir.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anie.dara.trackmonbus_supir.R;
import com.anie.dara.trackmonbus_supir.model.Rit;

import java.util.ArrayList;

public class RitAdapter extends  RecyclerView.Adapter<RitAdapter.RitHolder> {

    private ArrayList<Rit> dataRit;
    Context context;

    public void setDataRit(ArrayList<Rit> data){
        dataRit = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RitAdapter.RitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengambil data dalam bentuk objek
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.daftar_rit, parent, false);
        RitAdapter.RitHolder holder = new RitHolder(v);
        context = parent.getContext();
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull RitAdapter.RitHolder holder, int position) {
        Rit rit = dataRit.get(position);
        holder.waktu_berangkat.setText(String.valueOf(rit.getWaktuBerangkat()));

        if(rit.getWaktuDatang() == null){
            holder.waktu_datang.setText("-");
            holder.waktu_datang.setGravity(2);
        }else{
            holder.waktu_datang.setText(String.valueOf(rit.getWaktuDatang()));
        }
        holder.namaJalur.setText(String.valueOf(rit.getDetailTrayeks().getJalurs().getNamaJalur()));
    }


    @Override
    public int getItemCount() {
        //mengembalikan jumlah data yang dimiliki
        if(dataRit!=null){
            return dataRit.size();
        }else {
            return 0;
        }
    }

    // Inner CLASS
    public class RitHolder extends RecyclerView.ViewHolder{
        TextView waktu_berangkat, waktu_datang, namaJalur;

        public RitHolder(@NonNull View itemView){
            super(itemView);
            waktu_berangkat = itemView.findViewById(R.id.waktu_berangkat);
            waktu_datang = itemView.findViewById(R.id.waktu_datang);
            namaJalur = itemView.findViewById(R.id.namaJalur);
        }
    }



}
