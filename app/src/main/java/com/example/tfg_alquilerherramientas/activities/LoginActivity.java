package com.example.tfg_alquilerherramientas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg_alquilerherramientas.R;
import com.example.tfg_alquilerherramientas.modelos.Cliente;
import com.example.tfg_alquilerherramientas.retrofit.ClienteApiService;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText slotEmail;
    private EditText slotPassword;
    private ImageButton botonBack;
    private MaterialButton botonAcceder;
    private MaterialButton botonRegistrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        slotEmail = findViewById(R.id.editTextEmail);
        slotPassword = findViewById(R.id.editTextPassword);
        botonBack = findViewById(R.id.imageButtonBack);
        botonAcceder = findViewById(R.id.buttonAcceder);
        botonRegistrate = findViewById(R.id.buttonRegistrate);

        botonAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = slotEmail.getText().toString().trim();
                String password = slotPassword.getText().toString();
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
                        .addConverterFactory(GsonConverterFactory.create()).build();
                ClienteApiService clienteApiService = retrofit.create(ClienteApiService.class);
                Call<Cliente> call = clienteApiService.getClienteByEmail(email);
                call.enqueue(new Callback<Cliente>() {
                    @Override
                    public void onResponse(@NonNull Call<Cliente> call, @NonNull Response<Cliente> response) {
                        try{
                            if (response.isSuccessful()) {
                                Cliente cliente = response.body();
                                assert cliente != null;
                                if(cliente.getPassword().equals(password) && cliente.getEmail().equals(email)){
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("cliente", cliente);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this, "La contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("Cliente", "No encontrado");
                            }
                        }catch (Exception ex){
                            Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Cliente> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        botonRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        botonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}