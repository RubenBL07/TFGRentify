package com.example.tfg_alquilerherramientas.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tfg_alquilerherramientas.R;
import com.example.tfg_alquilerherramientas.modelos.Cliente;

public class PerfilActivity extends AppCompatActivity {
    private ImageButton botonBack;
    private TextView textoNombre, textoEmail, textoDireccion, textoSaldo;
    private Cliente cliente;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        cliente = (Cliente) getIntent().getSerializableExtra("cliente");

        botonBack = findViewById(R.id.imageButtonBackPer);
        textoNombre = findViewById(R.id.textViewNombrePer);
        textoEmail = findViewById(R.id.textViewEmailPer);
        textoDireccion = findViewById(R.id.textViewDireccionPer);
        textoSaldo = findViewById(R.id.textViewSaldoPer);

        textoNombre.setText(cliente.getNombre());
        textoEmail.setText(cliente.getEmail());
        textoDireccion.setText(cliente.getDireccion());
        textoSaldo.setText(cliente.getSaldo()+"€");

        botonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilActivity.this, HomeActivity.class);
                intent.putExtra("cliente", cliente);
                startActivity(intent);
            }
        });
    }
}