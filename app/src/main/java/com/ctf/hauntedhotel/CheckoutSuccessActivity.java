package com.ctf.hauntedhotel;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CheckoutSuccessActivity extends AppCompatActivity {

    private TextView receiptFlag;
    private Button collectEvidenceBtn;
    private Button backToLobbyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_success);

        int roomNumber = getIntent().getIntExtra("ROOM_NUMBER", 0);
        String roomName = getIntent().getStringExtra("ROOM_NAME");
        String flag = getIntent().getStringExtra("FLAG");

        receiptFlag = findViewById(R.id.receiptFlag);
        collectEvidenceBtn = findViewById(R.id.collectEvidenceBtn);
        backToLobbyBtn = findViewById(R.id.backToLobbyBtn);

        ((TextView) findViewById(R.id.receiptRoom)).setText("Room: " + roomNumber);
        ((TextView) findViewById(R.id.receiptGuest)).setText("Guest: " + roomName);
        receiptFlag.setText(flag);

        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.receipt_slide);
        findViewById(R.id.receiptContainer).startAnimation(slideIn);

        collectEvidenceBtn.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("CTF Flag", flag);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "📋 Flag copied to clipboard!", Toast.LENGTH_SHORT).show();
        });

        backToLobbyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutSuccessActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}