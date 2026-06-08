package com.malak.gps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private Button btnOuvrirCarte;
    private TextView txtDateLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOuvrirCarte = findViewById(R.id.btnOuvrirCarte);
        txtDateLab = findViewById(R.id.txtDateLab);

        String dateDuJour = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH)
                .format(new Date());

        txtDateLab.setText("LAB 11 • GPS & Google Maps • " + dateDuJour);

        btnOuvrirCarte.setOnClickListener(v -> {
            Intent intentCarte = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intentCarte);
        });
    }
}