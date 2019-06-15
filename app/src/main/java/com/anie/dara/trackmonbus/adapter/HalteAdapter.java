package com.anie.dara.trackmonbus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anie.dara.trackmonbus.R;
import com.anie.dara.trackmonbus.model.Halte;

import java.util.ArrayList;

public class HalteAdapter extends  RecyclerView.Adapter<HalteAdapter.HalteHolder>{

    private ArrayList<Halte> dataHalte;
    Context context;

    public void setDataHalte(ArrayList<Halte> data){
        dataHalte = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HalteAdapter.HalteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengambil data dalam bentuk objek
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.daftar_halte, parent, false);
        HalteAdapter.HalteHolder holder = new HalteAdapter.HalteHolder(v);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HalteAdapter.HalteHolder holder, int position) {
        Halte halte = dataHalte.get(position);
        holder.namaHalte.setText(String.valueOf(halte.getNama()));
        holder.lat_lng.setText(String.valueOf(halte.getLat() +" , " + halte.getLng()));
    }


    @Override
    public int getItemCount() {
        //mengembalikan jumlah data yang dimiliki
        if(dataHalte!=null){
            return dataHalte.size();
        }else {
            return 0;
        }
    }

    // Inner CLASS
    public class HalteHolder extends RecyclerView.ViewHolder{
        TextView namaHalte, lat_lng;

        public HalteHolder(@NonNull View itemView){
            super(itemView);
            namaHalte = itemView.findViewById(R.id.nama_halte);
            lat_lng = itemView.findViewById(R.id.lat_lng);
        }
    }

}
