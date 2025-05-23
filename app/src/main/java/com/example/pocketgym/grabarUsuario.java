package com.example.pocketgym;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class grabarUsuario extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private String dni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabar_usuario);

        dni = getIntent().getStringExtra("dni");
        if (dni == null) dni = "DNI no recibido";

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "Este dispositivo no soporta NFC", Toast.LENGTH_LONG).show();
            finish();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        IntentFilter[] filters = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag != null) {
                writeNfcTag(tag, dni);
            }
        }
    }

    private void writeNfcTag(Tag tag, String message) {
        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            Toast.makeText(this, "Etiqueta NFC no soporta NDEF", Toast.LENGTH_SHORT).show();
            return;
        }

        NdefRecord ndefRecord = NdefRecord.createTextRecord("en", message);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

        try {
            ndef.connect();
            if (!ndef.isWritable()) {
                Toast.makeText(this, "Etiqueta NFC no es escribible", Toast.LENGTH_SHORT).show();
                return;
            }

            ndef.writeNdefMessage(ndefMessage);
            Toast.makeText(this, "DNI escrito en etiqueta NFC", Toast.LENGTH_LONG).show();
        } catch (IOException | android.nfc.FormatException e) {
            Toast.makeText(this, "Error escribiendo en la etiqueta NFC", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            try {
                ndef.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}