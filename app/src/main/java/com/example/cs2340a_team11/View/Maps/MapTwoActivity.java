package com.example.cs2340a_team11.View.Maps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cs2340a_team11.Environment.BitmapInterface;
import com.example.cs2340a_team11.Model.Player;
import com.example.cs2340a_team11.Model.Wall;
import com.example.cs2340a_team11.R;
import com.example.cs2340a_team11.View.GameOverActivity;
import com.example.cs2340a_team11.View.PlayerView;
import com.example.cs2340a_team11.View.BanditView;
import com.example.cs2340a_team11.View.EvilWizardView;
import com.example.cs2340a_team11.ViewModel.GameScreenViewModel;

public class MapTwoActivity extends AppCompatActivity {
    private static Context gameContext;
    private Player player = Player.getPlayer();
    private PlayerView playerView;
    private BanditView banView;

    private EvilWizardView evView;
    private GameScreenViewModel gameScreenViewModel;
    private Wall walls = Wall.getWall();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_two);
        gameContext = this;
        gameScreenViewModel = new ViewModelProvider(this).get(GameScreenViewModel.class);

        ImageView characterView = (ImageView) findViewById(R.id.character_photo);
        TextView nameView = (TextView) findViewById(R.id.name);
        ProgressBar healthBar = (ProgressBar) findViewById(R.id.healthBar);
        ConstraintLayout layout = findViewById(R.id.backgroundLayout);

        healthBar.setProgress(player.getHP());
        nameView.setText(player.getName());

        characterView.setImageResource(gameScreenViewModel.getImg());


        MapView mapView = new MapView(this, 1);
        MapView mapViewItem = new MapView(this, 13);
        layout.addView(mapView);
        layout.addView(mapViewItem);

        // offset the position of map to show in background AND below the info bar
        int offsetY = BitmapInterface.TILE_SIZE * 2;
        mapView.setZ(-1);
        mapView.setY(offsetY);
        mapViewItem.setY(offsetY);

        TextView timeView = findViewById(R.id.scoreUpdate);
        gameScreenViewModel.runTimer(timeView);

        // render playerView
        gameScreenViewModel.setPlayerStarting(2);
        playerView = new PlayerView(this, player.getX(), player.getY(), player.getCharId());
        layout.addView(playerView);
        System.out.println("Player view added");
        playerView.bringToFront();

        banView = new BanditView(this, player.getX(), player.getY() - 4 * BitmapInterface.TILE_SIZE);
        layout.addView(banView);
        System.out.println("Enemy view added");
        banView.bringToFront();
        gameScreenViewModel.runMovement(banView);

        evView = new EvilWizardView(this, player.getX() + 2 * BitmapInterface.TILE_SIZE, player.getY() - 3 * BitmapInterface.TILE_SIZE);
        layout.addView(evView);
        System.out.println("Enemy view added");
        evView.bringToFront();
        gameScreenViewModel.runMovement(evView);
        gameScreenViewModel.updatePlayerHealth(healthBar);
        gameScreenViewModel.getIsGameOver().observe(this, isGameOver -> {
            if (isGameOver) {
                gameScreenViewModel.stopTimer();
                endGame();
            }
        });
        gameScreenViewModel.checkGameOver();
    }


    public static Context getGameContext() {
        return gameContext;
    }

    public void progressToNextMap() {
        Intent progressToMapFinalIntent = new Intent(this, MapFinalActivity.class);
        walls.resetWalls();
        walls.setIsDrawn(false);
        startActivity(progressToMapFinalIntent);
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        gameScreenViewModel.onKeyDown(keycode, event, playerView, walls.getWalls());
        if (gameScreenViewModel.checkDoor()) {
            gameScreenViewModel.stopTimer();
            progressToNextMap();
        }
        return true;
    }
    public void endGame() {
        Intent progressToGameOverScreen = new Intent(this, GameOverActivity.class);
        startActivity(progressToGameOverScreen);
        finish();
    }
}

