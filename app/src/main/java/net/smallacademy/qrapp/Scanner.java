package net.smallacademy.qrapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Scanner extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView scannView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        scannView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this,scannView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                                Scanner.this, R.style.BottomSheetDialogTheme
                        );
                        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                                .inflate(
                                        R.layout.activity_information,
                                        (LinearLayout) findViewById((R.id.bottomActivity)
                                        )
                                );
                        bottomSheetDialog.setContentView(bottomSheetView);
                        Bundle args = new Bundle();
                        TextView tv = (TextView) bottomSheetDialog.findViewById(R.id.name);
                        String temp = result.getText();
                        ImageView img = (ImageView)bottomSheetDialog.findViewById(R.id.photo);
                        String id = "i" + temp.substring(0,4);
                        int imageResource = getResources().getIdentifier(id, "drawable", getPackageName());
                        img.setImageResource(imageResource);
                        tv.setText(temp.substring(4));
                        bottomSheetDialog.show();
                    }
                });
            }
        });


        scannView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();

    }

    public void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(Scanner.this, "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }


}
