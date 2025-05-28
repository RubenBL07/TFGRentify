package com.example.tfg_alquilerherramientas.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_alquilerherramientas.R;
import com.example.tfg_alquilerherramientas.adapters.RecyclerAdapterReservas;
import com.example.tfg_alquilerherramientas.modelos.Cliente;
import com.example.tfg_alquilerherramientas.modelos.Reserva;
import com.example.tfg_alquilerherramientas.retrofit.ApiClient;
import com.example.tfg_alquilerherramientas.retrofit.ReservaApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private TextView textoBienvenida, textoSaldo;
    private ImageButton botonBack, botonAjustes, botonBuscar, botonBilling;
    private RecyclerView recyclerReservas;
    private RecyclerAdapterReservas adapter;
    private List<Reserva> listaReservas = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        textoBienvenida = findViewById(R.id.textViewBienvenidaH);
        textoSaldo = findViewById(R.id.textViewSaldoH);
        botonBack = findViewById(R.id.imageButtonBackH);
        botonAjustes = findViewById(R.id.imageButtonAjustesH);
        botonBuscar = findViewById(R.id.imageButtonBuscarH);
        botonBilling = findViewById(R.id.imageButtonBillingH);
        recyclerReservas = findViewById(R.id.recyclerReservasH);

        Cliente cliente = (Cliente) getIntent().getSerializableExtra("cliente");
        if (cliente != null) {
            textoBienvenida.append(" " + cliente.getNombre());
            textoSaldo.setText(" "+cliente.getSaldo()+"€");
        } else {
            Toast.makeText(this, "Cliente no recibido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerReservas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapterReservas(this, listaReservas, (item, position) -> {
            Intent intent = new Intent(HomeActivity.this, DetallesReservaActivity.class);
            intent.putExtra("reserva", item);
            intent.putExtra("herramienta", item.getHerramienta());
            startActivity(intent);
        });
        recyclerReservas.setAdapter(adapter);

        ReservaApiService reservaService = ApiClient.getRetrofit()
                .create(ReservaApiService.class);

        reservaService.getAllReservasByClienteId(cliente.getId())
                .enqueue(new Callback<List<Reserva>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Reserva>> call,
                                           @NonNull Response<List<Reserva>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            listaReservas.clear();
                            listaReservas.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("Reservas", "Respuesta no exitosa");
                            Toast.makeText(HomeActivity.this,
                                    "No hay reservas disponibles", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Reserva>> call,
                                          @NonNull Throwable t) {
                        Log.e("API_ERROR", "Error al llamar a API", t);
                        Toast.makeText(HomeActivity.this,
                                        "Error de conexión al cargar reservas", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        botonBack.setOnClickListener(v -> finish());
        botonBuscar.setOnClickListener(v -> {
            Intent i = new Intent(this, HerramientasActivity.class);
            i.putExtra("cliente", cliente);
            startActivity(i);
        });
        botonBilling.setOnClickListener(v -> {
            Intent i = new Intent(this, BillingActivity.class);
            i.putExtra("cliente", cliente);
            startActivity(i);
        });
        botonAjustes.setOnClickListener(v -> {
            // TODO: implementar ajustes
        });
    }
}
