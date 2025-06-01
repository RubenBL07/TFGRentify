package com.example.tfg_alquilerherramientas.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tfg_alquilerherramientas.R;
import com.example.tfg_alquilerherramientas.modelos.Cliente;
import com.example.tfg_alquilerherramientas.modelos.Herramienta;
import com.example.tfg_alquilerherramientas.modelos.Reserva;
import com.example.tfg_alquilerherramientas.retrofit.ApiClient;
import com.example.tfg_alquilerherramientas.retrofit.ClienteApiService;
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
        textFechaInicio.setText(String.valueOf(reserva.getFechaInicio()).substring(0, 16).replace("T", " "));
        if (reserva.getFechaFin() != null) {
            textFechaFin.setText(String.valueOf(reserva.getFechaFin()).substring(0, 16).replace("T", " "));
        } else {
            textFechaFin.setText("-");
        }

        textPrecioDia.setText(herramienta.getPrecioDia() + "€");

        if (reserva.getEstado().equals("ACTIVA")) {
            textEstado.setText(reserva.getEstado() + "✅");
            botonCancelarReserva.setEnabled(true);
            botonCancelarReserva.setVisibility(View.VISIBLE);
        } else {
            textEstado.setText(reserva.getEstado() + "❌");
            botonCancelarReserva.setEnabled(false);
            botonCancelarReserva.setVisibility(View.INVISIBLE);
        }

        if (reserva.getEstado().equals("ACTIVA")) {
            long dias = ChronoUnit.DAYS.between(reserva.getFechaInicio(), LocalDateTime.now());
            if (dias <= 0) dias = 1;
            BigDecimal total = herramienta.getPrecioDia().multiply(BigDecimal.valueOf(dias));
            textPrecioTotal.setText(total + "€");
        } else {
            textPrecioTotal.setText(reserva.getPrecioTotal() + "€");
        }

        botonBack.setOnClickListener(v -> finish());

        botonCancelarReserva.setOnClickListener(v -> {
            String estadoAnterior = reserva.getEstado();
            reserva.setEstado("FINALIZADA");

            long dias = ChronoUnit.DAYS.between(reserva.getFechaInicio(), LocalDateTime.now());
            if (dias <= 0) dias = 1;
            BigDecimal total = herramienta.getPrecioDia().multiply(BigDecimal.valueOf(dias));

            reserva.setPrecioTotal(total);
            cliente.setSaldo(cliente.getSaldo().subtract(total));

            ReservaApiService reservaService = ApiClient.getRetrofit().create(ReservaApiService.class);
            ClienteApiService clienteService = ApiClient.getRetrofit().create(ClienteApiService.class);

            reservaService.finalizarReserva(reserva.getId()).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                        clienteService.updateSaldoCliente(cliente.getId(), cliente.getSaldo()).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> respSaldo) {
                                if (respSaldo.isSuccessful()) {
                                    Log.d("SALDO", "Saldo actualizado correctamente");
                                    Toast.makeText(DetallesReservaActivity.this, "Reserva finalizada con éxito", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("SALDO", "Error al actualizar saldo, código: " + respSaldo.code());
                                    Toast.makeText(DetallesReservaActivity.this, "Fallo al actualizar saldo", Toast.LENGTH_SHORT).show();
                                }
                                lanzarHome();
                            }
                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.e("SALDO", "Fallo de red al actualizar saldo", t);
                                Toast.makeText(DetallesReservaActivity.this, "Error de conexión al actualizar saldo", Toast.LENGTH_SHORT).show();
                                lanzarHome();
                            }
                        });
                    } else {
                        Toast.makeText(DetallesReservaActivity.this, "Error al finalizar reserva", Toast.LENGTH_SHORT).show();
                        revertirEstadoLocal(estadoAnterior);
                    }
                }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(DetallesReservaActivity.this, "Error de conexión al finalizar", Toast.LENGTH_SHORT).show();
                    revertirEstadoLocal(estadoAnterior);
                }
            });
        });
    }

    private void lanzarHome() {
        Intent intent = new Intent(DetallesReservaActivity.this, HomeActivity.class);
        intent.putExtra("herramienta", herramienta);
        intent.putExtra("cliente", cliente);
        startActivity(intent);
    }

    private void revertirEstadoLocal(String estadoPrevio) {
        reserva.setEstado(estadoPrevio);
        textEstado.setText(reserva.getEstado());
        botonCancelarReserva.setEnabled(true);
    }
}
