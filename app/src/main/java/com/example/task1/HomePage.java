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
    Button buttonDarkMode;
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

        TextView textHighScore = findViewById(R.id.textViewHighScore);
        buttonDarkMode = findViewById(R.id.buttonDarkMode);
        setTimer = findViewById(R.id.buttonSetTimer);
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