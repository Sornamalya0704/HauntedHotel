package com.ctf.hauntedhotel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView hotelImage;
    private TextView hotelName;
    private TextView hotelTagline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hotelImage = findViewById(R.id.hotelImage);
        hotelName = findViewById(R.id.hotelName);
        hotelTagline = findViewById(R.id.hotelTagline);

        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

        if (hotelImage != null) {
            hotelImage.startAnimation(zoomIn);
        }

        new Handler().postDelayed(() -> {
            if (hotelName != null) {
                hotelName.setVisibility(TextView.VISIBLE);
                hotelName.startAnimation(fadeIn);
            }
        }, 1200);

        new Handler().postDelayed(() -> {
            if (hotelTagline != null) {
                hotelTagline.setVisibility(TextView.VISIBLE);
                hotelTagline.startAnimation(fadeIn);
            }
        }, 1800);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 3200);
    }
}