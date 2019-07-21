package com.anie.dara.trackmonbus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.anie.dara.trackmonbus.R;
import com.anie.dara.trackmonbus.model.Trayek;

import java.util.ArrayList;

public class TrayekAdapter extends RecyclerView.Adapter<TrayekAdapter.TrayekHolder> {

    private ArrayList<Trayek> dataTrayek;
    Context context;
    TrayekAdapter.OnItemClicked clickHandler;

    public void setDataTrayek(ArrayList<Trayek> data){
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
        Trayek trayek = dataTrayek.get(position);
        holder.trayek.setText(String.valueOf(trayek.getTrayek()));
        holder.km_rit.setText(String.valueOf(trayek.getKm_rit() + " km"));
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
    public class TrayekHolder extends RecyclerView.ViewHolder{
        TextView trayek_id, trayek, km_rit;

        public TrayekHolder(@NonNull View itemView){
            super(itemView);
            trayek = itemView.findViewById(R.id.nama_trayek);
            km_rit = itemView.findViewById(R.id.km_rit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Trayek trayek= dataTrayek.get(getAdapterPosition());
                    clickHandler.ItemClicked(trayek);
                }
            });
        }
    }

    public interface OnItemClicked{
        void ItemClicked(Trayek trayek);

    }



    public void setClickHandler(TrayekAdapter.OnItemClicked clickHandler) {

        this.clickHandler = clickHandler;
    }

}
