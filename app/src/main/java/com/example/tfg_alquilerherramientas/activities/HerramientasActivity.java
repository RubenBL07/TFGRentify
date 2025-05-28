package com.example.tfg_alquilerherramientas.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_alquilerherramientas.R;
import com.example.tfg_alquilerherramientas.adapters.RecyclerAdapterHerramientas;
import com.example.tfg_alquilerherramientas.modelos.Cliente;
import com.example.tfg_alquilerherramientas.modelos.Herramienta;
import com.example.tfg_alquilerherramientas.retrofit.ApiClient;
import com.example.tfg_alquilerherramientas.retrofit.HerramientaApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HerramientasActivity extends AppCompatActivity {
    private ImageButton botonHome, botonBilling, botonBack;
    private RecyclerView recyclerHerramientas;
    private RecyclerAdapterHerramientas adapter;
    private List<Herramienta> listaHerramientas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_herramientas);

        Cliente cliente = (Cliente) getIntent().getSerializableExtra("cliente");

        botonBilling = findViewById(R.id.imageButtonBillingHerr);
        botonHome    = findViewById(R.id.imageButtonHomeHerr);
        botonBack    = findViewById(R.id.imageButtonBackHerr);
        recyclerHerramientas = findViewById(R.id.recyclerHerramientasHerr);

        recyclerHerramientas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapterHerramientas(this, listaHerramientas, (item, position) -> {
            Intent intent = new Intent(HerramientasActivity.this, DetallesHerramientaActivity.class);
            intent.putExtra("herramienta", item);
            intent.putExtra("cliente", cliente);
            startActivity(intent);
        });
        recyclerHerramientas.setAdapter(adapter);

        HerramientaApiService apiService = ApiClient.getRetrofit()
                .create(HerramientaApiService.class);

        apiService.getAllHerramientas().enqueue(new Callback<List<Herramienta>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Herramienta>> call,
                                   @NonNull Response<List<Herramienta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaHerramientas.clear();
                    listaHerramientas.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("Herramientas", "Respuesta no exitosa");
                    Toast.makeText(HerramientasActivity.this,
                            "No hay herramientas disponibles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Herramienta>> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error al llamar a API", t);
                Toast.makeText(HerramientasActivity.this,
                        "Error de conexión al cargar herramientas", Toast.LENGTH_SHORT).show();
            }
        });

        botonBack.setOnClickListener(v -> finish());
        botonHome.setOnClickListener(v -> {
            Intent intent = new Intent(HerramientasActivity.this, HomeActivity.class);
            intent.putExtra("cliente", cliente);
            startActivity(intent);
        });
        botonBilling.setOnClickListener(v -> {
            Intent intent = new Intent(HerramientasActivity.this, BillingActivity.class);
            intent.putExtra("cliente", cliente);
            startActivity(intent);
        });
    }
}
