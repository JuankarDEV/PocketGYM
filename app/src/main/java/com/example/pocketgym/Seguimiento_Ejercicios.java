package com.example.pocketgym;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pocketgym.Conexion.ConexionBBDD;
import com.example.pocketgym.Dao.UsuarioDao;

import java.util.ArrayList;

public class Seguimiento_Ejercicios extends AppCompatActivity {

    boolean modo_historial;

    private UsuarioDao usuarioDAO;
    private ConexionBBDD conexionDB;

    int id_usuario;
    int dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seguimiento_ejercicios);

        conexionDB = new ConexionBBDD();
        usuarioDAO = new UsuarioDao(conexionDB);
        id_usuario = getIntent().getIntExtra("id_usuario",0);
        dia = getIntent().getIntExtra("dia",0);
        modo_historial = getIntent().getBooleanExtra("modo_historial",false);

        ArrayList<String> ejercicios = usuarioDAO.obtenerEjerciciosdia(1,dia);

        LinearLayout layout = findViewById(R.id.btones_ejer);

        for (String ejercicio : ejercicios) {
            Button btn = new Button(this);
            btn.setText(ejercicio);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    800,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(0, 200, 0, 10);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            btn.setPadding(5,5,0,0);
            btn.setLayoutParams(params);
            btn.setTextColor(getColor(R.color.white));
            btn.setTextSize(24);
            btn.setTypeface(ResourcesCompat.getFont(this, R.font.aclonica));
            btn.setBackground(ContextCompat.getDrawable(this, R.drawable.boton_estilo));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goPrueba(ejercicio);
                }
            });
            layout.addView(btn);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void goPrueba(String ejercicio) {


        Intent intent = new Intent(this, Seguimiento_Series.class);
        intent.putExtra("modo_historial",modo_historial);
        intent.putExtra("ejercicio",ejercicio);
        intent.putExtra("dia",dia);
        intent.putExtra("id_usuario",id_usuario);
        startActivity(intent);
    }
}