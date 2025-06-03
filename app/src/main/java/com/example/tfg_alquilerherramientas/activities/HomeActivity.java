package com.example.tfg_alquilerherramientas.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
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
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private TextView textoBienvenida;
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
        botonBack = findViewById(R.id.imageButtonBackH);
        botonAjustes = findViewById(R.id.imageButtonAjustesH);
        botonBuscar = findViewById(R.id.imageButtonBuscarH);
        botonBilling = findViewById(R.id.imageButtonBillingH);
        recyclerReservas = findViewById(R.id.recyclerReservasH);

        Cliente cliente = (Cliente) getIntent().getSerializableExtra("cliente");
        if (cliente != null) {
            textoBienvenida.append(" " + cliente.getNombre());
        } else {
            Toast.makeText(this, "Cliente no recibido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerReservas.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapterReservas(this, listaReservas, (item, position) -> {
            Intent intent = new Intent(HomeActivity.this, DetallesReservaActivity.class);
            intent.putExtra("reserva", item);
            intent.putExtra("herramienta", item.getHerramienta());
            intent.putExtra("cliente", cliente);
            startActivity(intent);
        });
        recyclerReservas.setAdapter(adapter);

        ReservaApiService reservaService = ApiClient.getRetrofit().create(ReservaApiService.class);

        reservaService.getAllReservasByClienteId(cliente.getId()).enqueue(new Callback<List<Reserva>>() {
            @Override
            public void onResponse(@NonNull Call<List<Reserva>> call, @NonNull Response<List<Reserva>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaReservas.clear();
                    List<Reserva> reservas = response.body();
                    if (reservas != null) {
                        listaReservas.addAll(reservas);
                        Collections.reverse(listaReservas);
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(HomeActivity.this, "No hay reservas disponibles", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Reservas", "Respuesta no exitosa");
                    Toast.makeText(HomeActivity.this, "No hay reservas disponibles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Reserva>> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error al llamar a API", t);
                Toast.makeText(HomeActivity.this, "Error de conexión al cargar reservas", Toast.LENGTH_SHORT).show();
            }
        });

        botonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

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
            PopupMenu popupMenu = new PopupMenu(HomeActivity.this, botonAjustes);
            popupMenu.getMenu().add("Perfil");

            if (cliente.getEmail().equals("admin@gmail.com") && cliente.getPassword().equals("admin")) {
                popupMenu.getMenu().add("Crear herramienta");
                popupMenu.getMenu().add("Administrar herramientas");

            }

            popupMenu.setOnMenuItemClickListener(item -> {
                String titulo = item.getTitle().toString();
                if (titulo.equals("Perfil")) {
                    Intent intent = new Intent(this, PerfilActivity.class);
                    intent.putExtra("cliente", cliente);
                    startActivity(intent);
                    return true;
                } else if (titulo.equals("Crear herramienta")) {
                    Intent intent = new Intent(this, CrearHerramientaActivity.class);
                    intent.putExtra("cliente", cliente);
                    startActivity(intent);
                    return true;
                } else if (titulo.equals("Administrar herramientas")) {
                    Intent intent = new Intent(this, DarDeBajaHerramientaActivity.class);
                    intent.putExtra("cliente", cliente);
                    startActivity(intent);
                    return true;
                }
                return false;
            });
            popupMenu.show();

        });
    }
}
