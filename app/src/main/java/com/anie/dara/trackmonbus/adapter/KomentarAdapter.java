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
import com.anie.dara.trackmonbus.model.Komentar;

import java.util.ArrayList;

public class KomentarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private ArrayList<Komentar> dataKomentar;
    Context context;

    String user_id;
    OnItemClicked clickHandler;

    private static int TYPE_UMUM = 1;
    private static int TYPE_USER = 2;

    public static  final String DEFAULT ="N/A";

    public KomentarAdapter(Context context) {
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", DEFAULT);
    }

    public void setDataKomentar(ArrayList<Komentar> data){

        dataKomentar= data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == TYPE_UMUM) { // for call layout
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daftar_komentar, parent, false);
            return new KomentarAdapter.KomentarUmumHolder(view);

        } else { // for email layout
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daftar_komentar_saya, parent, false);
            return new KomentarAdapter.KomentarUserHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_UMUM) {
            ((KomentarAdapter.KomentarUmumHolder) holder).setDataUmumKomentar(dataKomentar.get(position));
        } else {
            ((KomentarAdapter.KomentarUserHolder) holder).setDataUserKomentar(dataKomentar.get(position));
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (dataKomentar.get(position).getUser_id().equals(user_id)) {
            return TYPE_USER;
        } else {
            return TYPE_UMUM;
        }
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
    public class KomentarUmumHolder extends RecyclerView.ViewHolder{
            TextView user, isi_komentar, tgl;

            public KomentarUmumHolder(@NonNull View itemView){
                super(itemView);
                user = itemView.findViewById(R.id.user_dafKomen);
                isi_komentar = itemView.findViewById(R.id.isi_komentar);
                tgl = itemView.findViewById(R.id.tgl_komentar);
            }

        private void setDataUmumKomentar(Komentar komentar) {
            user.setText(String.valueOf(komentar.getName()));
            isi_komentar.setText(komentar.getIsi_komentar());
            tgl.setText(komentar.getCreated_at());
        }
    }

        public class KomentarUserHolder extends RecyclerView.ViewHolder{
            TextView user, isi_komentar, tgl;
            Button btn_del;

            public KomentarUserHolder(@NonNull View itemView){
                super(itemView);
                user = itemView.findViewById(R.id.user_dafKomen);
                isi_komentar = itemView.findViewById(R.id.isi_komentar);
                tgl = itemView.findViewById(R.id.tgl_komentar);
                btn_del = itemView.findViewById(R.id.btn_del);

                btn_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Komentar komentar= dataKomentar.get(getAdapterPosition());
                        clickHandler.ItemClicked(komentar);
                    }
                });
            }

            private void setDataUserKomentar(Komentar komentar) {
                user.setText(komentar.getName());
                isi_komentar.setText(komentar.getIsi_komentar());
                tgl.setText(komentar.getCreated_at());
            }
        }

    public interface OnItemClicked{
        void ItemClicked(Komentar komentar);

    }



    public void setClickHandler(KomentarAdapter.OnItemClicked clickHandler) {

        this.clickHandler = clickHandler;
    }
}
