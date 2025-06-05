package com.example.pocketgym;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pocketgym.Conexion.ConexionBBDD;
import com.example.pocketgym.Dao.UsuarioDao;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoActivity extends AppCompatActivity {

    private UsuarioDao usuarioDAO;
    private ConexionBBDD conexionDB;

    private SimpleExoPlayer player;

    private TextView textView_ejercicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);
        String ejercicio = getIntent().getStringExtra("boton");
        conexionDB = new ConexionBBDD();
        usuarioDAO = new UsuarioDao(conexionDB);
        textView_ejercicio = findViewById(R.id.tw_nombre_video);
        textView_ejercicio.setText(ejercicio);
        String video_url = usuarioDAO.obtenerVideoEjercicio(ejercicio);
        PlayerView playerView = findViewById(R.id.videoView);
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        String videoUrl = "https://rfakleoxqnovsdfqzilz.supabase.co/storage/v1/object/public/videos//"+video_url+".mp4";
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
    }
}