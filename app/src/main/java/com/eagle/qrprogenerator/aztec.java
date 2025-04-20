package com.eagle.qrprogenerator;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

public class aztec extends AppCompatActivity {

    private Button generateButton;
    private ImageView aztecCodeImageView;
    private ImageButton backButton;
    private EditText inputEditText;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aztec); // Custom Aztec layout

        generateButton = findViewById(R.id.btnGenerateAztec);
        aztecCodeImageView = findViewById(R.id.ivAztecCode);
        inputEditText = findViewById(R.id.etAztecInput);
        loadingProgressBar = findViewById(R.id.aztecProgressBar);
        backButton = findViewById(R.id.btnAztecBack);

        loadingProgressBar.setVisibility(View.GONE);

        backButton.setOnClickListener(view -> finish());

        TextInputEditText inputDataEditText = findViewById(R.id.etAztecInput);
        inputDataEditText.requestFocus();

        generateButton.setOnClickListener(view -> {
            String inputText = inputEditText.getText().toString();
            if (!inputText.isEmpty()) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                aztecCodeImageView.setImageBitmap(null);

                new Thread(() -> {
                    try {
                        Bitmap aztecBitmap = generateAztecCode(inputText);
                        runOnUiThread(() -> {
                            loadingProgressBar.setVisibility(View.GONE);

                            aztecCodeImageView.setImageBitmap(aztecBitmap);
                        });
                    } catch (WriterException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            loadingProgressBar.setVisibility(View.GONE);
                            Toast.makeText(aztec.this, "Error generating Aztec Code", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).start();
            } else {
                Toast.makeText(aztec.this, "Please enter text to generate Aztec Code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap generateAztecCode(String text) throws WriterException {
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.MARGIN, 1); // Set the margin for the Aztec Code
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // Use UTF-8 encoding

        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.AZTEC, 800, 800, hints);
        Bitmap bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.RGB_565);

        for (int x = 0; x < 800; x++) {
            for (int y = 0; y < 800; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ?
                        ContextCompat.getColor(aztec.this, android.R.color.black) :
                        ContextCompat.getColor(aztec.this, android.R.color.white));
            }
        }

        return bitmap;
    }

}
