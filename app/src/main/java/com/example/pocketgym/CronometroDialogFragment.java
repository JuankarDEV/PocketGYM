package com.example.pocketgym;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
}
