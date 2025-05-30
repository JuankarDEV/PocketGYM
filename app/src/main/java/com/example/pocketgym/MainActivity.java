package com.example.pocketgym;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pocketgym.Conexion.ConexionBBDD;
import com.example.pocketgym.Dao.UsuarioDao;
import com.example.pocketgym.Models.Usuario;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private View scanLine;
    private FrameLayout scanArea;
    private Button scanButton;
    private boolean isScanning = false;
    private ObjectAnimator animator;
    private UsuarioDao usuarioDAO;
    private ConexionBBDD conexionDB;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    private boolean isActivityResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanLine = findViewById(R.id.scanLine);
        scanArea = findViewById(R.id.scan_area);
        scanButton = findViewById(R.id.scanButton);

        conexionDB = new ConexionBBDD();
        usuarioDAO = new UsuarioDao(conexionDB);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "Este dispositivo no soporta NFC.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC no está habilitado. Por favor, habilítelo en la configuración.", Toast.LENGTH_SHORT).show();
        }

        pendingIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityResumed = true;
        if (isScanning) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityResumed = false;
        if (isScanning) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        pararAnimacionEscanear();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        readNfcTag(intent);
    }

    public void alternarEscaner(View view) {
        if (nfcAdapter == null || !nfcAdapter.isEnabled()) {
            mostrarPopupNfcDeshabilitado();
            return;
        }

        if (isScanning) {
            pararAnimacionEscanear();
            if (isActivityResumed) {
                nfcAdapter.disableForegroundDispatch(this);
            }
            isScanning = false;
        } else {
            iniciarAnimacionEscaner();
            if (isActivityResumed) {
                nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
            } else {
                Toast.makeText(this, "La actividad no está en primer plano. Reintente.", Toast.LENGTH_SHORT).show();
                return;
            }
            isScanning = true;
        }
    }

    private void iniciarAnimacionEscaner() {
        scanLine.setVisibility(View.VISIBLE);
        animator = ObjectAnimator.ofFloat(scanLine, "translationY", 0, scanArea.getHeight());
        animator.setDuration(1500);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
        scanButton.setText("Detener");
    }

    private void pararAnimacionEscanear() {
        if (animator != null) {
            animator.cancel();
        }
        scanLine.setVisibility(View.GONE);
        scanButton.setText("Escanear");
    }

    private void mostrarPopupNfcDeshabilitado() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.pop_up_personalizado, null);
        TextView txtMensaje = popupView.findViewById(R.id.tw_nombre);
        txtMensaje.setText("NFC no está habilitado. Por favor, habilítelo en la configuración.");
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }

    private void readNfcTag(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) return;

        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            Toast.makeText(this, "Etiqueta NFC no compatible con NDEF", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            if (ndefMessage != null && ndefMessage.getRecords().length > 0) {
                String message = new String(ndefMessage.getRecords()[0].getPayload(), StandardCharsets.UTF_8);
                message = message.substring(3); // Saltar el idioma
              //  Toast.makeText(this, "Mensaje NFC leído: " + message, Toast.LENGTH_SHORT).show();
                buscarUsuario(message.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al leer la etiqueta NFC", Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarUsuario(String codigoNfc) {
        Usuario usuario = usuarioDAO.obtenerUsuarioPorNFC(codigoNfc);

        if (usuario != null) {
            Intent intent;
            if (usuario.getCodigoNfc().equals("Admin")) {
              //  Toast.makeText(this, "Admin encontrado: " + usuario.getNombre(), Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this, AdminMenu.class);
            } else {
               // Toast.makeText(this, "Usuario encontrado: " + usuario.getNombre(), Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this, UsuarioMenu.class);
            }

            intent.putExtra("usuario_id", usuario.getId());
            intent.putExtra("usuario_nombre", usuario.getNombre());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
        }
    }
}
