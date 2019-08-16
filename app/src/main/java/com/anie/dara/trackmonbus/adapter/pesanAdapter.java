package com.anie.dara.trackmonbus.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anie.dara.trackmonbus.R;
import com.anie.dara.trackmonbus.model.Pesan;

import java.util.ArrayList;

public class pesanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Pesan> dataPesan;
    OnItemClicked clickHandler, delHandler;
    Context context;
    String user_id;

    private static int TYPE_UMUM = 1;
    private static int TYPE_USER = 2;

    public static  final String DEFAULT ="N/A";



    public pesanAdapter(Context context)
    {
        this.context= context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", DEFAULT);

    }

    public void setDataPesan(ArrayList<Pesan> data){

        dataPesan = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengambil data dalam bentuk objek
//        View v = LayoutInflater
//                .from(parent.getContext())
//                .inflate(R.layout.daftar_pesan, parent, false);
//        PesanHolder holder = new PesanHolder(v);
//        context = parent.getContext();
//        return holder;
        View view;
        if (viewType == TYPE_UMUM) { // for call layout
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daftar_pesan, parent, false);
            return new PesanUmumHolder(view);

        } else { // for email layout
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daftar_pesan_saya, parent, false);
            return new PesanUserHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_UMUM) {
            ((PesanUmumHolder) viewHolder).setDataUmumPesan(dataPesan.get(position));
        } else {
            ((PesanUserHolder) viewHolder).setDataUserPesan(dataPesan.get(position));
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (dataPesan.get(position).getUser_id().equals(user_id)) {
            return TYPE_USER;
        } else {
            return TYPE_UMUM;
        }
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
//    public class PesanHolder extends RecyclerView.ViewHolder{
//        TextView perihal, isi_pesan, tgl, user, komentar;
//
//        public PesanHolder(@NonNull View itemView){
//            super(itemView);
//            perihal = itemView.findViewById(R.id.perihal);
//            isi_pesan = itemView.findViewById(R.id.isi_pesan);
//            tgl = itemView.findViewById(R.id.tgl);
//            user = itemView.findViewById(R.id.user);
//            komentar = itemView.findViewById(R.id.komentar);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Pesan pesan= dataPesan.get(getAdapterPosition());
//                    clickHandler.ItemClicked(pesan);
//                }
//            });
//        }
//    }

    public class PesanUmumHolder extends RecyclerView.ViewHolder{
        TextView perihal, isi_pesan, tgl, user, komentar;

        public PesanUmumHolder(@NonNull View itemView){
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

        private void setDataUmumPesan(Pesan pesan) {
            perihal.setText(String.valueOf(pesan.getPerihal()));
            if((String.valueOf(pesan.getIsi_keluhan())).length() <= 60){
                isi_pesan.setText(String.valueOf(pesan.getIsi_keluhan()));
            }else{
                isi_pesan.setText((String.valueOf(pesan.getIsi_keluhan())).substring(0,60) + "...");
            }
            tgl.setText(String.valueOf(pesan.getCreated_at()));
            user.setText(String.valueOf(pesan.getName()));
            komentar.setText(String.valueOf(pesan.getJumlah_komentar()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pesan pesan= dataPesan.get(getAdapterPosition());
                    clickHandler.ItemClicked(pesan);
                }
            });
        }


    }

    public class PesanUserHolder extends RecyclerView.ViewHolder{
        TextView perihal, isi_pesan, tgl, user, komentar;
        Button btn_del;

        public PesanUserHolder(@NonNull View itemView){
            super(itemView);
            perihal = itemView.findViewById(R.id.perihal);
            isi_pesan = itemView.findViewById(R.id.isi_pesan);
            tgl = itemView.findViewById(R.id.tgl);
            user = itemView.findViewById(R.id.user);
            komentar = itemView.findViewById(R.id.komentar);
            btn_del = itemView.findViewById(R.id.btn_del);

            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pesan pesan= dataPesan.get(getAdapterPosition());
                    clickHandler.deletePesan(pesan);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pesan pesan= dataPesan.get(getAdapterPosition());
                    clickHandler.ItemClicked(pesan);
                }
            });
        }

        private void setDataUserPesan(Pesan pesan) {
            perihal.setText(String.valueOf(pesan.getPerihal()));
            if((String.valueOf(pesan.getIsi_keluhan())).length() <= 60){
                isi_pesan.setText(String.valueOf(pesan.getIsi_keluhan()));
            }else{
                isi_pesan.setText((String.valueOf(pesan.getIsi_keluhan())).substring(0,60) + "...");
            }
            tgl.setText(String.valueOf(pesan.getCreated_at()));
            user.setText(String.valueOf(pesan.getName()));
            komentar.setText(String.valueOf(pesan.getJumlah_komentar()));
        }

    }

        public interface OnItemClicked{
        void ItemClicked(Pesan pesan);
        void deletePesan(Pesan pesan);
        }



    public void setClickHandler(OnItemClicked clickHandler) {

        this.clickHandler = clickHandler;
    }
}
