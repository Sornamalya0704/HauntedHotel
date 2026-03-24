package com.ctf.hauntedhotel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Room105Activity extends AppCompatActivity {

    private TextView ghostMessage;
    private EditText flagInput;
    private Button checkoutButton;
    private Button backButton;
    private ImageView ghostImage;
    private View doorView;
    private TextView roomNumber;
    private TextView ghostName;
    private TextView progressText;

    private int failedAttempts = 0;
    private boolean isLocked = false;
    private SharedPreferences prefs;

    private MediaPlayer thunderPlayer;
    private MediaPlayer successPlayer;
    private MediaPlayer whisperPlayer;

    // PLACEHOLDER FLAG - Replace with real flag later
    private static final String CORRECT_FLAG = "test123";

    private static final int ROOM_NUM = 105;
    private static final String GHOST_NAME = "The Crying Lady";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_template);

        prefs = getSharedPreferences("hotel_evidence", Context.MODE_PRIVATE);

        if (prefs.getBoolean("room" + ROOM_NUM + "_checkedout", false)) {
            showAlreadyCheckedOut();
            return;
        }

        initializeViews();
        setupRoomDetails();
        setupListeners();
        initializeSounds();
        startGhostAnimation();
        playThunderWithVibration();
        updateProgressCounter();
    }

    private void initializeViews() {
        ghostMessage = findViewById(R.id.ghostMessage);
        flagInput = findViewById(R.id.flagInput);
        checkoutButton = findViewById(R.id.checkoutButton);
        backButton = findViewById(R.id.backButton);
        ghostImage = findViewById(R.id.ghostImage);
        doorView = findViewById(R.id.doorView);
        roomNumber = findViewById(R.id.roomNumber);
        ghostName = findViewById(R.id.ghostName);
        progressText = findViewById(R.id.progressText);
    }

    private void setupRoomDetails() {
        roomNumber.setText("ROOM " + ROOM_NUM);
        ghostName.setText(GHOST_NAME);
        TextView roomTitle = findViewById(R.id.roomTitle);
        if (roomTitle != null) {
            roomTitle.setText("🚪 ROOM " + ROOM_NUM + " 🚪");
        }
    }

    private void setupListeners() {
        checkoutButton.setOnClickListener(v -> checkFlag());
        backButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    private void initializeSounds() {
        try {
            thunderPlayer = MediaPlayer.create(this, R.raw.thunder);
            if (thunderPlayer != null) thunderPlayer.setVolume(0.9f, 0.9f);
            successPlayer = MediaPlayer.create(this, R.raw.success);
            if (successPlayer != null) successPlayer.setVolume(1.0f, 1.0f);
            whisperPlayer = MediaPlayer.create(this, R.raw.whisper);
            if (whisperPlayer != null) whisperPlayer.setVolume(0.6f, 0.6f);
        } catch (Exception e) {}
    }

    private void playThunderWithVibration() {
        if (thunderPlayer != null) {
            try {
                if (thunderPlayer.isPlaying()) thunderPlayer.stop();
                thunderPlayer.start();
            } catch (Exception e) {}
        }
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(500);
        }
        flashScreen();
    }

    private void playSuccessSound() {
        if (successPlayer != null) {
            try {
                if (successPlayer.isPlaying()) successPlayer.stop();
                successPlayer.start();
            } catch (Exception e) {}
        }
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(200);
        }
    }

    private void playWhisperSound() {
        if (whisperPlayer != null) {
            try {
                if (whisperPlayer.isPlaying()) whisperPlayer.stop();
                whisperPlayer.start();
            } catch (Exception e) {}
        }
    }

    private void flashScreen() {
        try {
            final View flashOverlay = new View(this);
            flashOverlay.setBackgroundColor(0xFFFFFFFF);
            final ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
            rootView.addView(flashOverlay, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            flashOverlay.animate().alpha(0f).setDuration(300)
                    .withEndAction(() -> {
                        try { rootView.removeView(flashOverlay); } catch (Exception e) {}
                    }).start();
        } catch (Exception e) {}
    }

    private void startGhostAnimation() {
        AlphaAnimation fadeInOut = new AlphaAnimation(0.6f, 1.0f);
        fadeInOut.setDuration(2000);
        fadeInOut.setRepeatMode(Animation.REVERSE);
        fadeInOut.setRepeatCount(Animation.INFINITE);
        ghostImage.startAnimation(fadeInOut);
    }

    private void updateProgressCounter() {
        int checkedOut = 0;
        int[] rooms = {101, 102, 103, 104, 105, 106, 107, 108};
        for (int room : rooms) {
            if (prefs.getBoolean("room" + room + "_checkedout", false)) {
                checkedOut++;
            }
        }
        progressText.setText(checkedOut + "/8 GUESTS");
    }

    private void checkFlag() {
        if (isLocked) {
            Toast.makeText(this, "The ghost is angry! Wait a moment...", Toast.LENGTH_SHORT).show();
            return;
        }
        String enteredFlag = flagInput.getText().toString().trim();
        if (enteredFlag.equals(CORRECT_FLAG)) {
            handleCorrectFlag();
        } else {
            handleWrongFlag();
        }
    }

    private void handleCorrectFlag() {
        playSuccessSound();
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(1000);
        fadeOut.setFillAfter(true);
        ghostImage.startAnimation(fadeOut);
        ghostImage.setVisibility(View.INVISIBLE);
        RotateAnimation rotateDoor = new RotateAnimation(0, 90,
                RotateAnimation.RELATIVE_TO_SELF, 0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateDoor.setDuration(600);
        rotateDoor.setFillAfter(true);
        doorView.startAnimation(rotateDoor);
        doorView.setVisibility(View.INVISIBLE);
        ghostMessage.setText("✨ \"Thank you... I can finally rest...\" ✨");
        ghostMessage.setTextColor(0xFF4CAF50);
        flagInput.setEnabled(false);
        checkoutButton.setEnabled(false);
        prefs.edit().putBoolean("room" + ROOM_NUM + "_checkedout", true).apply();
        Toast.makeText(this, "✨ The ghost fades away with a smile! ✨", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Room105Activity.this, CheckoutSuccessActivity.class);
            intent.putExtra("ROOM_NUMBER", ROOM_NUM);
            intent.putExtra("ROOM_NAME", GHOST_NAME);
            intent.putExtra("FLAG", CORRECT_FLAG);
            startActivity(intent);
            finish();
        }, 2000);
    }

    private void handleWrongFlag() {
        failedAttempts++;
        playWhisperSound();
        TranslateAnimation shake = new TranslateAnimation(-10, 10, 0, 0);
        shake.setDuration(100);
        shake.setRepeatCount(5);
        shake.setRepeatMode(Animation.REVERSE);
        flagInput.startAnimation(shake);
        ghostImage.startAnimation(shake);
        doorView.startAnimation(shake);
        Toast.makeText(this, "❌ Wrong code! The ghost shakes its head...", Toast.LENGTH_SHORT).show();
        if (failedAttempts == 1) {
            ghostMessage.setText("👻 \"That's not right... Try again...\" 👻");
            ghostMessage.setTextColor(0xFFFF6666);
        } else if (failedAttempts == 2) {
            ghostMessage.setText("😠 \"You're disturbing my peace...\" 😠");
            ghostImage.setAlpha(0.8f);
        } else if (failedAttempts >= 3) {
            lockRoom(30000);
        }
        flagInput.setText("");
    }

    private void lockRoom(int durationMillis) {
        isLocked = true;
        flagInput.setEnabled(false);
        checkoutButton.setEnabled(false);
        ghostMessage.setText("😤 \"ENOUGH! I need time to calm down...\" 😤");
        ghostMessage.setTextColor(0xFFFF0000);
        final int seconds = durationMillis / 1000;
        new Thread(() -> {
            for (int i = seconds; i > 0; i--) {
                final int remaining = i;
                runOnUiThread(() -> {
                    ghostMessage.setText("😤 The ghost is angry! Try again in " + remaining + " seconds... 😤");
                });
                try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
            }
            runOnUiThread(() -> {
                isLocked = false;
                flagInput.setEnabled(true);
                checkoutButton.setEnabled(true);
                flagInput.setText("");
                ghostMessage.setText("👻 \"Try again... but be respectful...\" 👻");
                ghostMessage.setTextColor(0xFFCCCCCC);
                ghostImage.setAlpha(1.0f);
                failedAttempts = 0;
            });
        }).start();
    }

    private void showAlreadyCheckedOut() {
        setContentView(R.layout.activity_room_template);
        findViewById(R.id.flagInput).setVisibility(View.GONE);
        findViewById(R.id.checkoutButton).setVisibility(View.GONE);
        TextView msg = findViewById(R.id.ghostMessage);
        msg.setText("This room is now vacant.\nThe ghost has moved on.");
        msg.setTextColor(0xFF4CAF50);
        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thunderPlayer != null) { thunderPlayer.release(); thunderPlayer = null; }
        if (successPlayer != null) { successPlayer.release(); successPlayer = null; }
        if (whisperPlayer != null) { whisperPlayer.release(); whisperPlayer = null; }
    }
}