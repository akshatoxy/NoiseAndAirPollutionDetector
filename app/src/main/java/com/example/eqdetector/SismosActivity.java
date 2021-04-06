package com.example.eqdetector;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.example.eqdetector.Utilities.DataUtils;


public class SismosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private EarthquakeAdapter earthquakeAdapter;

    private static final String TAG = "SismosActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sismos);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle("Earthquake Detector");

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        String[] data = DataUtils.getSimpleEarthquakeData(this);
        earthquakeAdapter = new EarthquakeAdapter(this, data);

        recyclerView = (RecyclerView) findViewById(R.id.rv_eq_data);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(earthquakeAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}