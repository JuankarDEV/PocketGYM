package com.example.pocketgym;

import static androidx.core.view.ViewKt.setPadding;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pocketgym.Conexion.ConexionBBDD;
import com.example.pocketgym.Dao.UsuarioDao;

import java.util.ArrayList;

public class RutinaEjercicios extends AppCompatActivity {

    ArrayList<String>ListaEjercicios =new ArrayList<>();

    private UsuarioDao usuarioDAO;
    private ConexionBBDD conexionDB;

    int id_usuario;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rutina_ejercicios);

        conexionDB = new ConexionBBDD();
        usuarioDAO = new UsuarioDao(conexionDB);

         id_usuario = getIntent().getIntExtra("usuario_id",0);

        LinearLayout contenedorEjercicios = findViewById(R.id.Contenedor_Ejercicios);
        ListaEjercicios = usuarioDAO.obtenerEjercicios(id_usuario);

        for (String ejercicio : ListaEjercicios) {
            Button boton = new Button(this);
            boton.setText(ejercicio);
            boton.setBackground(getDrawable(R.drawable.bg_popup));
            boton.setTextColor(getColor(R.color.nombres));
            boton.setTextSize(40);
            boton.setAllCaps(false);
            boton.setPadding(0, 100, 0, 100);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(0, 40, 0, 40);

            boton.setLayoutParams(params);

            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    go_VideoEjercicio((String) boton.getText());
                }
            });

            contenedorEjercicios.addView(boton);
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void go_ImagenRutina(View view) {
        Intent intent = new Intent(this, ImagenTabla.class);
        intent.putExtra("usuario_id",id_usuario);
        startActivity(intent);
    }

    public void go_VideoEjercicio(String ejercicio) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("boton",ejercicio);
        startActivity(intent);
    }
}