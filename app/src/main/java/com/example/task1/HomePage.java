package com.example.task1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        highScore = sharedPreferences.getInt("highscore", 0);
        isTimerMode = sharedPreferences.getBoolean("isTimerMode", false);

        textHighScore.setText(String.valueOf(highScore));

        isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            darkMode.setText("LIGHT MODE");
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            darkMode.setText("DARK MODE");
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        startActivity(intent);
    }

    public void setTimer (View view) {
        if (isTimerMode) {
            try {
                setTime();
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Enter a valid value", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Enable Timer Mode", Toast.LENGTH_SHORT).show();
        }
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
                        sharedPreferences.edit().putBoolean("isTimerMode", true).apply();
                        setTime();
                    }
                })
                .setNegativeButton("DISABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isTimerMode = false;
                        setTimer.setClickable(false);
                        sharedPreferences.edit().putBoolean("isTimerMode", false).apply();
                    }
                })
                .show();
    }
    public void setTime() {
        EditText editText = new EditText(HomePage.this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(HomePage.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Game Time")
                .setMessage("Enter the time for game")
                .setView(editText)
                .setPositiveButton("SET TIME", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        timer = Integer.parseInt(editText.getText().toString());
                        sharedPreferences.edit().putInt("timer", timer).apply();
                    }
                })
                .setNegativeButton("SET TO DEFAULT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        timer = 300000;
                        sharedPreferences.edit().putInt("timer", timer).apply();
                    }
                })
                .show();
    }
}