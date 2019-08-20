package com.anie.dara.trackmonbus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anie.dara.trackmonbus.R;
import com.anie.dara.trackmonbus.model.Trayeks.HalteItem;

import java.util.List;

public class HalteAdapter extends  RecyclerView.Adapter<HalteAdapter.HalteHolder> {

    private List<HalteItem> dataHalte;
    Context context;
    OnHalteListener halteListener;


    public HalteAdapter(List<HalteItem> data, OnHalteListener listener) {
        dataHalte = data;
        this.halteListener = listener;
        notifyDataSetChanged();
    }


    public void setDataHalte(List<HalteItem> data){
        dataHalte = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HalteAdapter.HalteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengambil data dalam bentuk objek
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.daftar_halte, parent, false);
        HalteAdapter.HalteHolder holder = new HalteAdapter.HalteHolder(v, halteListener);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HalteHolder holder, int position) {
        final HalteItem halte = dataHalte.get(position);
        holder.namaHalte.setText(String.valueOf(halte.getNama()));
        holder.lat_lng.setText(String.valueOf(halte.getLat() + " , " + halte.getLng()));

    }


    @Override
    public int getItemCount() {
        //mengembalikan jumlah data yang dimiliki
        if (dataHalte != null) {
            return dataHalte.size();
        } else {
            return 0;
        }
    }

    // Inner CLASS
    public class HalteHolder extends RecyclerView.ViewHolder {
        TextView namaHalte, lat_lng;
        private OnHalteListener halteListener;

        public HalteHolder(@NonNull View itemView, final OnHalteListener halteListener) {
            super(itemView);
            namaHalte = itemView.findViewById(R.id.nama_trayek);
            lat_lng = itemView.findViewById(R.id.lat_lng);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(halteListener != null){
                        HalteItem item = dataHalte.get(getAdapterPosition());
                        halteListener.onHalteClick(item);
                    }

                }
            });

        }
    }

    public interface OnHalteListener {

    void onHalteClick(HalteItem halteItem);
}

    public void setClickHandler(HalteAdapter.OnHalteListener halteListener) {

        this.halteListener = halteListener;
    }

}
