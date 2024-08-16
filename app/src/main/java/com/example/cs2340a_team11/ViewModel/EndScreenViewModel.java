package com.example.cs2340a_team11.ViewModel;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

import com.example.cs2340a_team11.Model.Leaderboard;
import com.example.cs2340a_team11.Model.Player;
import com.google.firebase.firestore.FirebaseFirestore;

public class EndScreenViewModel extends ViewModel {
    private Player player = Player.getPlayer();
    private Leaderboard leaderboard = Leaderboard.getInstance();

    public EndScreenViewModel() {
        /*
        leaderboard.addScore(player.getName(), calcTotalScore());
        ArrayList topScores = leaderboard.getTopScores(5);
         */
    }

    // DO PLEASE: fetch results from player and return to view
    public boolean fetchResult() {
        return player.getHP() != 0;
    }

    public int fetchKilled() {
        return 0;
    }

    public int fetchBoost() {
        return 0;
    }

    public int calcTotalScore() {
        return player.getScore();
    }

    public String getScores() {
        leaderboard.addScore(player.getName(), calcTotalScore());
        ArrayList topScores = leaderboard.getTopScores(5);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < topScores.size(); i++) {
            out.append(i + 1).append(". ").append(topScores.get(i)).append("\n\n");
        }
        return out.toString();
    }

    public void resetScore() {
        player.setScore(0);
    }
}
