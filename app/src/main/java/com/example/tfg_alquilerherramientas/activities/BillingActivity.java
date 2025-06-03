package com.example.tfg_alquilerherramientas.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_alquilerherramientas.R;
import com.example.tfg_alquilerherramientas.adapters.RecyclerAdapterPagos;
import com.example.tfg_alquilerherramientas.modelos.Cliente;
import com.example.tfg_alquilerherramientas.modelos.Reserva;
import com.example.tfg_alquilerherramientas.retrofit.ApiClient;
import com.example.tfg_alquilerherramientas.retrofit.ReservaApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillingActivity extends AppCompatActivity {
    private ImageButton botonHome, botonBuscar, botonBack;
    private RecyclerView recyclerPagos;
    private RecyclerAdapterPagos adapterPagos;
    private Cliente cliente;
    private TextView textoSaldo;
    private List<Reserva> listaPagos = new ArrayList<>();


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_billing);

        cliente = (Cliente) getIntent().getSerializableExtra("cliente");

        if (cliente == null) {
            Toast.makeText(this, "Cliente no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        botonBack = findViewById(R.id.imageButtonBackBill);
        botonBuscar = findViewById(R.id.imageButtonBuscarBill);
        botonHome = findViewById(R.id.imageButtonHomeBill);
        recyclerPagos = findViewById(R.id.recyclerBill);
        textoSaldo = findViewById(R.id.textViewSaldoBill);

        textoSaldo.setText(cliente.getSaldo() + "€");

        adapterPagos = new RecyclerAdapterPagos(this, listaPagos);
        recyclerPagos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPagos.setAdapter(adapterPagos);

        ReservaApiService reservaApi = ApiClient.getRetrofit().create(ReservaApiService.class);
        reservaApi.getAllReservasByClienteId(cliente.getId()).enqueue(new Callback<List<Reserva>>() {
            @Override
            public void onResponse(@NonNull Call<List<Reserva>> call, @NonNull Response<List<Reserva>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPagos.clear();
                    for (Reserva reserva : response.body()) {
                        if (reserva.getEstado().equals("FINALIZADA")) {
                            listaPagos.add(reserva);
                        }
                    }
                    if (listaPagos.isEmpty()) {
                        Toast.makeText(BillingActivity.this, "Todavía no hay cobros realizados", Toast.LENGTH_SHORT).show();
                    }
                    Collections.reverse(listaPagos);
                    adapterPagos.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Reserva>> call, @NonNull Throwable t) {
                Toast.makeText(BillingActivity.this, "Error al cargar los cobros", Toast.LENGTH_SHORT).show();
            }
        });

        botonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                intent.putExtra("cliente", cliente);
                startActivity(intent);
            }
        });

        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HerramientasActivity.class);
                intent.putExtra("cliente", cliente);
                startActivity(intent);
            }
        });

        botonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                intent.putExtra("cliente", cliente);
                startActivity(intent);
            }
        });
    }
}