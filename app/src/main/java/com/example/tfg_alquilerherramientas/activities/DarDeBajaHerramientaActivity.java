package com.example.tfg_alquilerherramientas.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg_alquilerherramientas.R;
import com.example.tfg_alquilerherramientas.modelos.Cliente;
import com.example.tfg_alquilerherramientas.modelos.Herramienta;
import com.example.tfg_alquilerherramientas.retrofit.ApiClient;
import com.example.tfg_alquilerherramientas.retrofit.HerramientaApiService;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DarDeBajaHerramientaActivity extends AppCompatActivity {
    private MaterialButton botonDarDeBaja, botonDarDeAlta;
    private Spinner spinnerHerramientas1, spinnerHerramientas2;
    private ImageButton botonBack;
    private Cliente cliente;

    private List<Herramienta> listaHerramientasDisponibles = new ArrayList<>();
    private List<String> nombresHerramientasDisponibles = new ArrayList<>();
    private ArrayAdapter<String> adapterDisponibles;

    private List<Herramienta> listaHerramientasInactivas = new ArrayList<>();
    private List<String> nombresHerramientasInactivas = new ArrayList<>();
    private ArrayAdapter<String> adapterInactivas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dar_de_baja_herramienta);

        cliente = (Cliente) getIntent().getSerializableExtra("cliente");

        botonDarDeBaja = findViewById(R.id.buttonDarDeBaja);
        botonDarDeAlta = findViewById(R.id.buttonDarDeAlta);
        spinnerHerramientas1 = findViewById(R.id.spinnerBaja);
        spinnerHerramientas2 = findViewById(R.id.spinnerAlta);
        botonBack = findViewById(R.id.imageButtonBackBaja);

        botonDarDeBaja.setEnabled(false);
        botonDarDeAlta.setEnabled(false);

        cargarHerramientasActivas();
        cargarHerramientasInactivas();

        botonDarDeBaja.setOnClickListener(v -> {
            int pos = spinnerHerramientas1.getSelectedItemPosition();
            if (pos < 0 || pos >= listaHerramientasDisponibles.size()) {
                Toast.makeText(this, "Selecciona una herramienta válida", Toast.LENGTH_SHORT).show();
                return;
            }
            Herramienta seleccion = listaHerramientasDisponibles.get(pos);

            HerramientaApiService api = ApiClient.getRetrofit().create(HerramientaApiService.class);
            api.darDeBajaHerramienta(seleccion.getId()).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                        Toast.makeText(DarDeBajaHerramientaActivity.this, "Herramienta dada de baja", Toast.LENGTH_SHORT).show();

                        Herramienta h = listaHerramientasDisponibles.remove(pos);
                        nombresHerramientasDisponibles.remove(pos);
                        adapterDisponibles.notifyDataSetChanged();

                        listaHerramientasInactivas.add(h);
                        nombresHerramientasInactivas.add(h.getNombre());
                        adapterInactivas.notifyDataSetChanged();

                        if (listaHerramientasDisponibles.isEmpty()) botonDarDeBaja.setEnabled(false);
                        botonDarDeAlta.setEnabled(!listaHerramientasInactivas.isEmpty());
                    } else {
                        Toast.makeText(DarDeBajaHerramientaActivity.this, "No se pudo dar de baja (HTTP " + response.code() + ")", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                    Toast.makeText(DarDeBajaHerramientaActivity.this, "Error de conexión al dar de baja", Toast.LENGTH_SHORT).show();
                }
            });
        });

        botonDarDeAlta.setOnClickListener(v -> {
            int pos = spinnerHerramientas2.getSelectedItemPosition();
            if (pos < 0 || pos >= listaHerramientasInactivas.size()) {
                Toast.makeText(this, "Selecciona una herramienta válida", Toast.LENGTH_SHORT).show();
                return;
            }
            Herramienta seleccion = listaHerramientasInactivas.get(pos);

            HerramientaApiService api = ApiClient.getRetrofit().create(HerramientaApiService.class);
            api.reactivarHerramienta(seleccion.getId()).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                        Toast.makeText(DarDeBajaHerramientaActivity.this, "Herramienta reactivada", Toast.LENGTH_SHORT).show();

                        Herramienta h = listaHerramientasInactivas.remove(pos);
                        nombresHerramientasInactivas.remove(pos);
                        adapterInactivas.notifyDataSetChanged();

                        listaHerramientasDisponibles.add(h);
                        nombresHerramientasDisponibles.add(h.getNombre());
                        adapterDisponibles.notifyDataSetChanged();

                        if (listaHerramientasInactivas.isEmpty()) botonDarDeAlta.setEnabled(false);
                        botonDarDeBaja.setEnabled(!listaHerramientasDisponibles.isEmpty());
                    } else {
                        Toast.makeText(DarDeBajaHerramientaActivity.this, "No se pudo reactivar (HTTP " + response.code() + ")", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                    Toast.makeText(DarDeBajaHerramientaActivity.this, "Error de conexión al reactivar", Toast.LENGTH_SHORT).show();
                }
            });
        });

        botonBack.setOnClickListener(v -> finish());
    }

    private void cargarHerramientasActivas() {
        HerramientaApiService api = ApiClient.getRetrofit().create(HerramientaApiService.class);
        api.getAllHerramientasActivas().enqueue(new Callback<List<Herramienta>>() {
            @Override
            public void onResponse(@NonNull Call<List<Herramienta>> call, @NonNull Response<List<Herramienta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Herramienta> herramientas = response.body();
                    listaHerramientasDisponibles.clear();
                    nombresHerramientasDisponibles.clear();

                    for (Herramienta h : herramientas) {
                        if (h.getDisponible()) {
                            listaHerramientasDisponibles.add(h);
                            nombresHerramientasDisponibles.add(h.getNombre());
                        }
                    }

                    if (listaHerramientasDisponibles.isEmpty()) {
                        Toast.makeText(DarDeBajaHerramientaActivity.this, "No hay herramientas activas", Toast.LENGTH_SHORT).show();
                        spinnerHerramientas1.setEnabled(false);
                        return;
                    }

                    adapterDisponibles = new ArrayAdapter<>(
                            DarDeBajaHerramientaActivity.this,
                            android.R.layout.simple_spinner_item,
                            nombresHerramientasDisponibles
                    );
                    adapterDisponibles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerHerramientas1.setAdapter(adapterDisponibles);
                    botonDarDeBaja.setEnabled(true);
                } else {
                    Toast.makeText(DarDeBajaHerramientaActivity.this, "Error al cargar herramientas activas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Herramienta>> call, @NonNull Throwable t) {
                Toast.makeText(DarDeBajaHerramientaActivity.this, "Error de conexión al cargar activas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarHerramientasInactivas() {
        HerramientaApiService api = ApiClient.getRetrofit().create(HerramientaApiService.class);
        api.getAllHerramientasInactivas().enqueue(new Callback<List<Herramienta>>() {
            @Override
            public void onResponse(@NonNull Call<List<Herramienta>> call, @NonNull Response<List<Herramienta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Herramienta> herramientas = response.body();
                    listaHerramientasInactivas.clear();
                    nombresHerramientasInactivas.clear();

                    for (Herramienta h : herramientas) {
                        listaHerramientasInactivas.add(h);
                        nombresHerramientasInactivas.add(h.getNombre());
                    }

                    if (listaHerramientasInactivas.isEmpty()) {
                        Toast.makeText(DarDeBajaHerramientaActivity.this, "No hay herramientas inactivas", Toast.LENGTH_SHORT).show();
                        spinnerHerramientas2.setEnabled(false);
                        return;
                    }

                    adapterInactivas = new ArrayAdapter<>(
                            DarDeBajaHerramientaActivity.this,
                            android.R.layout.simple_spinner_item,
                            nombresHerramientasInactivas
                    );
                    adapterInactivas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerHerramientas2.setAdapter(adapterInactivas);
                    botonDarDeAlta.setEnabled(true);
                } else {
                    Toast.makeText(DarDeBajaHerramientaActivity.this, "Error al cargar herramientas inactivas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Herramienta>> call, @NonNull Throwable t) {
                Toast.makeText(DarDeBajaHerramientaActivity.this, "Error de conexión al cargar inactivas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
