package com.eagle.qrprogenerator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
Button QRCG, BCG, data, astaz;
ImageButton nav ;
TextView contactus ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        QRCG = findViewById(R.id.ibQRCG);
        BCG = findViewById(R.id.ibBCG);
        data = findViewById(R.id.ibdata);
        astaz = findViewById(R.id.ibastez);
        nav = findViewById(R.id.nav);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        QRCG.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, QRCodeGenerator.class));
        });
        BCG.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BarcodeGenerator.class));
        });
        data.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, DataMatrixGenerator.class));

        });
        astaz.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, aztec.class));

        });
        nav.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, menunav.class));

        });
    }


}