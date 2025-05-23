package com.example.pocketgym;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pocketgym.Conexion.ConexionBBDD;
import com.example.pocketgym.Dao.UsuarioDao;

import java.util.ArrayList;

public class Seguimiento_Series extends AppCompatActivity {

    private UsuarioDao usuarioDAO;
    private ConexionBBDD conexionDB;
    int id_ejercicio, id_usuario;
    boolean modo_historial;

    ArrayList<Integer>pesosyrepes_rellenar = new ArrayList<>();
    ArrayList<EditText> listaET = new ArrayList<>();
    int dia ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seguimiento_series);


        conexionDB = new ConexionBBDD();
        usuarioDAO = new UsuarioDao(conexionDB);
        id_usuario = getIntent().getIntExtra("id_usuario", 0);
        String ejercicio = getIntent().getStringExtra("ejercicio");
        dia = getIntent().getIntExtra("dia",0);
        modo_historial = getIntent().getBooleanExtra("modo_historial",false);
        TextView tw_series_ejercicio = findViewById(R.id.nombre_ejercicio);
        id_ejercicio = usuarioDAO.obteneridEjercicio(ejercicio);
        tw_series_ejercicio.setText(ejercicio);

        LinearLayout mainLayout = findViewById(R.id.linearLayout1);
        int cantidad = usuarioDAO.ObtenerSeriesEjercicios(ejercicio);

        if (!modo_historial) {
            for (int i = 1; i <= cantidad; i++) {

                LinearLayout horizontalLayout = new LinearLayout(this);
                horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));


                TextView textView = new TextView(this);
                textView.setText("Serie " + i);
                textView.setTextColor(getColor(R.color.nombres));
                textView.setTextSize(28);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));


                EditText editText1 = new EditText(this);
                editText1.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_popup));
                editText1.setTextColor(getColor(R.color.white));
                editText1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                editText1.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText1.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));

                EditText editText2 = new EditText(this);
                editText2.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_popup));
                editText2.setTextColor(getColor(R.color.white));
                editText2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                editText2.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText2.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));

                listaET.add(editText1);
                listaET.add(editText2);
                horizontalLayout.addView(textView);
                horizontalLayout.addView(editText1);
                horizontalLayout.addView(editText2);


                mainLayout.addView(horizontalLayout);


                if (i <= cantidad - 1) {
                    TextView separador = new TextView(this);
                    separador.setText("-------------------------");
                    separador.setTextColor(getColor(R.color.white));
                    separador.setTextSize(60);
                    separador.setGravity(Gravity.CENTER);
                    separador.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    mainLayout.addView(separador);
                }
            }
            Button boton = new Button(this);
            boton.setText("GUARDAR");
            boton.setBackground(getDrawable(R.drawable.boton_estilo));
            boton.setTextColor(getColor(R.color.nombres));
            boton.setTextSize(28);

            boton.setAllCaps(false);
            boton.setPadding(0, 100, 0, 100);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.setMargins(0, 40, 0, 40);

            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardarDatos();
                }
            });
            boton.setLayoutParams(params);
            mainLayout.addView(boton);
        }
        else{
            for (int i = 1; i <= cantidad; i++) {
                pesosyrepes_rellenar =usuarioDAO.obtenerpesoyrepes(id_usuario,id_ejercicio,i,dia);


                LinearLayout horizontalLayout = new LinearLayout(this);
                horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));


                TextView textView = new TextView(this);
                textView.setText("Serie " +i);
                textView.setTextColor(getColor(R.color.nombres));
                textView.setTextSize(28);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));


                EditText editText1 = new EditText(this);

                if(pesosyrepes_rellenar.isEmpty()){
                    editText1.setText("X");
                    editText1.setTextColor(getColor(R.color.error));
                }
                else{
                    editText1.setText(String.valueOf(pesosyrepes_rellenar.get(0)));
                    editText1.setTextColor(getColor(R.color.white));
                }

                editText1.setFocusable(false);
                editText1.setCursorVisible(false);
                editText1.setInputType(InputType.TYPE_NULL);
                editText1.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_popup));

                editText1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                editText1.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));

                EditText editText2 = new EditText(this);

                if(pesosyrepes_rellenar.isEmpty()){
                    editText2.setText("X");
                    editText2.setTextColor(getColor(R.color.error));
                }
                else{
                    editText2.setText(String.valueOf(pesosyrepes_rellenar.get(1)));
                    editText2.setTextColor(getColor(R.color.white));
                }
                editText2.setFocusable(false);
                editText2.setCursorVisible(false);
                editText2.setInputType(InputType.TYPE_NULL);
                editText2.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_popup));

                editText2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                editText2.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));

                listaET.add(editText1);
                listaET.add(editText2);
                horizontalLayout.addView(textView);
                horizontalLayout.addView(editText1);
                horizontalLayout.addView(editText2);


                mainLayout.addView(horizontalLayout);


                if (i <= cantidad - 1) {
                    TextView separador = new TextView(this);
                    separador.setText("-------------------------");
                    separador.setTextColor(getColor(R.color.white));
                    separador.setTextSize(60);
                    separador.setGravity(Gravity.CENTER);
                    separador.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    mainLayout.addView(separador);
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void abrirCronometro(View view) {
        CronometroDialogFragment dialog = new CronometroDialogFragment();
        dialog.show(getSupportFragmentManager(), "cronometro");

    }

    public void guardarDatos() {
        for (int i = 0; i < listaET.size(); i += 2) {
            String pesoStr = listaET.get(i).getText().toString();
            String repeticionesStr = listaET.get(i + 1).getText().toString();

            if (!pesoStr.isEmpty() && !repeticionesStr.isEmpty()) {
                int peso = Integer.parseInt(pesoStr);
                int repeticiones = Integer.parseInt(repeticionesStr);
                int serie = (i / 2) + 1;

                if (usuarioDAO.insertarSeries(id_usuario, id_ejercicio, repeticiones, peso, serie,dia)) {
                    Toast.makeText(this, "Serie " + serie + " Guardada Correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al guardar serie" + serie, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Rellena todos los campos antes de guardar", Toast.LENGTH_SHORT).show();

            }
        }

    }
}