package com.example.tfg_alquilerherramientas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tfg_alquilerherramientas.R;
import com.example.tfg_alquilerherramientas.modelos.Cliente;

public class BillingActivity extends AppCompatActivity {
    private ImageButton botonHome;
    private ImageButton botonBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_billing);

        Cliente cliente = (Cliente) getIntent().getSerializableExtra("cliente");

        botonBuscar = findViewById(R.id.imageButtonBuscarBill);
        botonHome = findViewById(R.id.imageButtonHomeBill);

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