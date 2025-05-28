package com.example.tfg_alquilerherramientas.adapters;

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
        public TextView textViewId;
        public TextView textViewHerramienta;
        public TextView textViewEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.textViewIdReservaR1);
            textViewHerramienta = itemView.findViewById(R.id.textViewHerramientaReservaR1);
            textViewEstado = itemView.findViewById(R.id.textViewEstadoReservaR1);
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reserva currentItem = items.get(position);

        holder.textViewId.setText(String.valueOf(currentItem.getId()));
        holder.textViewHerramienta.setText(String.valueOf(currentItem.getHerramienta().getNombre()));
        holder.textViewEstado.setText(currentItem.getEstado());

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

