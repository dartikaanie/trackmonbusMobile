package com.anie.dara.trackmonbus_supir.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anie.dara.trackmonbus_supir.R;
import com.anie.dara.trackmonbus_supir.model.rit;

import java.util.ArrayList;

public class RitAdapter extends  RecyclerView.Adapter<RitAdapter.RitHolder> {

    private ArrayList<rit> dataRit;
    Context context;

    public void setDataRit(ArrayList<rit> data){
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
        rit rit = dataRit.get(position);
        holder.no_rit.setText(String.valueOf(rit.getNo_rit()));
        holder.start_awal.setText(String.valueOf(rit.getStrat_awal()));
        holder.end_akhir.setText(String.valueOf(rit.getEnd_akhir()));
        holder.end_awal.setText(String.valueOf(rit.getEnd_awal()));
        holder.start_akhir.setText(String.valueOf(rit.getStart_akhir()));
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
        TextView no_rit, start_awal, end_akhir, start_akhir, end_awal;

        public RitHolder(@NonNull View itemView){
            super(itemView);
            no_rit = itemView.findViewById(R.id.no_rit);
            start_awal = itemView.findViewById(R.id.start_awal);
            end_akhir = itemView.findViewById(R.id.end_akhir);
            start_akhir = itemView.findViewById(R.id.end_awal);
            end_awal = itemView.findViewById(R.id.start_akhir);
        }
    }



}
