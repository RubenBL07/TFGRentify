package com.example.tfg_alquilerherramientas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_alquilerherramientas.R;
import com.example.tfg_alquilerherramientas.modelos.Reserva;

import java.util.List;

public class RecyclerAdapterPagos extends RecyclerView.Adapter<RecyclerAdapterPagos.ViewHolder> {
    private final List<Reserva> listaReservas;
    private final Context context;

    public RecyclerAdapterPagos(Context context, List<Reserva> listaReservas) {
        this.context = context;
        this.listaReservas = listaReservas;
    }

    @NonNull
    @Override
    public RecyclerAdapterPagos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_pagos, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterPagos.ViewHolder holder, int position) {
        Reserva reserva = listaReservas.get(position);
        holder.textFecha.setText(String.valueOf(reserva.getFechaFin()).substring(0, 16).replace("T"," "));
        holder.textImporte.setText(reserva.getPrecioTotal() + "€");
    }

    @Override
    public int getItemCount() {
        return listaReservas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textFecha, textImporte;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textFecha = itemView.findViewById(R.id.textViewFechaImporte);
            textImporte = itemView.findViewById(R.id.textViewImporte);
        }
    }
}
