package com.example.tfg_alquilerherramientas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_alquilerherramientas.R;
import com.example.tfg_alquilerherramientas.modelos.Reserva;

import java.util.List;

public class RecyclerAdapterReservas extends RecyclerView.Adapter<RecyclerAdapterReservas.ViewHolder> {
    private List<Reserva> items;
    private Context context;
    private OnItemClickListener listener;

    public RecyclerAdapterReservas(Context context, List<Reserva> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewHerramienta;
        public TextView textViewEstado;
        public TextView textViewFechaInicio;
        public TextView textViewFechaFin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHerramienta = itemView.findViewById(R.id.textViewHerramientaReservaR1);
            textViewEstado = itemView.findViewById(R.id.textViewEstadoReservaR1);
            textViewFechaInicio = itemView.findViewById(R.id.textViewFechaInicioR1);
            textViewFechaFin = itemView.findViewById(R.id.textViewFechaFinR1);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Reserva item, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_misreservas, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reserva currentItem = items.get(position);

        holder.textViewHerramienta.setText(String.valueOf(currentItem.getHerramienta().getNombre()));
        if(currentItem.getEstado().equals("ACTIVA")) {
            holder.textViewEstado.setText(currentItem.getEstado()+"✅");
        } else {
            holder.textViewEstado.setText(currentItem.getEstado()+"⛔");
        }

        holder.textViewFechaInicio.setText(String.valueOf(currentItem.getFechaInicio()).substring(0,16).replace("T", " "));
        if (currentItem.getFechaFin() != null) {
            holder.textViewFechaFin.setText(String.valueOf(currentItem.getFechaFin()).substring(0,16).replace("T", " "));
        } else {
            holder.textViewFechaFin.setText("-");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentItem, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

