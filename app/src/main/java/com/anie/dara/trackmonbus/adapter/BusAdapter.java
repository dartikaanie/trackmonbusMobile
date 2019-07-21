package com.anie.dara.trackmonbus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anie.dara.trackmonbus.R;
import com.anie.dara.trackmonbus.model.Bus;

import java.util.ArrayList;

public class BusAdapter extends  RecyclerView.Adapter<BusAdapter.BusHolder> {

    private ArrayList<Bus> dataBus;
    Context context;

    public void setDataBus(ArrayList<Bus> data){
        dataBus = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BusAdapter.BusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengambil data dalam bentuk objek
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.daftar_bus, parent, false);
        BusAdapter.BusHolder holder = new BusAdapter.BusHolder(v);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BusAdapter.BusHolder holder, int position) {
        Bus bus = dataBus.get(position);
        holder.noBus.setText(String.valueOf(bus.getNo_bus()));
        holder.no_tnkb.setText(String.valueOf(bus.getNo_tnkb()));
        holder.kapasitas.setText(String.valueOf(bus.getKapasitas() + " orang"));
    }


    @Override
    public int getItemCount() {
        //mengembalikan jumlah data yang dimiliki
        if(dataBus!=null){
            return dataBus.size();
        }else {
            return 0;
        }
    }

    // Inner CLASS
    public class BusHolder extends RecyclerView.ViewHolder{
        TextView noBus, no_tnkb, kapasitas;

        public BusHolder(@NonNull View itemView){
            super(itemView);
            noBus = itemView.findViewById(R.id.noBus);
            no_tnkb = itemView.findViewById(R.id.nama_trayek);
            kapasitas = itemView.findViewById(R.id.kapasitas);
        }
    }



}
