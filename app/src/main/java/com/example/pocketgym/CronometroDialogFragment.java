package com.example.pocketgym;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;



import java.util.Locale;

public class CronometroDialogFragment extends DialogFragment {

    private TextView timerText;
    private ImageButton btnUp, btnDown;
    private Button startButton;

    private long timeInMillis = 30000;
    private CountDownTimer countDownTimer;

    private MediaPlayer reproductorMedia_cronometro;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cronometro, container, false);
        crearCanalNotificaciones();
        timerText = view.findViewById(R.id.timerText);
        btnUp = view.findViewById(R.id.btnUp);
        btnDown = view.findViewById(R.id.btnDown);
        startButton = view.findViewById(R.id.startButton);

        reproductorMedia_cronometro = MediaPlayer.create(requireContext(), R.raw.funny_alarm);
        reproductorMedia_cronometro.setVolume(1,1);

        updateTimerText();

        btnUp.setOnClickListener(v -> {
            timeInMillis += 30000; // +30s
            updateTimerText();
        });

        btnDown.setOnClickListener(v -> {
            if (timeInMillis > 30000) {
                timeInMillis -= 30000;
                updateTimerText();
            }
        });

        startButton.setOnClickListener(v -> startTimer());

        return view;
    }

    private void updateTimerText() {
        int minutes = (int) (timeInMillis / 1000) / 60;
        int seconds = (int) (timeInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerText.setText(timeFormatted);
    }

    private void startTimer() {
        startButton.setEnabled(false);
        btnUp.setEnabled(false);
        btnDown.setEnabled(false);

        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timeInMillis = millisUntilFinished;
                updateTimerText();
            }

            public void onFinish() {
                reproductorMedia_cronometro.start();
                mostrarNotificacion("Tiempo de descanso cumplido,a darle duro");
                startButton.setEnabled(true);
                btnUp.setEnabled(true);
                btnDown.setEnabled(true);
                timeInMillis = 30000;
                updateTimerText();
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "gym_channel",
                    "Notificaciones de Gym",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Se mostrara cuando el cronómetro termine");
            NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void mostrarNotificacion(String mensaje) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "gym_channel")
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("¡Tiempo terminado!")
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                return;
            }
        }
        notificationManager.notify(1001, builder.build());
    }

}
