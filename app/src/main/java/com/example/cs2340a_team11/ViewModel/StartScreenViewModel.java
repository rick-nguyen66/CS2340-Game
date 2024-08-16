package com.example.cs2340a_team11.ViewModel;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartScreenViewModel extends AndroidViewModel {
    private final FirebaseAuth mAuth;
    private final MutableLiveData<String> authErrorMessage;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private static final String TAG = "StartScreenViewModel";
    public StartScreenViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
        authErrorMessage = new MutableLiveData<>();
        userLiveData = new MutableLiveData<>();
    }
    public LiveData<String> getAuthErrorMessage() {
        Log.d(TAG, "Error getting");
        return authErrorMessage;
    }
    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
    public void createAccount(String email, String password) {
        Log.d(TAG, "createAccount called");
        if (email.isEmpty() || password.isEmpty()) {
            authErrorMessage.setValue("Email and password must not be empty.");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userLiveData.setValue(user);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            authErrorMessage.setValue("Authentication failed: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public void signIn(String email, String password) {
        Log.d(TAG, "signIn called");
        if (email.isEmpty() || password.isEmpty()) {
            authErrorMessage.setValue("Email and password must not be empty.");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        userLiveData.setValue(user);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        authErrorMessage.setValue("Authentication failed: " + task.getException().getMessage());
                    }
                }
            });
    }
}
