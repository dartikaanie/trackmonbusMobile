package com.anie.dara.trackmonbus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anie.dara.trackmonbus.R;
import com.anie.dara.trackmonbus.model.Pesan;

import java.util.ArrayList;

public class pesanAdapter extends RecyclerView.Adapter<pesanAdapter.PesanHolder> {

    private ArrayList<Pesan> dataPesan;
    OnItemClicked clickHandler;
    Context context;

    public void setDataPesan(ArrayList<Pesan> data){

        dataPesan = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PesanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengambil data dalam bentuk objek
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.daftar_pesan, parent, false);
        PesanHolder holder = new PesanHolder(v);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PesanHolder holder, int position) {
        Pesan pesan = dataPesan.get(position);
            holder.perihal.setText(String.valueOf(pesan.getPerihal()));
            if((String.valueOf(pesan.getIsi_keluhan())).length() <= 60){
                holder.isi_pesan.setText(String.valueOf(pesan.getIsi_keluhan()));
            }else{
                holder.isi_pesan.setText((String.valueOf(pesan.getIsi_keluhan())).substring(0,60) + "...");
            }
            holder.tgl.setText(String.valueOf(pesan.getCreated_at()));
            holder.user.setText(String.valueOf(pesan.getName()));
            holder.komentar.setText(String.valueOf(pesan.getJumlah_komentar()));
        }


    @Override
    public int getItemCount() {
        //mengembalikan jumlah data yang dimiliki
        if(dataPesan!=null){
            return dataPesan.size();
        }else {
            return 0;
        }
    }

    // Inner CLASS
    public class PesanHolder extends RecyclerView.ViewHolder{
        TextView perihal, isi_pesan, tgl, user, komentar;

        public PesanHolder(@NonNull View itemView){
            super(itemView);
            perihal = itemView.findViewById(R.id.perihal);
            isi_pesan = itemView.findViewById(R.id.isi_pesan);
            tgl = itemView.findViewById(R.id.tgl);
            user = itemView.findViewById(R.id.user);
            komentar = itemView.findViewById(R.id.komentar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pesan pesan= dataPesan.get(getAdapterPosition());
                    clickHandler.ItemClicked(pesan);
                }
            });
        }
    }

    public interface OnItemClicked{
        void ItemClicked(Pesan pesan);

    }



    public void setClickHandler(OnItemClicked clickHandler) {

        this.clickHandler = clickHandler;
    }
}
