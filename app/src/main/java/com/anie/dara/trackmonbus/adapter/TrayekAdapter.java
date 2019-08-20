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
import com.anie.dara.trackmonbus.model.Trayeks.Trayeks;

import java.util.ArrayList;

public class TrayekAdapter extends RecyclerView.Adapter<TrayekAdapter.TrayekHolder> {

    private ArrayList<Trayeks> dataTrayek;
    Context context;
    TrayekAdapter.OnItemClicked clickHandler;
    private JalurAdapter jalurAdapter;

    public void setDataTrayek(ArrayList<Trayeks> data){
        dataTrayek = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrayekAdapter.TrayekHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengambil data dalam bentuk objek
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.daftar_trayek, parent, false);
        TrayekAdapter.TrayekHolder holder = new TrayekAdapter.TrayekHolder(v);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrayekAdapter.TrayekHolder holder, int position) {
        Trayeks trayek = dataTrayek.get(position);
        holder.trayek.setText(String.valueOf(trayek.getTrayek()));
        holder.km_rit.setText(String.valueOf(trayek.getKmRit() + " km"));

        if(trayek.getJalur().size() != 0){
            holder.jalurText.setVisibility(View.VISIBLE);
            jalurAdapter = new JalurAdapter(trayek.getJalur(), context);
            holder.rvJalur.setAdapter(jalurAdapter);
        }
    }


    @Override
    public int getItemCount() {
        //mengembalikan jumlah data yang dimiliki
        if(dataTrayek!=null){
            return dataTrayek.size();
        }else {
            return 0;
        }
    }

    // Inner CLASS
    private RecyclerView rvJalur;
    public class TrayekHolder extends RecyclerView.ViewHolder{
        TextView trayek, km_rit, jalurText;
        RecyclerView rvJalur;
        private JalurAdapter jalurAdpater;


        public TrayekHolder(@NonNull View itemView){
            super(itemView);
            trayek = itemView.findViewById(R.id.nama_trayek);
            km_rit = itemView.findViewById(R.id.km_rit);
            rvJalur = itemView.findViewById(R.id.rvJalur);
            jalurText = itemView.findViewById(R.id.jalurText);

            jalurText.setVisibility(View.INVISIBLE);

            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            rvJalur.setLayoutManager(linearLayoutManager);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Trayeks trayek= dataTrayek.get(getAdapterPosition());
                    clickHandler.ItemClicked(trayek);
                }
            });
        }

    }

    public interface OnItemClicked{
        void ItemClicked(Trayeks trayek);

    }



    public void setClickHandler(TrayekAdapter.OnItemClicked clickHandler) {

        this.clickHandler = clickHandler;
    }

}
