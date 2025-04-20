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

public class DataMatrixGenerator extends AppCompatActivity {

    private Button generateButton;
    private ImageView dataMatrixImageView, backButton;
    private EditText inputEditText;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_matrix_generator);

        generateButton = findViewById(R.id.btnGenerate);
        dataMatrixImageView = findViewById(R.id.ivDataMatrix);
        inputEditText = findViewById(R.id.etInputData);
        loadingProgressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.btnBack);

        loadingProgressBar.setVisibility(View.GONE);

        // Back button functionality
        backButton.setOnClickListener(view -> finish());

        TextInputEditText inputDataEditText = findViewById(R.id.etInputData);
        inputDataEditText.requestFocus();

        // Generate Data Matrix button functionality
        generateButton.setOnClickListener(view -> {
            String inputText = inputEditText.getText().toString();
            if (!inputText.isEmpty()) {
                // Show loading spinner
                loadingProgressBar.setVisibility(View.VISIBLE);

                // Clear the previous Data Matrix
                dataMatrixImageView.setImageBitmap(null);

                // Generate the new Data Matrix in a separate thread to avoid blocking the UI
                new Thread(() -> {
                    try {
                        Bitmap dataMatrixBitmap = generateDataMatrix(inputText);
                        runOnUiThread(() -> {
                            // Hide the loading spinner
                            loadingProgressBar.setVisibility(View.GONE);

                            // Set the newly generated Data Matrix to the ImageView
                            dataMatrixImageView.setImageBitmap(dataMatrixBitmap);
                        });
                    } catch (WriterException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            // Hide the loading spinner if there's an error
                            loadingProgressBar.setVisibility(View.GONE);
                            Toast.makeText(DataMatrixGenerator.this, "Error generating Data Matrix", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).start();
            } else {
                Toast.makeText(DataMatrixGenerator.this, "Please enter text to generate Data Matrix", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap generateDataMatrix(String text) throws WriterException {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();

            // Set the margin and character encoding
            hints.put(EncodeHintType.MARGIN, 2); // Set the margin for Data Matrix
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // Support for multi-language characters

            // Encode the text into a Data Matrix BitMatrix
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.DATA_MATRIX, 400, 400, hints);

            // Create a Bitmap from the BitMatrix
            Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.RGB_565);

            // Set pixel colors based on the BitMatrix
            for (int x = 0; x < 400; x++) {
                for (int y = 0; y < 400; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ?
                            0xFF000000 : // Black for data bits
                            0xFFFFFFFF);  // White for empty spaces
                }
            }

            return bitmap;
        } catch (IllegalArgumentException e) {
            // Handle unsupported characters
            runOnUiThread(() ->
                    Toast.makeText(DataMatrixGenerator.this, "Unsupported characters in text", Toast.LENGTH_SHORT).show()
            );
            return null;
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            runOnUiThread(() ->
                    Toast.makeText(DataMatrixGenerator.this, "Error generating Data Matrix", Toast.LENGTH_SHORT).show()
            );
            return null;
        }
    }

}
