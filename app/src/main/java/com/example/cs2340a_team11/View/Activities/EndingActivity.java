package com.example.cs2340a_team11.View.Activities;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs2340a_team11.Model.Player;
import com.example.cs2340a_team11.R;
import com.example.cs2340a_team11.ViewModel.EndScreenViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EndingActivity extends AppCompatActivity {
    private EndScreenViewModel endScreenViewModel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Player player = Player.getPlayer();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ending_screen);
        endScreenViewModel = new ViewModelProvider(this).get(EndScreenViewModel.class);

        // DO PLEASE: display end results by fetching info from view-model
        TextView currentScore = findViewById(R.id.curr_score);
        currentScore.setText("Last attempted score: "
                + Integer.toString(endScreenViewModel.calcTotalScore()));

        Map<String, Object> user = new HashMap<>();
        user.put("player", player.getName());
        user.put("score", player.getScore());

        // add score to database (generally use a hashmap for multiple data)
        db.collection("scores")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        TextView scoreList = findViewById(R.id.scoreList);
        scoreList.setText(endScreenViewModel.getScores());

        Button restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });
    }

    public void restartGame() {
        endScreenViewModel.resetScore();
        Intent restart = new Intent(this, InitialConfigScreen.class);
        startActivity(restart);
    }
}