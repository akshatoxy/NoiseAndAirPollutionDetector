package com.example.noisedetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity{

    private ImageView mIcon;

    private Animation zoomIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        zoomIn = AnimationUtils.loadAnimation(this,R.anim.zoom_in);

        mIcon = findViewById(R.id.icon);

        mIcon.setAnimation(zoomIn);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i;
                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCE,MODE_PRIVATE);

                Log.d("main-activity",sharedPreferences.getString(LoginActivity.LOGIN_PREF,"logged-out"));

                if(sharedPreferences.getString(LoginActivity.LOGIN_PREF,"logged-out").equals("logged-out")) {
                    i = new Intent(MainActivity.this, LoginActivity.class);
                    nextActivity(i);
                }else if(sharedPreferences.getString(LoginActivity.LOGIN_PREF,"logged-out").equals("logged-in")){
                    i = new Intent(MainActivity.this, OptionsActivity.class);
                    nextActivity(i);
                }
            }
        }, 2000);
    }

    private void nextActivity(Intent i){
        startActivity(i);
        finish();
    }
}
