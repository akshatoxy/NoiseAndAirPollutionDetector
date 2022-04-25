package com.example.noisedetector;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.LinearLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout llLogout;
    LinearLayout llPushNotification;
    LinearLayout llVibrate;
    SwitchCompat pushNotification;
    SwitchCompat vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle("Settings");

        llLogout = findViewById(R.id.ll_logout);
        llPushNotification = findViewById(R.id.ll_push_notification);
        llVibrate = findViewById(R.id.ll_vibrate);
        pushNotification = findViewById(R.id.tv_notifications_push);
        vibrate = findViewById(R.id.tv_notifications_vibrate);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = sharedPreferences.edit();

        llLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myEditor.putString(LoginActivity.LOGIN_PREF,"logged-out");
                myEditor.apply();

                Intent intent = new Intent(SettingsActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        llPushNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEditor.putBoolean(LoginActivity.NOTIFICATION_PREF,pushNotification.isChecked());
                myEditor.apply();
            }
        });
        llVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEditor.putBoolean(LoginActivity.VIBRATION_PREF,vibrate.isChecked());
                myEditor.apply();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}