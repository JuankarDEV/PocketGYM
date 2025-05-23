package com.example.pocketgym;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pocketgym.Conexion.ConexionBBDD;
import com.example.pocketgym.Dao.UsuarioDao;
import com.example.pocketgym.Models.Usuario;

public class UsuarioMenu extends AppCompatActivity {

    private TextView tw_usuario;
    private Button btn_rutina;
    private Button btn_seg;
    private Button btn_his;

    private ProgressBar progressBar;
    private ImageView weightIcon;
    private TextView progressText;

    private UsuarioDao usuarioDAO;
    private ConexionBBDD conexionDB;
    int id_usuario;
    private Usuario infouser;
    private String usuario;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usuario_menu);

        conexionDB = new ConexionBBDD();
        usuarioDAO = new UsuarioDao(conexionDB);

        // usuario = getIntent().getStringExtra("usuario_nombre");
      //    id_usuario = getIntent().getIntExtra("usuario_id",0);


     usuario = "Juan Pérez";
     id_usuario = 1;

        infouser = usuarioDAO.obtenerUsuarioporid(id_usuario);

        tw_usuario = findViewById(R.id.tvNombre);
        tw_usuario.setText(usuario);
        popupbienvenida(usuario);

        btn_rutina = findViewById(R.id.btn_rutina);
        btn_his = findViewById(R.id.btn_his);
        btn_seg = findViewById(R.id.btn_seg);

        View.OnTouchListener animTouchListener = (view, evento) -> {
            switch (evento.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    view.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false;
        };

        btn_rutina.setOnTouchListener(animTouchListener);
        btn_seg.setOnTouchListener(animTouchListener);
        btn_his.setOnTouchListener(animTouchListener);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void popupbienvenida(String usuario) {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            LayoutInflater inflater = LayoutInflater.from(this);
            View popupView = inflater.inflate(R.layout.pop_up_personalizado, null);

            TextView txtMensaje = popupView.findViewById(R.id.tw_nombre);
            ImageView img_popup = popupView.findViewById(R.id.imgIconopopup);


            img_popup.setImageResource(R.mipmap.strength);
            txtMensaje.setText("Bienvenido " + usuario);

            PopupWindow popupEmergente = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
            );

            popupEmergente.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);

        }, 100);
    }

    public void go_Rutina(View view) {
        Intent intent = new Intent(this, RutinaEjercicios.class);
        intent.putExtra("usuario_id", id_usuario);
        startActivity(intent);
    }

    public void go_Seguimiento(View view) {
        Intent intent = new Intent(this, DiasActivity.class);
        intent.putExtra("usuario_id", id_usuario);
        startActivity(intent);
    }

    public void go_Historial(View view) {
        boolean modo_historial = true;
        Intent intent = new Intent(this, DiasActivity.class);
        intent.putExtra("usuario_id", id_usuario);
        intent.putExtra("modo_historial",modo_historial);
        startActivity(intent);
    }

    public void PopupInfo(View view) {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            LayoutInflater inflater = LayoutInflater.from(this);
            View popupView = inflater.inflate(R.layout.pop_up_infouser, null);

            TextView txt_nombre = popupView.findViewById(R.id.tw_info_nombre);
            TextView txt_nivel = popupView.findViewById(R.id.tw_info_nivel);
            TextView txt_activo = popupView.findViewById(R.id.tw_info_activo);
            TextView txt_fecha = popupView.findViewById(R.id.tw_info_fecha);

            progressBar = popupView.findViewById(R.id.progressBar);
            weightIcon = popupView.findViewById(R.id.weightIcon);
            progressText = popupView.findViewById(R.id.progressText);
            updateProgress(usuarioDAO.ObtenerEjerciciosCompletados(id_usuario));

            txt_nombre.setText(infouser.getNombre());
            txt_nivel.setText("Nivel: " + infouser.getNivel());

            if (infouser.isSuscripcionActiva()) {
                txt_activo.setText("Activo");
                txt_activo.setTextColor(getColor(R.color.laser));
            } else {
                txt_activo.setText("Inactivo");
                txt_activo.setTextColor(getColor(com.google.android.material.R.color.design_default_color_error));
            }

            txt_fecha.setText(String.valueOf(infouser.getFechaVencimiento()));

            PopupWindow popupEmergente = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
            );

            popupEmergente.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
        }, 100);
    }

    private void updateProgress(int progress) {
        if (progressBar == null || weightIcon == null || progressText == null) return;

        progressBar.setProgress(progress);
        progressText.setText(progress + "/50");

        progressBar.post(() -> {
            int max = progressBar.getMax();
            int barWidth = progressBar.getWidth();
            int iconWidth = weightIcon.getWidth();

            float ratio = (float) progress / max;
            float positionX = ratio * (barWidth - iconWidth);

            weightIcon.setTranslationX(positionX);

            if (progress < 15) {
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                weightIcon.setImageResource(R.drawable.ronnie_enfadado);
            } else if (progress < 35) {
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                weightIcon.setImageResource(R.drawable.ronnie_neutral);
            } else {
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                weightIcon.setImageResource(R.drawable.ronnie_feliz);
            }
        });
    }
}
