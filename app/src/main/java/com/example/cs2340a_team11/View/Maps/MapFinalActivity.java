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
import androidx.lifecycle.ViewModelProvider;

import com.example.cs2340a_team11.Environment.BitmapInterface;
import com.example.cs2340a_team11.Model.Enemies.EvilWizard;
import com.example.cs2340a_team11.Model.Factories.EvilWizardFactory;
import com.example.cs2340a_team11.Model.Enemies.Nightborneidle;
import com.example.cs2340a_team11.Model.Factories.NightborneidleFactory;
import com.example.cs2340a_team11.Model.Player;
import com.example.cs2340a_team11.Model.Wall;
import com.example.cs2340a_team11.R;
import com.example.cs2340a_team11.View.Activities.EndingActivity;
import com.example.cs2340a_team11.View.Activities.GameOverActivity;
import com.example.cs2340a_team11.View.EntityViews.EvilWizardView;
import com.example.cs2340a_team11.View.EntityViews.NightborneidleView;
import com.example.cs2340a_team11.View.EntityViews.PlayerView;
import com.example.cs2340a_team11.ViewModel.GameScreenViewModel;

public class MapFinalActivity extends AppCompatActivity {
    private static Context gameContext;
    private Player player = Player.getPlayer();
    private NightborneidleFactory nbFactory = new NightborneidleFactory();
    private Nightborneidle nightborne = (Nightborneidle) nbFactory.createEnemy();
    private EvilWizardFactory evilWizardFactory = new EvilWizardFactory();
    private EvilWizard evilWizard = (EvilWizard) evilWizardFactory.createEnemy();
    private PlayerView playerView;
    private GameScreenViewModel gameScreenViewModel;
    private Wall walls = Wall.getWall();
    private final int playerInitialHP = player.getInitialHP();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_final);
        gameContext = this;
        gameScreenViewModel = new ViewModelProvider(this).get(GameScreenViewModel.class);

        ImageView characterView = (ImageView) findViewById(R.id.character_photo);
        TextView nameView = (TextView) findViewById(R.id.name);
        ProgressBar healthBar = (ProgressBar) findViewById(R.id.healthBar);
        ConstraintLayout layout = findViewById(R.id.backgroundLayout);


        nameView.setText(player.getName());
        healthBar.setProgress((int) (100 * ((float) player.getHP() / playerInitialHP)));
        characterView.setImageResource(gameScreenViewModel.getImg());


        MapView mapView = new MapView(this, 3);
        MapView mapViewItem = new MapView(this, 14);
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
        gameScreenViewModel.setPlayerStarting(3);
        playerView = new PlayerView(this, player.getX(), player.getY(), player.getCharId());
        layout.addView(playerView);
        System.out.println("Player view added");
        playerView.bringToFront();

        EvilWizardView evView = new EvilWizardView(this, player.getX(), player.getY() - 2 * BitmapInterface.TILE_SIZE, evilWizard);
        layout.addView(evView);
        System.out.println("Enemy view added");
        evView.bringToFront();
        gameScreenViewModel.runMovement(evView);

        NightborneidleView nbView = new NightborneidleView(this, player.getX() + BitmapInterface.TILE_SIZE, player.getY(), nightborne);
        layout.addView(nbView);
        System.out.println("Enemy view added");
        nbView.bringToFront();
        gameScreenViewModel.runMovement(nbView);
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

    public void progressToEndScreen() {
        Intent progressToEndIntent = new Intent(this, EndingActivity.class);
        walls.resetWalls();
        walls.setIsDrawn(false);
        startActivity(progressToEndIntent);
    }
    public boolean onKeyDown(int keycode, KeyEvent event) {
        gameScreenViewModel.onKeyDown(keycode, event, playerView, walls.getWalls());
        if (gameScreenViewModel.checkDoor()) {
            gameScreenViewModel.stopTimer();
            progressToEndScreen();
        }
        return true;
    }
    public void endGame() {
        Intent progressToGameOverScreen = new Intent(this, GameOverActivity.class);
        walls.resetWalls();
        walls.setIsDrawn(false);
        startActivity(progressToGameOverScreen);
        finish();
    }
}
