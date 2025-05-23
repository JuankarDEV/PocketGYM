package com.example.pocketgym;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pocketgym.Conexion.ConexionBBDD;
import com.example.pocketgym.Dao.UsuarioDao;

public class ImagenTabla extends AppCompatActivity {
    int id_usuario;
    ImageView tablaimg;

    private UsuarioDao usuarioDAO;
    private ConexionBBDD conexionDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_imagen_tabla);

        conexionDB = new ConexionBBDD();
        usuarioDAO = new UsuarioDao(conexionDB);

        tablaimg = findViewById(R.id.imagenTabla);

        id_usuario = getIntent().getIntExtra("usuario_id",0);

        String nombreImagen = usuarioDAO.obtenerImagenTabla(id_usuario);

        int resID = getResources().getIdentifier(nombreImagen, "drawable", getPackageName());
        Drawable drawable = ContextCompat.getDrawable(this, resID);

        tablaimg.setImageDrawable(drawable);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}