package com.example.noisedetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noisedetector.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Void> {

    private FirebaseAuth mAuth;
    private TextView login ;
    private Button registerUser;
    private EditText name, email, password;
    private AutoCompleteTextView location;
    private ProgressBar progressBar;
    private ArrayList<String> cities, states;

    public static final int USER_REGISTER_LOADER = 33;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        login =(TextView)findViewById(R.id.login);
        login.setOnClickListener(this);

        registerUser =(Button)findViewById(R.id.registerbtn);
        registerUser.setOnClickListener(this);

        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        location = findViewById(R.id.location);

        cities = new ArrayList<>();
        states = new ArrayList<>();

        progressBar=(ProgressBar)findViewById(R.id.pb_loading_indicator_register);
        getSupportLoaderManager().initLoader(USER_REGISTER_LOADER,null,this);

        try {
            JSONArray arr = new JSONArray(loadJSONFromAsset());

            for(int i=0; i<arr.length(); i++) {
                JSONObject elem = (JSONObject) arr.get(i);
                cities.add(elem.getString("name"));
                states.add(elem.getString("state"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        location.setThreshold(2);
        location.setAdapter(new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, cities));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            case R.id.registerbtn:
                getUserDetails();
                break;
        }

    }


    private void getUserDetails(){
        final String email1 = email.getText().toString().trim();
        String password1= password.getText().toString().trim();
        final String name1 =name.getText().toString().trim();
        final String location1= location.getText().toString().trim();

        if(name1.isEmpty()){
            name.setError("Name is required");
            name.requestFocus();
            return;
        }
        if(email1.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            email.setError("Please enter valid email");
            email.requestFocus();
            return;
        }
        if(location1.isEmpty()){
            location.setError("Location is required");
            location.requestFocus();
            return;
        }

        if(password1.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if(password1.length() < 6){
            password.setError("Min password length should be 6 character");
            password.requestFocus();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("NAME",name1);
        bundle.putString("EMAIL",email1);
        bundle.putString("PASSWORD",password1);
        bundle.putString("LOCATION",location1);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Void> loader = loaderManager.getLoader(USER_REGISTER_LOADER);

        if (loader == null) {
            loaderManager.initLoader(USER_REGISTER_LOADER, bundle, this);
        } else {
            loaderManager.restartLoader(USER_REGISTER_LOADER, bundle, this);
        }

    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(this) {

            @Override
            protected void onStartLoading() {
                Log.d("Register", "Inside on start loading");
                super.onStartLoading();
                if(args == null){
                    return;
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public Void loadInBackground() {
                String email1 = args.getString("EMAIL");
                String password1 = args.getString("PASSWORD");
                String name1 = args.getString("NAME");
                String location1 = args.getString("LOCATION");
                mAuth.createUserWithEmailAndPassword(email1, password1)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    User user= new User(name1,email1,location1);
                                    Log.d("Register", FirebaseAuth.getInstance().getCurrentUser().getUid() + " " + FirebaseDatabase.getInstance().getReference("users") + " " + user);
                                    FirebaseDatabase.getInstance("https://noisedetector-8d941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterUser.this,"User has registered successfully",Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Intent i = new Intent(RegisterUser.this,LoginActivity.class);
                                                startActivity(i);
                                                finish();
                                            }else {
                                                Log.d("Register", task.getException().toString());
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(RegisterUser.this,"Failed to register ! Try again",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else {
                                    Log.d("Register", task.getException().toString());
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(RegisterUser.this,"Failed to register ! Try again",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished( Loader<Void> loader, Void data) {

    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}