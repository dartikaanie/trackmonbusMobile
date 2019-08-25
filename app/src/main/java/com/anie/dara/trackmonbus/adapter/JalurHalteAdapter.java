package com.anie.dara.trackmonbus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anie.dara.trackmonbus.R;
import com.anie.dara.trackmonbus.model.Trayeks.HalteItem;
import com.anie.dara.trackmonbus.model.Trayeks.JalurItem;

import java.util.ArrayList;

public class JalurHalteAdapter extends RecyclerView.Adapter<JalurHalteAdapter.JalurHalteHolder> implements  HalteAdapter.OnHalteListener {

    private ArrayList<JalurItem> dataJalur;
    Context context;
    private HalteAdapter halteAdapter;
    Onlistener onListener;

    public void setDataJalur(ArrayList<JalurItem> data){
        dataJalur = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JalurHalteAdapter.JalurHalteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengambil data dalam bentuk objek
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.daftar_jalur_halte, parent, false);
        JalurHalteAdapter.JalurHalteHolder holder = new JalurHalteAdapter.JalurHalteHolder(v);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull JalurHalteAdapter.JalurHalteHolder holder, int position) {
        JalurItem jalur = dataJalur.get(position);
        holder.jalur.setText(String.valueOf(jalur.getAwal() + " - " + jalur.getAkhir()));


        if(jalur.getHalte().size() > 0){
            halteAdapter = new HalteAdapter(jalur.getHalte(), this);
            holder.rvListHalte.setAdapter(halteAdapter);
            halteAdapter.setClickHandler(new HalteAdapter.OnHalteListener() {
                @Override
                public void onHalteClick(HalteItem halteItem) {
                    onListener.HalteClickItem(halteItem);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        //mengembalikan jumlah data yang dimiliki
        if(dataJalur!=null){
            return dataJalur.size();
        }else {
            return 0;
        }
    }

    public interface Onlistener{
        void HalteClickItem(HalteItem halteItem);
    }

    public void setClickHandler(Onlistener onListener) {

        this.onListener = onListener;
    }

    @Override
    public void onHalteClick(HalteItem halteItem) {

    }


    // Inner CLASS
    public class JalurHalteHolder extends RecyclerView.ViewHolder{
        TextView jalur;
        RecyclerView rvListHalte;


        public JalurHalteHolder(@NonNull View itemView){
            super(itemView);
            jalur = itemView.findViewById(R.id.jalur);
            rvListHalte = itemView.findViewById(R.id.rvListHalte);

            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            rvListHalte.setLayoutManager(linearLayoutManager);
        }
    }





}
