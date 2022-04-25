package com.example.noisedetector;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Button noiseActivityButton = findViewById(R.id.noise_button);
        Button airActivityButton = findViewById(R.id.air_button);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setElevation(0);
            actionBar.setTitle("Pollution Detector");
        }

        noiseActivityButton.setOnClickListener(view -> {
            Intent i = new Intent(OptionsActivity.this, NoisePollutionActivity.class);
            startActivity(i);
        });

        airActivityButton.setOnClickListener(view -> {
            Intent i = new Intent(OptionsActivity.this, AirPollutionActivity.class);
            startActivity(i);
        });
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
            Intent i = new Intent(OptionsActivity.this,SettingsActivity.class);
            startActivity(i);
        }else if(id == R.id.overflow_about_us) {
            Intent i = new Intent(OptionsActivity.this, AboutUsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
