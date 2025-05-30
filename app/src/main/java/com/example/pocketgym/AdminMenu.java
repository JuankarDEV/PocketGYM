package com.example.pocketgym;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pocketgym.Conexion.ConexionBBDD;
import com.example.pocketgym.Dao.UsuarioDao;
import com.example.pocketgym.Models.Usuario;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminMenu extends AppCompatActivity {

    ArrayList<String> niveles = new ArrayList<>(Arrays.asList("1","2","3","4"));
    Spinner n_picker ;
    EditText et_nombre;
    EditText et_dni;

    private UsuarioDao usuarioDAO;
    private ConexionBBDD conexionDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_menu);

        n_picker = findViewById(R.id.nivel_picker);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, niveles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        n_picker.setAdapter(adapter);
        et_nombre = findViewById(R.id.et_nombre);
        et_dni = findViewById(R.id.et_dni);
        conexionDB = new ConexionBBDD();
        usuarioDAO = new UsuarioDao(conexionDB);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void registrarUsuario (View view){
        String nombre = String.valueOf(et_nombre.getText());
        String dni = String.valueOf(et_dni.getText());
        int nivel = Integer.parseInt(String.valueOf(n_picker.getSelectedItem()));

        if(nombre.isEmpty()){
            Toast.makeText(this,"El nombre no puede estar en blanco", Toast.LENGTH_SHORT).show();
        }
        else if (dni.isEmpty()){
            Toast.makeText(this,"El DNI no puede estar en blanco",Toast.LENGTH_SHORT).show();
        }
        else if (dni.equalsIgnoreCase("admin")|| nombre.equalsIgnoreCase("admin")){
            Toast.makeText(this,"El DNI o nombre no puede ser 'admin'",Toast.LENGTH_SHORT).show();

        }
        else {
            usuarioDAO.insertarUsuario(new Usuario(nombre,dni,nivel,true));
            Toast.makeText(this,"usuario introducido corrrectamente",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, grabarUsuario.class);
            intent.putExtra("dni",dni);
            startActivity(intent);
        }


    }
}