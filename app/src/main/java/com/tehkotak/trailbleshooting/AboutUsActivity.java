package com.tehkotak.trailbleshooting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

public class AboutUsActivity extends AppCompatActivity {

    private ImageView icBack;
    private LinearLayout aboutApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        icBack = findViewById(R.id.ic_back_aboutUs);
        aboutApp = findViewById(R.id.ly_aboutApp);
        final View ctxView = findViewById(R.id.cl_aboutUs);

        aboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(ctxView,
                        "Coming soon..",
                        Snackbar.LENGTH_LONG).show();
            }
        });
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(AboutUsActivity.this, MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
            }
        });
    }
}
