package com.eagle.qrprogenerator;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class menunav extends AppCompatActivity {
Button contactUs , privacyPolicy  , main ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menunav);
        contactUs = findViewById(R.id.contact_us_button);
        privacyPolicy = findViewById(R.id.privacy_policy_button);
        main = findViewById(R.id.btnMain);
        privacyPolicy.setOnClickListener(v -> showTermsPopup());
        contactUs.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"alhassan.khalilnew@gmail.com"});
            try {
                startActivity(Intent.createChooser(emailIntent, "Send Email"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No email client installed.", Toast.LENGTH_SHORT).show();
            }


            });


        main.setOnClickListener(v-> {
            Intent i = new Intent(menunav.this, MainActivity.class);
            startActivity(i);
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void showTermsPopup() {
        // Create a custom AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.terms_policy_dialog, null);
        builder.setView(dialogView);

        // Set the "Close" button with custom styling
        builder.setNeutralButton("Close", (dialog, which) -> dialog.dismiss());

        // Create and show the dialog
        AlertDialog dialog = builder.create();

        // Customize the button text color after the dialog is shown
        dialog.setOnShowListener(dialogInterface -> {
            Button closeButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            if (closeButton != null) {
                closeButton.setTextColor(Color.BLACK);
            }
        });

        dialog.show();
    }
}