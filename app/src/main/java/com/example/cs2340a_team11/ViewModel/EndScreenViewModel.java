package com.example.cs2340a_team11.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.cs2340a_team11.Model.Leaderboard;
import com.example.cs2340a_team11.Model.Player;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class EndScreenViewModel extends ViewModel {
    private Player player = Player.getPlayer();
    private final int numScores = 5;
    private Leaderboard leaderboard = Leaderboard.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<String> scoresLiveData = new MutableLiveData<>();
    public EndScreenViewModel() {
        /*
        leaderboard.addScore(player.getName(), calcTotalScore());
        ArrayList topScores = leaderboard.getTopScores(5);
         */
        loadScores(numScores);
    }

    public void addScoreToDatabase() {
        Map<String, Object> user = new HashMap<>();
        user.put("player", player.getName());
        user.put("score", player.getScore());

        db.collection("scores")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("EndScreenViewModel", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("EndScreenViewModel", "Error adding document", e);
                    }
                });
    }
    public void loadScores(int numDisplay) {
        db.collection("scores").orderBy("score", Query.Direction.DESCENDING)
                .limit(numDisplay).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder scores = new StringBuilder();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String playerName = document.getString("player");
                            Long score = document.getLong("score");
                            scores.append(playerName).append(": ").append(score).append("\n");
                        }
                        scoresLiveData.setValue(scores.toString());
                    } else {
                        Log.w("EndScreenViewModel", "Error getting documents.", task.getException());
                    }
                });
    }

    public LiveData<String> getScoresLiveData() {
        return scoresLiveData;
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
