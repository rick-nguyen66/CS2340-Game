package com.example.cs2340a_team11.View.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs2340a_team11.R;
import com.example.cs2340a_team11.ViewModel.EndScreenViewModel;


public class EndingActivity extends AppCompatActivity {

    private EndScreenViewModel endScreenViewModel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ending_screen);
        endScreenViewModel = new ViewModelProvider(this).get(EndScreenViewModel.class);

        // DO PLEASE: display end results by fetching info from view-model
        TextView currentScore = findViewById(R.id.curr_score);
        currentScore.setText("Last attempted score: "
                + Integer.toString(endScreenViewModel.calcTotalScore()));

        // add score to database

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
        Intent restart = new Intent(this, StartScreen.class);
        startActivity(restart);
    }
}