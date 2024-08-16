package com.example.cs2340a_team11.View.Activities;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs2340a_team11.R;
import com.example.cs2340a_team11.ViewModel.StartScreenViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartScreen extends AppCompatActivity {
    private StartScreenViewModel startScreenViewModel;
    private static final String TAG = "StartScreen";
    /*
        For testing:
        user: re@gmail.com
        pass: abc123
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        //create activity_start_screen XML file

        TextView welcomeText = findViewById(R.id.welcomeText);
        // Button startButton = findViewById(R.id.signInButton);
        Button quitButton = findViewById(R.id.quitButton);

        TextView email = findViewById(R.id.emailField);
        TextView password = findViewById(R.id.passwordField);
        Button signInButton = findViewById(R.id.signInButton);
        Button createAcc = findViewById(R.id.createAccount);

        // Set text to something better if wanted
        welcomeText.setText("Welcome!");

        // mAuth = FirebaseAuth.getInstance();
        startScreenViewModel = new ViewModelProvider(this).get(StartScreenViewModel.class);

        startScreenViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                Log.d(TAG, "getUser onChanged called");
                if (user != null) {
                    startGame(); // Proceed to the next activity
                } else {
                    Log.d(TAG, "LOL");
                }
            }
        });

        startScreenViewModel.getAuthErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Log.d(TAG, "getAuth onChanged called");
                if (message != null) {
                    Log.d(TAG, "getAuth onChanged message not null: " + message);
                    Toast.makeText(StartScreen.this, message, Toast.LENGTH_LONG).show();
                } else {
                    Log.d(TAG, "LOL");
                }
            }
        });

        createAcc.setOnClickListener(view -> {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            startScreenViewModel.createAccount(emailText, passwordText);
        });

        signInButton.setOnClickListener(view -> {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            startScreenViewModel.signIn(emailText, passwordText);
        });

        /*
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
                //welcomeText.setVisibility(View.GONE);
                //startButton.setVisibility(View.GONE);
                //endButton.setVisibility(View.VISIBLE);
            }
        });
         */

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // welcomeText.setVisibility(View.GONE);
                // startButton.setVisibility(View.GONE);
                finish();
            }
        });
    }

    /* If we want the user to auto sign-in, then include this
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload(); // DO: check if user signed-in (not null), update UI accordingly
        }
    }

    private void reload() { }
     */


    private void startGame() {
        Intent initialConfigIntent = new Intent(this, InitialConfigScreen.class);
        startActivity(initialConfigIntent);
    }

}
