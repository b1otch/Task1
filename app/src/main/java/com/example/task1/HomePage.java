package com.example.task1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    boolean isDarkMode;
    SharedPreferences sharedPreferences;
    int highScore;
    boolean isTimerMode;
    int timer;
    Button setTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = this.getSharedPreferences("com.example.task1", MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button darkMode = findViewById(R.id.buttonDarkMode);

        TextView textHighScore = findViewById(R.id.textViewHighScore);
        setTimer = findViewById(R.id.buttonSetTimer);
        Intent intent = getIntent();
        highScore = sharedPreferences.getInt("highscore", 0);
        textHighScore.setText(String.valueOf(highScore));

        Log.i("highscore", String.valueOf(highScore));

        isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            darkMode.setText("LIGHT MODE");
        }
        else {
            darkMode.setText("DARK MODE");
        }
    }

    public void clickDarkMode(View view) {
        Button isDarkModeButton = (Button) view;
        Log.i("isDarkMode", String.valueOf(isDarkMode));

        if (isDarkMode) {
            isDarkMode = false;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            isDarkModeButton.setText("Dark mode");
            recreate();
        }
        else{
            isDarkMode = true;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            isDarkModeButton.setText("Light mode");
            recreate();
        }
        sharedPreferences.edit().putBoolean("isDarkMode", isDarkMode).apply();
    }

    public void toGame (View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("isDarkMode", isDarkMode);
        intent.putExtra("timerLength", timer);
        intent.putExtra("isTimerMode", isTimerMode);
        startActivity(intent);
    }

    public void setTimer (View view) {
        final EditText editText = new EditText(HomePage.this);
        new AlertDialog.Builder(HomePage.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Game Time")
                .setMessage("Enter the time for game")
                .setView(editText)
                .setPositiveButton("SET TIME", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        timer = Integer.parseInt(editText.getText().toString());
                    }
                })
                .setNegativeButton("SET TO DEFAULT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        timer = 300000;
                    }
                })
                .show();
    }

    public void enableTimer(View view) {
        new AlertDialog.Builder(HomePage.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Timer Mode")
                .setMessage("Do you want to enable/disable timer mode")
                .setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isTimerMode = true;
                        setTimer.setClickable(true);
                    }
                })
                .setNegativeButton("DISABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isTimerMode = false;
                        setTimer.setClickable(false);
                    }
                })
                .show();

    }
}