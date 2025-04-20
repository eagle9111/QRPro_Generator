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

import java.util.HashMap;
import java.util.Map;

public class BarcodeGenerator extends AppCompatActivity {

    private Button generateButton;
    private ImageView barcodeImageView, backButton;
    private EditText inputEditText;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_generator);

        // Initialize UI elements
        generateButton = findViewById(R.id.btnGenerate);
        barcodeImageView = findViewById(R.id.ivBarcode);
        inputEditText = findViewById(R.id.etInputData);
        loadingProgressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.btnBack);

        loadingProgressBar.setVisibility(View.GONE);
        TextInputEditText inputDataEditText = findViewById(R.id.etInputData);
        inputDataEditText.requestFocus();
        // Handle Back button click
        backButton.setOnClickListener(view -> finish());

        // Handle Generate button click
        generateButton.setOnClickListener(view -> {
            String inputText = inputEditText.getText().toString().trim();
            if (inputText.isEmpty()) {
                Toast.makeText(this, "Please enter text to generate a Barcode", Toast.LENGTH_SHORT).show();
                return;
            }

            if (inputText.length() > 35) {
                Toast.makeText(this, "Barcode length exceeds the limit of 35 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show loading spinner
            loadingProgressBar.setVisibility(View.VISIBLE);

            // Clear the previous Barcode
            barcodeImageView.setImageBitmap(null);

            // Generate the Barcode in a separate thread
            new Thread(() -> {
                try {
                    Bitmap barcodeBitmap = generateBarcode(inputText);
                    runOnUiThread(() -> {
                        // Hide the loading spinner
                        loadingProgressBar.setVisibility(View.GONE);

                        // Set the newly generated Barcode to the ImageView
                        if (barcodeBitmap != null) {
                            barcodeImageView.setImageBitmap(barcodeBitmap);
                        } else {
                            Toast.makeText(this, "Error generating Barcode", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (WriterException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        // Hide the loading spinner in case of error
                        loadingProgressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Error generating Barcode", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        });
    }

    /**
     * Generates a Barcode Bitmap based on the input text.
     *
     * @param text The text to encode in the Barcode.
     * @return A Bitmap representing the Barcode.
     * @throws WriterException If an error occurs during encoding.
     */
    private Bitmap generateBarcode(String text) throws WriterException {
        final int width = 800; // Barcode width
        final int height = 300; // Barcode height
        final int margin = 8; // Quiet zone margin

        // MultiFormatWriter for encoding
        MultiFormatWriter writer = new MultiFormatWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, margin); // Set margin
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // Ensure UTF-8 encoding

        try {
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.CODE_128, width, height, hints);

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            return bitmap;
        } catch (IllegalArgumentException e) {
            // Handle case where unsupported characters are in the text
            runOnUiThread(() -> Toast.makeText(this, "Unsupported characters in text", Toast.LENGTH_SHORT).show());
            return null;
        }
    }

}
