package com.example.tfg_alquilerherramientas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfg_alquilerherramientas.R;
import com.example.tfg_alquilerherramientas.modelos.Herramienta;

import java.util.List;

public class RecyclerAdapterHerramientas extends RecyclerView.Adapter<RecyclerAdapterHerramientas.ViewHolder> {
    private List<Herramienta> items;
    private Context context;
    private OnItemClickListener listener;

    public RecyclerAdapterHerramientas(Context context, List<Herramienta> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNombre;
        public TextView textViewCategoria;
        public TextView textViewPrecio;
        public TextView textViewEstado;
        public ImageView imageViewHerramienta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombreHerramientaR2);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoriaHerramientaR2);
            textViewPrecio = itemView.findViewById(R.id.textViewPrecioHerramientaR2);
            textViewEstado = itemView.findViewById(R.id.textViewEstadoHerramientaR2);
            imageViewHerramienta = itemView.findViewById(R.id.imageViewHerramientaR2);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Herramienta item, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_herramientas, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Herramienta currentItem = items.get(position);

        holder.textViewNombre.setText(currentItem.getNombre());
        holder.textViewCategoria.setText(String.valueOf(currentItem.getCategoria()));
        holder.textViewPrecio.setText(currentItem.getPrecioDia()+"€/día");
        holder.textViewEstado.setText(currentItem.getDisponible()?"Disponible ✅":"Reservada 🚫");

        if (currentItem.getImagenUrl() != null && !currentItem.getImagenUrl().isEmpty()) {
            Glide.with(context)
                    .load(currentItem.getImagenUrl())
                    .placeholder(R.drawable.logo_background)
                    .error(R.drawable.logo_background)
                    .into(holder.imageViewHerramienta);
        } else {
            holder.imageViewHerramienta.setImageResource(R.drawable.logo_background);
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

