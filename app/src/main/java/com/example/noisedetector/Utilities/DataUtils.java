package com.example.noisedetector.Utilities;

import android.content.Context;
import android.util.Log;

import com.example.noisedetector.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DataUtils {

    private static String[] data;

    public static String[] getSimpleEarthquakeData(Context context){
        BufferedInputStream bufferedInputStream = new BufferedInputStream(context.getResources().openRawResource(R.raw.dummy_data));
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(bufferedInputStream));

        ArrayList<String> arrayList = new ArrayList<>();

        try {
            String line = bufferedReader.readLine();

            for(int i = 0;i<15;i++){
                line = bufferedReader.readLine();
                String[] dataAtI = line.split(",");
                String date = dataAtI[0].substring(0,10);
                String magnitude = dataAtI[4];
                String state = dataAtI[13].split(" ")[3];
                String country = dataAtI[14].trim();
                String countryClean = country.substring(0,country.length()-1);
                String location = state + ", " + countryClean;

                String lineData = date + "!" + magnitude + "!" + location;

                arrayList.add(i,lineData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        data = arrayList.toArray(new String[arrayList.size()]);

        for(String s:data) {
            Log.d("Data-Utils", s);
        }
        Log.d("Data-Utils","end");
        return data;
    }

}
