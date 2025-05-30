package com.example.tfg_alquilerherramientas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg_alquilerherramientas.R;
import com.example.tfg_alquilerherramientas.modelos.Cliente;
import com.example.tfg_alquilerherramientas.retrofit.ApiClient;
import com.example.tfg_alquilerherramientas.retrofit.ClienteApiService;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText slotNombre;
    private EditText slotEmail;
    private EditText slotPassword;
    private EditText slotDireccion;
    private MaterialButton botonRegistrar;
    private ImageButton botonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        slotNombre   = findViewById(R.id.editTextNombreR);
        slotEmail    = findViewById(R.id.editTextEmailR);
        slotPassword = findViewById(R.id.editTextPasswordR);
        slotDireccion= findViewById(R.id.editTextDireccionR);
        botonRegistrar = findViewById(R.id.buttonRegistroR);
        botonBack      = findViewById(R.id.imageButtonBackR);

        botonBack.setOnClickListener(v -> finish());

        botonRegistrar.setOnClickListener(v -> {
            String nombre = slotNombre.getText().toString().trim();
            String email = slotEmail.getText().toString().trim();
            String password = slotPassword.getText().toString().trim();
            String direccion = slotDireccion.getText().toString().trim();

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || direccion.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Cliente nuevoCliente = new Cliente(nombre, email, password, direccion);

            ClienteApiService clienteService = ApiClient.getRetrofit().create(ClienteApiService.class);

            clienteService.postCliente(nuevoCliente).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                        Toast.makeText(RegisterActivity.this, "Cliente creado correctamente", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Fallo en la red", t);
                }
            });
        });
    }
}
