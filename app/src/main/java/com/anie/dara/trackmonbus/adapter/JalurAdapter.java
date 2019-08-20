package com.anie.dara.trackmonbus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anie.dara.trackmonbus.R;
import com.anie.dara.trackmonbus.model.Trayeks.JalurItem;

import java.util.List;

public class JalurAdapter extends RecyclerView.Adapter<JalurAdapter.JalurHolder> {

    private List<JalurItem> dataJalur;
    Context context;
    jalurClick jalurListener;

    public JalurAdapter(List<JalurItem> data, Context context) {
        dataJalur = data;
        notifyDataSetChanged();
    }


    public void setJalurList(List<JalurItem> data){
        this.dataJalur.clear();
        this.dataJalur.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JalurAdapter.JalurHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengambil data dalam bentuk objek
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.daftar_jalur, parent, false);
        JalurAdapter.JalurHolder holder = new JalurAdapter.JalurHolder(v);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull JalurAdapter.JalurHolder holder, int position) {
        JalurItem jalur = dataJalur.get(position);
            holder.awal.setText(String.valueOf(jalur.getAwal()));
            holder.akhir.setText(String.valueOf(jalur.getAkhir()));
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

    // Inner CLASS
    public class JalurHolder extends RecyclerView.ViewHolder{
        TextView awal, akhir;
        CardView cardViewJalur;

        public JalurHolder(@NonNull View itemView){
            super(itemView);
            awal = itemView.findViewById(R.id.awal);
            akhir = itemView.findViewById(R.id.akhir);
            cardViewJalur = itemView.findViewById(R.id.cardViewJalur);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JalurItem jalurItem = dataJalur.get(getAdapterPosition());
                    if(jalurListener != null){
                        jalurListener.OnKlikJalur(jalurItem);
                    }
                }
            });
        }
    }

    public interface jalurClick{
        void OnKlikJalur(JalurItem jalurItem);
    }

    public void SetClickJalur(jalurClick jalurListener){
        this.jalurListener = jalurListener;

    }



}
