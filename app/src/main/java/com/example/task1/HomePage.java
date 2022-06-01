package com.example.task1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {


    boolean isDarkMode;
    Button buttonDarkMode;
    SharedPreferences sharedPreferences;
    int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = this.getSharedPreferences("com.example.task1", MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        TextView textHighScore = findViewById(R.id.textViewHighScore);
        buttonDarkMode = findViewById(R.id.buttonDarkMode);
        Intent intent = getIntent();
        highScore = intent.getIntExtra("highscore", 0);
        textHighScore.setText(String.valueOf(highScore));

        Log.i("highscore", String.valueOf(highScore));

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
    }

    public void clickDarkMode(View view) {
        if (isDarkMode) {
            isDarkMode = false;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            buttonDarkMode.setText("Dark mode");
        }
        else{
            isDarkMode = true;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            buttonDarkMode.setText("Light mode");
        }
    }

    public void toGame (View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("isDarkMode", isDarkMode);
        startActivity(intent);
    }
}