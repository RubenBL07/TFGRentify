package com.example.tfg_alquilerherramientas.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tfg_alquilerherramientas.R;
import com.example.tfg_alquilerherramientas.modelos.Cliente;
import com.example.tfg_alquilerherramientas.modelos.Herramienta;
import com.example.tfg_alquilerherramientas.modelos.Reserva;
import com.google.android.material.button.MaterialButton;

public class DetallesReservaActivity extends AppCompatActivity {
    private ImageView imagenHerramienta;
    private ImageButton botonBack;
    private TextView textNombreHerramienta, textFechaInicio, textFechaFin, textPrecioDia, textEstado, textPrecioTotal;
    private MaterialButton botonCancelarReserva;
    private Herramienta herramienta;
    private Cliente cliente;
    private Reserva reserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalles_reserva);

        herramienta = (Herramienta) getIntent().getSerializableExtra("herramienta");
        cliente = (Cliente) getIntent().getSerializableExtra("cliente");
        reserva = (Reserva) getIntent().getSerializableExtra("reserva");

        botonBack = findViewById(R.id.imageButtonBackDetRe);
        imagenHerramienta = findViewById(R.id.imageViewHerramientaDetRe);
        textNombreHerramienta = findViewById(R.id.textViewHerramientaDetRe);
        textFechaInicio = findViewById(R.id.textViewFechaInicioDetRe);
        textFechaFin = findViewById(R.id.textViewFechaFinDetRe);
        textPrecioDia = findViewById(R.id.textViewPrecioDiaDetRe);
        textEstado = findViewById(R.id.textViewEstadoDetRe);
        textPrecioTotal = findViewById(R.id.textViewPrecioTotalDetRe);
        botonCancelarReserva = findViewById(R.id.buttonCancelarReservaDetRe);

        Glide.with(this)
                .load(herramienta.getImagenUrl())
                .placeholder(R.drawable.logo_background)
                .error(R.drawable.logo_background)
                .into(imagenHerramienta);
        textNombreHerramienta.setText(herramienta.getNombre());
        textFechaInicio.setText(String.valueOf(reserva.getFechaInicio()));
        textFechaFin.setText(String.valueOf(reserva.getFechaFin()));
        textPrecioDia.setText(String.valueOf(herramienta.getPrecioDia()));
        textEstado.setText(reserva.getEstado());
        textPrecioTotal.setText(String.valueOf(reserva.getPrecioTotal()));

        botonBack.setOnClickListener(v -> finish());













    }
}