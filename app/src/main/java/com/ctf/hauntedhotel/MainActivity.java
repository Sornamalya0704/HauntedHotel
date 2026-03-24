package com.ctf.hauntedhotel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView counterText;
    private SharedPreferences prefs;
    private int checkedOutCount = 0;

    private Button door101, door102, door103, door104;
    private Button door105, door106, door107, door108;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("hotel_evidence", Context.MODE_PRIVATE);

        initializeViews();
        updateDoorStates();
        updateCounter();
        setupDoorListeners();
    }

    private void initializeViews() {
        counterText = findViewById(R.id.counterText);

        door101 = findViewById(R.id.door101);
        door102 = findViewById(R.id.door102);
        door103 = findViewById(R.id.door103);
        door104 = findViewById(R.id.door104);
        door105 = findViewById(R.id.door105);
        door106 = findViewById(R.id.door106);
        door107 = findViewById(R.id.door107);
        door108 = findViewById(R.id.door108);
    }

    private void updateDoorStates() {
        updateDoorState(door101, 101);
        updateDoorState(door102, 102);
        updateDoorState(door103, 103);
        updateDoorState(door104, 104);
        updateDoorState(door105, 105);
        updateDoorState(door106, 106);
        updateDoorState(door107, 107);
        updateDoorState(door108, 108);
    }

    private void updateDoorState(Button door, int roomNumber) {
        boolean isCheckedOut = prefs.getBoolean("room" + roomNumber + "_checkedout", false);

        if (isCheckedOut) {
            door.setText("✓ CHECKED OUT");
            door.setBackgroundResource(R.drawable.checked_button);
            door.setTextColor(0xFF4CAF50);
            door.setEnabled(false);
        } else {
            door.setText("ENTER");
            door.setBackgroundResource(R.drawable.room_button);
            door.setTextColor(0xFFD4A373);
            door.setEnabled(true);
        }
    }

    private void updateCounter() {
        checkedOutCount = 0;
        int[] rooms = {101, 102, 103, 104, 105, 106, 107, 108};
        for (int room : rooms) {
            if (prefs.getBoolean("room" + room + "_checkedout", false)) {
                checkedOutCount++;
            }
        }
        counterText.setText(checkedOutCount + "/8");
    }

    private void setupDoorListeners() {
        door101.setOnClickListener(v -> openRoom(101));
        door102.setOnClickListener(v -> openRoom(102));
        door103.setOnClickListener(v -> openRoom(103));
        door104.setOnClickListener(v -> openRoom(104));
        door105.setOnClickListener(v -> openRoom(105));
        door106.setOnClickListener(v -> openRoom(106));
        door107.setOnClickListener(v -> openRoom(107));
        door108.setOnClickListener(v -> openRoom(108));
    }

    private void openRoom(int roomNumber) {
        boolean isCheckedOut = prefs.getBoolean("room" + roomNumber + "_checkedout", false);

        if (isCheckedOut) {
            Toast.makeText(this, "Room " + roomNumber + " is already vacant.", Toast.LENGTH_SHORT).show();
            return;
        }

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        Button door = getDoorButton(roomNumber);
        if (door != null) {
            door.startAnimation(shake);
        }

        Intent intent = new Intent(MainActivity.this, getRoomActivityClass(roomNumber));
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private Button getDoorButton(int roomNumber) {
        switch(roomNumber) {
            case 101: return door101;
            case 102: return door102;
            case 103: return door103;
            case 104: return door104;
            case 105: return door105;
            case 106: return door106;
            case 107: return door107;
            case 108: return door108;
            default: return null;
        }
    }

    private Class<?> getRoomActivityClass(int roomNumber) {
        switch(roomNumber) {
            case 101: return Room101Activity.class;
            case 102: return Room102Activity.class;
            case 103: return Room103Activity.class;
            case 104: return Room104Activity.class;
            case 105: return Room105Activity.class;
            case 106: return Room106Activity.class;
            case 107: return Room107Activity.class;
            case 108: return Room108Activity.class;
            default: return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDoorStates();
        updateCounter();
    }
}