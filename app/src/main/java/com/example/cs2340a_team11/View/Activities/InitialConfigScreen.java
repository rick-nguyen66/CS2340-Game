package com.example.cs2340a_team11.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cs2340a_team11.Model.Player;
import com.example.cs2340a_team11.R;
import com.example.cs2340a_team11.View.Maps.MapOneActivity;
import com.example.cs2340a_team11.ViewModel.InitialConfigViewModel;

public class InitialConfigScreen extends AppCompatActivity implements View.OnClickListener {
    private Player player = Player.getPlayer();
    private String playerName;
    private InitialConfigViewModel initialConfigViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_config_screen);
        // reference the view-model
        initialConfigViewModel = new ViewModelProvider(this).get(InitialConfigViewModel.class);

        Button submitButton = (Button) findViewById(R.id.Submit);

        Button easy = (Button) findViewById(R.id.Easy);
        easy.setOnClickListener(this);
        Button medium = (Button) findViewById(R.id.Medium);
        medium.setOnClickListener(this);
        Button hard = (Button) findViewById(R.id.Hard);
        hard.setOnClickListener(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView nameBox = (TextView) findViewById(R.id.nameBox);
                playerName = nameBox.getText().toString();

                if (initialConfigViewModel.checkName(playerName)) {
                    startGame();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();
        initialConfigViewModel.gameSetDifficulty(v.getId());

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        int id = view.getId();

        if (checked) {
            initialConfigViewModel.chooseChar(id);
        }
    }

    public void startGame() {
        Intent gameScreenIntent = new Intent(this, MapOneActivity.class);
        System.out.println(playerName);
        startActivity(gameScreenIntent);
    }

}