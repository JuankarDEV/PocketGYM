package com.example.pocketgym;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DiasActivity extends AppCompatActivity {
    boolean modo_historial;
    int id_usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dias);
        id_usuario = getIntent().getIntExtra("usuario_id",0);
        modo_historial = getIntent().getBooleanExtra("modo_historial",false);
        LinearLayout container = findViewById(R.id.contenedor_botones_dias);

        for (int i = 1; i <= 5; i++) {
            Button boton_dia = new Button(this);
            boton_dia.setText("Dia " + i);
            boton_dia.setBackground(getDrawable(R.drawable.boton_estilo));
            boton_dia.setTextColor(getColor(R.color.white));
            boton_dia.setTextSize(40);
            boton_dia.setTypeface(ResourcesCompat.getFont(this, R.font.aclonica));


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    700,
                    200
            );
            params.setMargins(0, 200, 0, 10);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            boton_dia.setLayoutParams(params);


            int finalI = i;
            boton_dia.setOnClickListener(v ->
                    goEjercicios(String.valueOf(finalI))
            );

            container.addView(boton_dia);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void goEjercicios(String dia) {
        Intent intent = new Intent(this, Seguimiento_Ejercicios.class);
        intent.putExtra("dia", Integer.parseInt(dia));
        intent.putExtra("modo_historial",modo_historial);
        intent.putExtra("id_usuario",id_usuario);
        startActivity(intent);
    }
}