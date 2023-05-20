package com.example.mygame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {
    TextView Game_points;
    TextView tvHighest;
    SharedPreferences sharedPreferences;
    /*SharedPreferences wins = getSharedPreferences("Wins", MODE_PRIVATE);
     = wins.getInt("NumWins", 0);*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        Game_points = findViewById(R.id.tvPoints);
        tvHighest = findViewById(R.id.tvHighest);
        int points = getIntent().getExtras().getInt("points");
        Game_points.setText("" + points);

        sharedPreferences = getSharedPreferences("my_pref", 0);
        int high = sharedPreferences.getInt("high", 0);
        if (points>high){
            high = points;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("high", high);
            editor.commit();
        }
        tvHighest.setText(""+high);

    }

    public void exit(View view) {
        finish();
    }

    public void reset(View view) {
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
