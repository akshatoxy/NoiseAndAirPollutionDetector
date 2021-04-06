package com.example.eqdetector;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void> {

    private Button loginbtn;
    private TextView register;
    private TextInputEditText editTextEmail,editTextPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    public static String MY_PREFERENCE = "my-pref";
    public static String LOGIN_PREF = "login-status-key";
    public static String NOTIFICATION_PREF = "notification-key";
    public static String VIBRATION_PREF = "vibration-key";

    private SharedPreferences sharedPreferences;

    public static final int USER_LOGIN_LOADER = 22;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        loginbtn =(Button) findViewById(R.id.loginbtn);
        register =(TextView) findViewById(R.id.register);
        editTextEmail=findViewById(R.id.etEmail);
        editTextPassword=findViewById((R.id.etPassword)) ;
        progressBar=(ProgressBar)findViewById(R.id.pb_loading_indicator);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(MY_PREFERENCE,Context.MODE_PRIVATE);

        getSupportLoaderManager().initLoader(USER_LOGIN_LOADER, null, this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Login-Activity","Login Button Clicked");
                if(toast != null){
                    toast.cancel();
                }
                getEmailAndPass();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterUser.class);
                startActivity(i);
            }
        });



        register.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (actionId == KeyEvent.KEYCODE_D)) {
                    Log.d("LoginActivity","done clicked");
                    loginbtn.performClick();
                    return true;
                }
                return false;
            }
        });

    }


    private void getEmailAndPass(){
        String email1=editTextEmail.getText().toString().trim();
        String password1=editTextPassword.getText().toString().trim();

        if(email1.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            editTextEmail.setError("Please enter valid email");
            editTextEmail.requestFocus();
            return;
        }
        if(password1.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if(password1.length() < 6){
            editTextPassword.setError("Min password length should be 6 character");
            editTextPassword.requestFocus();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("EMAIL",email1);
        bundle.putString("PASSWORD",password1);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Void> loginUserLoader = loaderManager.getLoader(USER_LOGIN_LOADER);

        if (loginUserLoader == null) {
            loaderManager.initLoader(USER_LOGIN_LOADER, bundle, this);
        } else {
            loaderManager.restartLoader(USER_LOGIN_LOADER, bundle, this);
        }

    }


    @Override
    public Loader<Void> onCreateLoader(int id,Bundle args) {
        return new AsyncTaskLoader<Void>(this) {

            @Override
            protected void onStartLoading() {
                if(args == null){
                    Log.d("login-activity","inside onstart Loading but args is null");
                    return;
                }else {
                    Log.d("login-activity","inside onstart Loading");
                    loginbtn.setClickable(false);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public Void loadInBackground() {
                Log.d("login-activity","inside Load in back");
                String email1=args.getString("EMAIL");
                String password1= args.getString("PASSWORD");
                mAuth.signInWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            SharedPreferences.Editor myEditor = sharedPreferences.edit();
                            myEditor.putString(LOGIN_PREF,"logged-in");
                            myEditor.apply();

                            Log.d("login-activity",sharedPreferences.getString(LOGIN_PREF,"logged-out"));

                            if(sharedPreferences.getString(LOGIN_PREF,"logged-out").equals("logged-in")) {
                                Intent i = new Intent(LoginActivity.this, EarthquakeActivity.class);
                                startActivity(i);
                                finish();
                            }
                            Log.d("login-activity","task success");
                        }else {
                            toast = Toast.makeText(LoginActivity.this,"Failed to Login ! Try again",Toast.LENGTH_LONG);
                            toast.show();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        loginbtn.setClickable(true);
                    }
                });
                return null;
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        //We dont need this!
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {
        //We dont need this
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
