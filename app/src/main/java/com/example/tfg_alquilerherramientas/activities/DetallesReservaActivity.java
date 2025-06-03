package com.example.tfg_alquilerherramientas.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    private BigDecimal precioTotalCalculado;
    private static final DateTimeFormatter ISO_FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

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
        textPrecioDia.setText(herramienta.getPrecioDia() + "€");

        textFechaInicio.setText(reserva.getFechaInicio().toString().substring(0, 16).replace("T", " "));

        if (reserva.getEstado().equals("ACTIVA")) {
            textFechaFin.setText("-");
        } else {
            if (reserva.getFechaFin() != null) {
                textFechaFin.setText(reserva.getFechaFin().toString().substring(0, 16).replace("T", " "));
            } else {
                textFechaFin.setText("-");
            }
        }

        if (reserva.getEstado().equals("ACTIVA")) {
            textEstado.setText(reserva.getEstado() + "✅");
            botonCancelarReserva.setEnabled(true);
            botonCancelarReserva.setVisibility(MaterialButton.VISIBLE);
        } else {
            textEstado.setText(reserva.getEstado() + "❌");
            botonCancelarReserva.setEnabled(false);
            botonCancelarReserva.setVisibility(MaterialButton.INVISIBLE);
        }

        actualizarPrecioTotal();

        botonBack.setOnClickListener(v -> finish());

        botonCancelarReserva.setOnClickListener(v -> {
            BigDecimal totalAhora = calcularPrecioTotal();

            LocalDateTime fechaFin = ZonedDateTime.now(ZoneId.of("Europe/Madrid")).toLocalDateTime();
            String fechaFinStr = fechaFin.format(ISO_FMT);

            reserva.setFechaFin(fechaFin);
            textFechaFin.setText(fechaFin.toString().substring(0, 16).replace("T", " "));

            String estadoAnterior = reserva.getEstado();
            reserva.setEstado("FINALIZADA");
            textEstado.setText(reserva.getEstado() + "❌");
            botonCancelarReserva.setEnabled(false);

            reserva.setPrecioTotal(totalAhora);
            cliente.setSaldo(cliente.getSaldo().subtract(totalAhora));

            ReservaApiService reservaService = ApiClient.getRetrofit().create(ReservaApiService.class);
            ClienteApiService clienteService = ApiClient.getRetrofit().create(ClienteApiService.class);

            reservaService.finalizarReserva(reserva.getId(), fechaFinStr, totalAhora).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                        clienteService.updateSaldoCliente(cliente.getId(), cliente.getSaldo()).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> respSaldo) {
                                if (respSaldo.isSuccessful() && Boolean.TRUE.equals(respSaldo.body())) {
                                    Log.d("SALDO", "Saldo actualizado correctamente");
                                    Toast.makeText(DetallesReservaActivity.this, "Reserva finalizada con éxito", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("SALDO", "Error al actualizar saldo, código: " + respSaldo.code());
                                    Toast.makeText(DetallesReservaActivity.this, "Fallo al actualizar saldo", Toast.LENGTH_SHORT).show();
                                }
                                volverAHome();
                            }
                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.e("SALDO", "Fallo de red al actualizar saldo", t);
                                Toast.makeText(DetallesReservaActivity.this, "Error de conexión al actualizar saldo", Toast.LENGTH_SHORT).show();
                                volverAHome();
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

    @Override
    protected void onResume() {
        super.onResume();
        actualizarPrecioTotal();
    }

    private BigDecimal calcularPrecioTotal() {
        long dias = ChronoUnit.DAYS.between(reserva.getFechaInicio(), LocalDateTime.now().plusDays(1));
        if (dias <= 0) {
            dias = 1;
        }
        return herramienta.getPrecioDia().multiply(BigDecimal.valueOf(dias));
    }
    
    private void actualizarPrecioTotal() {
        if (reserva.getEstado().equals("ACTIVA")) {
            precioTotalCalculado = calcularPrecioTotal();
            textPrecioTotal.setText(precioTotalCalculado + "€");
        } else {
            precioTotalCalculado = reserva.getPrecioTotal();
            textPrecioTotal.setText(precioTotalCalculado + "€");
        }
    }

    private void volverAHome() {
        Intent intent = new Intent(DetallesReservaActivity.this, HomeActivity.class);
        intent.putExtra("herramienta", herramienta);
        intent.putExtra("cliente", cliente);
        startActivity(intent);
    }

    private void revertirEstadoLocal(String estadoPrevio) {
        reserva.setEstado(estadoPrevio);
        textEstado.setText(reserva.getEstado() + "✅");
        botonCancelarReserva.setEnabled(true);
        actualizarPrecioTotal();
    }
}
