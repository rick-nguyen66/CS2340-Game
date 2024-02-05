package com.example.cs2340a_team11.View.Activities;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cs2340a_team11.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartScreen extends Activity {
    private FirebaseAuth mAuth;
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

        mAuth = FirebaseAuth.getInstance();

        createAcc.setOnClickListener(view -> {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // if signin successful, update UI with user info
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                // updateUI(user);
                                startGame();
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(StartScreen.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }
                        }
                    });
        });

        signInButton.setOnClickListener(view -> {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                // updateUI(user);

                                startGame();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(StartScreen.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }
                        }
                    });
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
