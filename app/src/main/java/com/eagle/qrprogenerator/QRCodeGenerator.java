package com.eagle.qrprogenerator;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

public class QRCodeGenerator extends AppCompatActivity {

    Button generateButton;
    ImageView qrCodeImageView, Back;
    EditText inputEditText;
    ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);

        generateButton = findViewById(R.id.btnGenerate);
        qrCodeImageView = findViewById(R.id.ivQRCode);
        inputEditText = findViewById(R.id.etInputData);
        loadingProgressBar = findViewById(R.id.progressBar);
        Back = findViewById(R.id.btnBack);

        loadingProgressBar.setVisibility(View.GONE);

        Back.setOnClickListener(view -> finish());
        TextInputEditText inputDataEditText = findViewById(R.id.etInputData);
        inputDataEditText.requestFocus();

        generateButton.setOnClickListener(view -> {
            String inputText = inputEditText.getText().toString();
            if (!inputText.isEmpty()) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                qrCodeImageView.setImageBitmap(null);

                new Thread(() -> {
                    try {
                        Bitmap qrCodeBitmap = generateQRCode(inputText);
                        runOnUiThread(() -> {
                            loadingProgressBar.setVisibility(View.GONE);
                            qrCodeImageView.setImageBitmap(qrCodeBitmap);

                        });
                    } catch (WriterException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            loadingProgressBar.setVisibility(View.GONE);
                            Toast.makeText(QRCodeGenerator.this, "Error generating QR Code", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).start();
            } else {
                Toast.makeText(QRCodeGenerator.this, "Please enter text to generate QR Code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap generateQRCode(String text) throws WriterException {
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();

        // Set margin and character encoding
        hints.put(EncodeHintType.MARGIN, 1); // Reduce margin for a more compact QR code
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // Support for multi-language characters

        // Generate the BitMatrix for the QR Code
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 500, 500, hints);

        // Create a Bitmap from the BitMatrix
        Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565);

        // Set pixel colors based on the BitMatrix
        for (int x = 0; x < 500; x++) {
            for (int y = 0; y < 500; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ?
                        0xFF000000 : // Black for data bits
                        0xFFFFFFFF);  // White for empty spaces
            }
        }

        return bitmap;
    }



}
