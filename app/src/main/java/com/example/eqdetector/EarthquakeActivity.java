package com.example.eqdetector;

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

import com.example.eqdetector.BroadcastReciever.Restarter;
import com.example.eqdetector.Services.NotificationService;
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

public class EarthquakeActivity extends AppCompatActivity {

    public static final String TAG = "EarthquakeActivity";
    TextView tvGVal;
    TextView tvIntensity;
    DatabaseReference mref;
    final ArrayList<Float> listData = new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        tvGVal = findViewById(R.id.tv_g_val);
        tvIntensity = findViewById(R.id.tv_intensity);

        mref = FirebaseDatabase.getInstance().getReference().child("g_val_a");

        Intent i = new Intent(EarthquakeActivity.this, NotificationService.class);
        if (!isMyServiceRunning(NotificationService.class)) {
            startService(i);
        }

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle("Earthquake Detector");



        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast

                        Log.d(TAG, "The result: "+ token);

                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            HashMap<String,Object> hashMap = new HashMap<>();
                            for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                                String val = snapshot1.getValue().toString();
                                Float valFloat = Float.parseFloat(val);
                                tvGVal.setText(val);
                                tvIntensity.setText(valToIntensity(valFloat));
                                if(valFloat > 0.092){
                                    listData.add(valFloat);
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
        }, 2000);
    }

    private String valToIntensity(Float value){
        if(value <= 0.014){
            return "Weak";
        }else if(value > 0.014 && value <= 0.039){
            return "Light";
        }else if(value > 0.039 && value <= 0.092){
            return "Moderate";
        }else if(value > 0.092 && value <= 0.18){
            return "Strong";
        }else if(value > 0.18 && value <= 0.34){
            return "Very Strong";
        }else if(value > 0.34 && value <= 0.65){
            return "Severe";
        }else if(value > 0.65 && value <= 1.24){
            return "Violent";
        }else if ( value > 1.24){
            return "Extreme";
        }
        return "Weak";
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.overflow_settings){
            Intent i = new Intent(EarthquakeActivity.this,SettingsActivity.class);
            startActivity(i);
        }else if(id == R.id.overflow_about_us){
            Intent i = new Intent(EarthquakeActivity.this,AboutUsActivity.class);
            startActivity(i);
        }else if(id == R.id.overflow_history){
            Intent i = new Intent(EarthquakeActivity.this,SismosActivity.class);
            startActivity(i);
        }else if(id == R.id.overflow_safety_instruction){
            Intent i = new Intent(EarthquakeActivity.this,SafetyInstructionsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }
}