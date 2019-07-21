package com.anie.dara.trackmonbus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anie.dara.trackmonbus.R;
import com.anie.dara.trackmonbus.model.Komentar;

import java.util.ArrayList;

public class KomentarAdapter extends RecyclerView.Adapter<KomentarAdapter.KomentarHolder>  {

    private ArrayList<Komentar> dataKomentar;
    Context context;

    public void setDataKomentar(ArrayList<Komentar> data){

        dataKomentar= data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KomentarAdapter.KomentarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengambil data dalam bentuk objek
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.daftar_komentar, parent, false);
        KomentarAdapter.KomentarHolder holder = new KomentarAdapter.KomentarHolder(v);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull KomentarHolder holder, int position) {
        Komentar komentar = dataKomentar.get(position);
        holder.user.setText(komentar.getName());
        holder.isi_komentar.setText(komentar.getIsi_komentar());
        holder.tgl.setText(komentar.getCreated_at());
    }


    @Override
    public int getItemCount() {
        //mengembalikan jumlah data yang dimiliki
        if(dataKomentar!=null){
            return dataKomentar.size();
        }else {
            return 0;
        }
    }

    // Inner CLASS
    public class KomentarHolder extends RecyclerView.ViewHolder{
        TextView user, isi_komentar, tgl;

        public KomentarHolder(@NonNull View itemView){
            super(itemView);
            user = itemView.findViewById(R.id.user_dafKomen);
            isi_komentar = itemView.findViewById(R.id.isi_komentar);
            tgl = itemView.findViewById(R.id.tgl_komentar);


        }
    }


}
