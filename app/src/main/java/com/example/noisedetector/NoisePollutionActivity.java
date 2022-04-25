package com.example.noisedetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.noisedetector.BroadcastReciever.Restarter;
import com.example.noisedetector.Services.NotificationService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

public class NoisePollutionActivity extends AppCompatActivity {

    public static final String TAG = "NoiseActivity";
    TextView tvSound;
    TextView tvIntensity;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise);

        tvSound = findViewById(R.id.tv_sound);
        tvIntensity = findViewById(R.id.tv_intensity);

        ref = FirebaseDatabase.getInstance("https://noisedetector-8d941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Data");

//        Intent i = new Intent(NoisePollutionActivity.this, NotificationService.class);
//        if (!isMyServiceRunning(NotificationService.class)) {
//            startService(i);
//        }

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setElevation(0);
            actionBar.setTitle("Noise Pollution Detector");
        }

//        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
//
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//                        // Log and toast
//
//                        Log.d(TAG, "The result: "+ token);
//
//                    }
//                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            HashMap<String,Object> hashMap = new HashMap<>();
                            for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                                String key = snapshot1.getKey();
                                String val = snapshot1.getValue().toString();

                                if(key.equals("sound")) {
                                    tvSound.setText(val);
                                } else {
                                    tvIntensity.setText(val);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG,error.toString());
                    }
                });
            }
        }, 100);
    }

//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                Log.i ("Service status", "Running");
//                return true;
//            }
//        }
//        Log.i ("Service status", "Not running");
//        return false;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.overflow_settings){
            Intent i = new Intent(NoisePollutionActivity.this,SettingsActivity.class);
            startActivity(i);
        }else if(id == R.id.overflow_about_us) {
            Intent i = new Intent(NoisePollutionActivity.this, AboutUsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("restartservice");
//        broadcastIntent.setClass(this, Restarter.class);
//        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }
}