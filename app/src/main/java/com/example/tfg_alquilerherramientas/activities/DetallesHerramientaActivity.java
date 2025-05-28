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
import com.example.tfg_alquilerherramientas.retrofit.ReservaApiService;
import com.example.tfg_alquilerherramientas.retrofit.HerramientaApiService;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesHerramientaActivity extends AppCompatActivity {
    private TextView textNombre, textCategoria, textPrecio, textDescripcion, textDisponible;
    private ImageView imageHerramienta;
    private ImageButton botonBack;
    private MaterialButton botonReservar;
    private Herramienta herramienta;
    private Cliente cliente;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalles_herramienta);

        herramienta = (Herramienta) getIntent().getSerializableExtra("herramienta");
        cliente = (Cliente) getIntent().getSerializableExtra("cliente");

        textNombre = findViewById(R.id.textViewNombreDetHerr);
        textCategoria = findViewById(R.id.textViewCategoriaDetHerr);
        textPrecio = findViewById(R.id.textViewPrecioDetHerr);
        textDescripcion = findViewById(R.id.textViewDescripcionDetHerr);
        textDisponible = findViewById(R.id.textViewDisponibilidadDetHerr);
        imageHerramienta = findViewById(R.id.imageViewDetHerr);
        botonReservar = findViewById(R.id.buttonReservarDetHerr);
        botonBack = findViewById(R.id.imageButtonBackDetHerr);

        textNombre.setText(herramienta.getNombre());
        textCategoria.setText(herramienta.getCategoria());
        textPrecio.setText(herramienta.getPrecioDia() + "€/día");
        textDescripcion.setText(herramienta.getDescripcion());
        textDisponible.setText(herramienta.getDisponible() ? "Disponible✅" : "Reservado🚫");

        Glide.with(this)
                .load(herramienta.getImagenUrl())
                .placeholder(R.drawable.logo_background)
                .error(R.drawable.logo_background)
                .into(imageHerramienta);

        botonReservar.setEnabled(herramienta.getDisponible());

        botonReservar.setOnClickListener(v -> {
            herramienta.setDisponible(false);
            textDisponible.setText("Reservado🚫");
            botonReservar.setEnabled(false);

            Reserva nuevaReserva = new Reserva(cliente,
                    herramienta,
                    LocalDateTime.now(),
                    "ACTIVA✅");

            ReservaApiService reservaService =
                    ApiClient.getRetrofit().create(ReservaApiService.class);

            reservaService.postReserva(nuevaReserva)
                    .enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call,
                                               Response<Boolean> response) {
                            if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                                HerramientaApiService herramientaService =
                                        ApiClient.getRetrofit().create(HerramientaApiService.class);
                                herramientaService.updateHerramienta(herramienta.getId(), herramienta)
                                        .enqueue(new Callback<Boolean>() {
                                            @Override
                                            public void onResponse(Call<Boolean> call, Response<Boolean> resp2) {
                                                if (resp2.isSuccessful() && Boolean.TRUE.equals(resp2.body())) {
                                                    Toast.makeText(DetallesHerramientaActivity.this,
                                                                    "Reserva completada", Toast.LENGTH_SHORT)
                                                            .show();
                                                    Intent intent = new Intent(
                                                            DetallesHerramientaActivity.this,
                                                            HomeActivity.class);
                                                    intent.putExtra("cliente", cliente);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(DetallesHerramientaActivity.this,
                                                            "Reserva fallida", Toast.LENGTH_SHORT).show();
                                                    revertirEstado();
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<Boolean> call, Throwable t) {
                                                Toast.makeText(DetallesHerramientaActivity.this,
                                                        "Algo ha fallado", Toast.LENGTH_SHORT).show();
                                                revertirEstado();
                                            }
                                        });
                            } else {
                                Toast.makeText(DetallesHerramientaActivity.this,
                                        "Error de conexión", Toast.LENGTH_SHORT).show();
                                Log.e("NETWORK_FAILURE", "Error: " + response);
                                revertirEstado();
                            }
                        }
                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(DetallesHerramientaActivity.this,
                                    "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            revertirEstado();
                        }
                    });
        });

        botonBack.setOnClickListener(v -> finish());
    }

    private void revertirEstado() {
        herramienta.setDisponible(true);
        textDisponible.setText("Disponible✅");
        botonReservar.setEnabled(true);
    }
}
