package com.example.tfg_alquilerherramientas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg_alquilerherramientas.R;

import com.example.tfg_alquilerherramientas.modelos.Cliente;
import com.example.tfg_alquilerherramientas.modelos.Herramienta;
import com.example.tfg_alquilerherramientas.retrofit.ApiClient;
import com.example.tfg_alquilerherramientas.retrofit.HerramientaApiService;
import com.google.android.material.button.MaterialButton;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearHerramientaActivity extends AppCompatActivity {
    private EditText slotNombre, slotCategoria, slotPrecioDia, slotImagenUrl, slotDescripcion;
    private MaterialButton botonCrear;
    private ImageButton botonBack;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_herramienta);

        cliente = (Cliente) getIntent().getSerializableExtra("cliente");
        if (cliente == null) {
            Toast.makeText(this, "Cliente no recibido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        };

        slotNombre = findViewById(R.id.editTextNombreCrear);
        slotCategoria = findViewById(R.id.editTextCategoriaCrear);
        slotPrecioDia = findViewById(R.id.editTextPrecioCrear);
        slotImagenUrl = findViewById(R.id.editTextImagenUrlCrear);
        slotDescripcion = findViewById(R.id.editTextDescripcionCrear);
        botonCrear = findViewById(R.id.buttonCrearCrear);
        botonBack = findViewById(R.id.imageButtonBackCrear);

        botonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = slotNombre.getText().toString().trim();
                String descripcion = slotDescripcion.getText().toString().trim();
                String precioDia = slotPrecioDia.getText().toString().trim();
                String categoria = slotCategoria.getText().toString().trim();
                Boolean disponible = true;
                String imagenUrl = slotImagenUrl.getText().toString().trim();

                BigDecimal precioDiaBD = BigDecimal.valueOf(Float.parseFloat(precioDia));

                if (nombre.isEmpty() || descripcion.isEmpty() || precioDia.isEmpty() || categoria.isEmpty() || imagenUrl.isEmpty()) {
                    Toast.makeText(CrearHerramientaActivity.this, "Completa todos los campos porfavor", Toast.LENGTH_SHORT).show();
                    return;
                }

                Herramienta nuevaHerramienta = new Herramienta(nombre, descripcion, precioDiaBD, categoria, disponible, imagenUrl);

                HerramientaApiService herramientaService = ApiClient.getRetrofit().create(HerramientaApiService.class);

                herramientaService.postHerramienta(nuevaHerramienta).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                            Toast.makeText(CrearHerramientaActivity.this, "Herramienta creada correctamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CrearHerramientaActivity.this, HomeActivity.class);
                            intent.putExtra("cliente", cliente);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(CrearHerramientaActivity.this, "No se pudo crear la herramienta", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(CrearHerramientaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("API_ERROR", "Fallo en la red", t);
                    }
                });
            }
        });

        botonBack.setOnClickListener(v -> finish());

    }
}