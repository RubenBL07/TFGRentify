package com.example.tfg_alquilerherramientas.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.tfg_alquilerherramientas.retrofit.ApiClient;
import com.example.tfg_alquilerherramientas.retrofit.ReservaApiService;
import com.google.android.material.button.MaterialButton;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesReservaActivity extends AppCompatActivity {
    private ImageView imagenHerramienta;
    private ImageButton botonBack;
    private TextView textNombreHerramienta, textFechaInicio, textFechaFin, textPrecioDia, textEstado, textPrecioTotal;
    private MaterialButton botonCancelarReserva;
    private Herramienta herramienta;
    private Reserva reserva;
    private Cliente cliente;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalles_reserva);

        herramienta = (Herramienta) getIntent().getSerializableExtra("herramienta");
        reserva = (Reserva) getIntent().getSerializableExtra("reserva");
        cliente = (Cliente) getIntent().getSerializableExtra("cliente");

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

        textFechaInicio.setText(String.valueOf(reserva.getFechaInicio()).substring(0,10));
        if (reserva.getFechaFin() != null) {
            textFechaFin.setText(String.valueOf(reserva.getFechaFin()).substring(0,10));
        } else {
            textFechaFin.setText("-");
        }

        textPrecioDia.setText(herramienta.getPrecioDia()+"€");

        if (reserva.getEstado().equals("ACTIVA")) {
            textEstado.setText(reserva.getEstado()+"✅");
            botonCancelarReserva.setEnabled(true);
            botonCancelarReserva.setVisibility(View.VISIBLE);
        } else {
            textEstado.setText(reserva.getEstado()+"❌");
            botonCancelarReserva.setEnabled(false);
            botonCancelarReserva.setVisibility(View.INVISIBLE);
        }
        if(reserva.getEstado().equals("ACTIVA")){
            long dias = ChronoUnit.DAYS.between(LocalDateTime.now(), reserva.getFechaInicio());
            if (dias <= 0) {
                dias = 1;
            }

            textPrecioTotal.setText((herramienta.getPrecioDia().multiply(BigDecimal.valueOf(dias)))+"€");
        }else{
            textPrecioTotal.setText(reserva.getPrecioTotal()+"€");
        }

        botonBack.setOnClickListener(v -> finish());

        botonCancelarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String estadoAnterior = reserva.getEstado();
                reserva.setEstado("FINALIZADA");
                textEstado.setText(estadoAnterior);
                botonCancelarReserva.setEnabled(false);

                ReservaApiService apiService = ApiClient.getRetrofit().create(ReservaApiService.class);

                apiService.finalizarReserva(reserva.getId()).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                            Toast.makeText(DetallesReservaActivity.this, "Reserva finalizada correctamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DetallesReservaActivity.this, HomeActivity.class);
                            intent.putExtra("herramienta", herramienta);
                            intent.putExtra("cliente", cliente);
                            startActivity(intent);
                        } else {
                            try {
                                String error = response.errorBody() != null ? response.errorBody().string() : "<vacío>";
                                Log.e("FinalizarReserva", "Código: " + response.code() + ", errorBody: " + error);
                            } catch (Exception e) {
                                Log.e("FinalizarReserva", "No se pudo leer errorBody", e);
                            }
                            Toast.makeText(DetallesReservaActivity.this, "No se pudo finalizar la reserva (código " + response.code() + ")", Toast.LENGTH_LONG).show();
                            revertirEstadoLocal(estadoAnterior);
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(DetallesReservaActivity.this, "Error de conexión al finalizar", Toast.LENGTH_SHORT).show();
                        revertirEstadoLocal(estadoAnterior);
                    }
                });
            }
        });
    }

    private void revertirEstadoLocal(String estadoPrevio) {
        reserva.setEstado(estadoPrevio);
        textEstado.setText(reserva.getEstado());
        botonCancelarReserva.setEnabled(true);
    }
}