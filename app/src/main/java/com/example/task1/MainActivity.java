package com.example.task1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.gridlayout.widget.GridLayout;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MainActivity extends AppCompatActivity {

    ConcurrentMap<Integer, Boolean> isClicked;
    ArrayList<Integer> values;
    ArrayList<String> usedOperations;
    ArrayList<Double> answers;
    GridLayout gridLayout2;
    GridLayout gridLayout, gridLayout5, gridLayout3, gridLayout4, gridLayout1;
    String[] operations = {"+", "-", "x", "/"};
    String selectedAnswer;
    SharedPreferences sharedPreferences;
    DecimalFormat decimalFormat;
    boolean isTimerMode, isDarkMode, changeOrientation;
    CountDownTimer countDownTimer;
    TextView textViewTimer;
    int livesCount = 3, score = 0, highscore;
    int orientation, randIndex;

    public void createPuzzle() {
        for(int i = 0; i < gridLayout2.getChildCount(); i++) {
            TextView child = (TextView) gridLayout2.getChildAt(i);
            child.setText(usedOperations.get(i));
        }
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button child = (Button) gridLayout.getChildAt(i);
            child.setText(String.valueOf(values.get(i)));
            child.setTag(String.valueOf(values.get(i)));
            if (isDarkMode){
                child.setBackgroundColor(Color.BLACK);
            }
            else {
                child.setBackgroundColor(Color.WHITE);
            }
        }
        for (int i = 0; i < gridLayout5.getChildCount(); i++) {
            TextView child = (TextView) gridLayout5.getChildAt(i);
            double getAnswer = Double.parseDouble(String.valueOf(answers.get(i)));
            child.setText(decimalFormat.format(getAnswer));
        }
        for (int i = 0; i < gridLayout1.getChildCount(); i++) {
            Button child = (Button) gridLayout1.getChildAt(i);
            child.setText("");
        }
        for (int i = 0; i < gridLayout3.getChildCount(); i++) {
            TextView child = (TextView) gridLayout3.getChildAt(i);
            child.setText("");
        }
    }

    public void createVariables() {
        isClicked = new ConcurrentHashMap<>();
        values = new ArrayList<Integer>();
        usedOperations = new ArrayList<String>();
        answers = new ArrayList<Double>();
        for (int i = 0; i < 5; i++) {
            randIndex = new Random().nextInt(4);
            usedOperations.add(operations[randIndex]);
        }
        for (int i = 10; i <= 19; i++) {
            isClicked.put(i, false);
            int rand = new Random().nextInt(100) + 1;
            while (values.contains(rand)){
                rand = new Random().nextInt(100) + 1;
            }
            values.add(rand);
        }
        Log.i("values", String.valueOf(values));

        for (int i = 0; i < 5; i++){
            double a = values.get(2*i);
            double b = values.get((2*i)+1);
            if (usedOperations.get(i) == "+") {
                double sum = a+b;
                answers.add(sum);
            }
            else if (usedOperations.get(i) == "-") {
                double difference = a-b;
                answers.add(difference);
            }
            else if (usedOperations.get(i) == "x") {
                double product = a*b;
                answers.add(product);
            }
            else if (usedOperations.get(i) == "/") {
                double quotient = a/b;
                answers.add(Double.parseDouble(decimalFormat.format(quotient)));
            }
        }
        Log.i("answers", String.valueOf(answers));
        Collections.shuffle(values);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        sharedPreferences = this.getSharedPreferences("com.example.task1", MODE_PRIVATE);
        int previousOrientation = sharedPreferences.getInt("orientation", Configuration.ORIENTATION_PORTRAIT);
        orientation = getResources().getConfiguration().orientation;
        sharedPreferences.edit().putInt("orientation", orientation).apply();
        isTimerMode = sharedPreferences.getBoolean("isTimerMode", false);
        isDarkMode = sharedPreferences.getBoolean("isDarkMode", true);
        int timer = sharedPreferences.getInt("timer", 300) * 1000;

        Log.i("isDarkMode", String.valueOf(isDarkMode));
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(timer);
        progressBar.setProgress(timer);
        progressBar.setVisibility(View.INVISIBLE);

        decimalFormat = new DecimalFormat("#.00");

        gridLayout2 = (GridLayout) findViewById(R.id.gridLayout2);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout5 = (GridLayout) findViewById(R.id.gridLayout5);
        gridLayout3 = (GridLayout) findViewById(R.id.gridLayout3);
        gridLayout4 = (GridLayout) findViewById(R.id.gridLayout4);
        gridLayout1 = (GridLayout) findViewById(R.id.gridLayout1);
        textViewTimer = findViewById(R.id.textViewTimer);

        if (isTimerMode) {
            progressBar.setVisibility(View.VISIBLE);
            countDownTimer = new CountDownTimer(timer, 1000) {
                @Override
                public void onTick(long l) {
                    textViewTimer.setText(String.valueOf((l / 1000) +1));
                    progressBar.setProgress((int) l);
                }

                @Override
                public void onFinish() {
                    progressBar.setProgress(0);
                    textViewTimer.setText("0");
                    endGame();
                }
            }.start();
        }
        Log.i("changeOrientation", String.valueOf(changeOrientation));

        isClicked = new ConcurrentHashMap<>();
        values = new ArrayList<Integer>();
        usedOperations = new ArrayList<String>();
        answers = new ArrayList<Double>();

        createVariables();
        createPuzzle();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putIntegerArrayList("values", values);
        outState.putStringArrayList("usedOperations", usedOperations);
        Log.i("answers", String.valueOf(answers));
        double answersList[] = new double[5];
        for (int i = 0; i < 5; i++) {
            answersList[i] = answers.get(i);
        }
        Log.i("answersList", Arrays.toString(answersList));
        Log.i("valuesSaved", values.toString());
        changeOrientation = true;
        Log.i("changeOrientation", String.valueOf(changeOrientation));

        outState.putSerializable("answers", answersList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        double answersList[] = (double[]) savedInstanceState.getSerializable("answers");
        Log.i("answerList1", Arrays.toString(answersList));
        values = savedInstanceState.getIntegerArrayList("values");
        usedOperations = savedInstanceState.getStringArrayList("usedOperations");
        answers.clear();
        for (int i = 0; i < 5; i++) {
            answers.add(answersList[i]);
        }
        createPuzzle();
    }

    public void clickInput(View view) {
        int activeButtonsCount = 0;
        int activeIndex = Integer.parseInt((String) view.getTag());
        int presentIndex;
        Log.i("map", String.valueOf(isClicked));
        Iterator<Map.Entry<Integer, Boolean>> iterator = isClicked.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Boolean> c = iterator.next();
            if (c.getValue()) {
                activeButtonsCount = activeButtonsCount+1;
                presentIndex = c.getKey();
                Log.i("this", String.valueOf(presentIndex));
                if (activeIndex == presentIndex) {
                    if (isDarkMode) {
                        view.setBackgroundColor(Color.BLACK);
                    } else {
                        view.setBackgroundColor(Color.WHITE);
                    }
                    isClicked.put(activeIndex, false);
                    selectedAnswer = "";
                }
                else {
                    view.setBackgroundColor(Color.GRAY);
                    for (int i = 0; i < 10; i++) {
                        Button child = (Button) gridLayout.getChildAt(i);
                        if (Integer.parseInt(String.valueOf(child.getTag())) == presentIndex) {
                            if (isDarkMode) {
                                child.setBackgroundColor(Color.BLACK);
                            } else {
                                child.setBackgroundColor(Color.WHITE);
                            }
                        }
                    }
                    isClicked.put(activeIndex, true);
                    isClicked.put(presentIndex, false);
                    selectedAnswer = String.valueOf(view.getTag());
                }
                activeButtonsCount += 1;
            }
        }
        if (activeButtonsCount == 0) {
            view.setBackgroundColor(Color.GRAY);
            isClicked.put(activeIndex, true);
            Button button = (Button) view;
            selectedAnswer = (String) button.getText();
            for (int i = 0; i < gridLayout1.getChildCount(); i++) {
                Button child = (Button) gridLayout1.getChildAt(i);
                child.setClickable(true);
                if (isDarkMode) {
                    child.setBackgroundColor(Color.BLACK);
                } else {
                    child.setBackgroundColor(Color.WHITE);
                }
            }
            for (int i = 0; i < gridLayout3.getChildCount(); i++) {
                Button child = (Button) gridLayout3.getChildAt(i);
                child.setClickable(true);
                if (isDarkMode) {
                    child.setBackgroundColor(Color.BLACK);
                } else {
                    child.setBackgroundColor(Color.WHITE);
                }
            }
        }
    }
    public void onAnswerClick(View view) {
        Button button = (Button) view;
        if (String.valueOf(button.getText()) == "") {
            button.setText(selectedAnswer);
            button.setTag(selectedAnswer);
            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                Button child = (Button) gridLayout.getChildAt(i);
                if (String.valueOf((child.getText())) == selectedAnswer) {
                    child.setText("");
                }
            }
            selectedAnswer = "";
        }
        else {
            selectedAnswer = String.valueOf(button.getText());
            button.setText("");
            button.setTag("");
            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                Button child = (Button) gridLayout.getChildAt(i);
                if (String.valueOf((child.getTag())) == selectedAnswer) {
                    child.setText(selectedAnswer);
                    if (isDarkMode){
                        child.setBackgroundColor(Color.BLACK);
                    }
                    else {
                        child.setBackgroundColor(Color.WHITE);
                    }
                }
            }
        }
        for (int i = 0; i < 10; i++){
            Button child = (Button) gridLayout.getChildAt(i);
            int index = Integer.parseInt(String.valueOf(child.getTag()));
            isClicked.put(index, false);
        }
    }
    public void submitAnswer(View view) {
        int wrongAnswerCount = 0;

        try {
            for (int i = 0; i < 5; i++) {
                Button child1 = (Button) gridLayout1.getChildAt(i);
                TextView child2 = (TextView) gridLayout2.getChildAt(i);
                Button child3 = (Button) gridLayout3.getChildAt(i);
                TextView child5 = (TextView) gridLayout5.getChildAt(i);

                Double a = Double.parseDouble(decimalFormat.format(Double.valueOf((String) child1.getText())));
                Double b = Double.parseDouble(decimalFormat.format(Double.valueOf((String) child3.getText())));
                String operation = String.valueOf(child2.getText());
                Double ans = Double.parseDouble(String.valueOf(child5.getText()));

                if (operation == "+") {
                    if (a+b != ans) {
                        wrongAnswerCount += 1;
                    }
                }
                if (operation == "-") {
                    if (a-b != ans) {
                        wrongAnswerCount += 1;
                    }
                }
                if (operation == "x") {
                    if (a*b != ans) {
                        wrongAnswerCount += 1;
                    }
                }
                if (operation == "/") {
                    Double formattedAns = Double.parseDouble(decimalFormat.format(a/b));

                    if (Double.compare(formattedAns, ans) != 0) {
                        wrongAnswerCount += 1;
                    }
                }
            }
            if (wrongAnswerCount == 0) {
                score += 1;
                Toast.makeText(getApplicationContext(), "Your score is " + String.valueOf(score), Toast.LENGTH_SHORT).show();
            }
            else {
                livesCount -= 1;
                Toast.makeText(getApplicationContext(), "You have lost a life " + String.valueOf(livesCount) + " lives remaining", Toast.LENGTH_SHORT).show();
            }
            if (livesCount == 0){
                countDownTimer.cancel();
                endGame();
            }
            else {
                createVariables();
                createPuzzle();
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please enter all the values", Toast.LENGTH_SHORT).show();
        }
    }
    public void takeToHomepage (View view) {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        intent.putExtra("highscore", highscore);
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }

        startActivity(intent);
    }

    public void endGame(){
        TextView textView = findViewById(R.id.textView7);
        textView.setVisibility(View.VISIBLE);
        highscore = sharedPreferences.getInt("highscore", 0);
        if (highscore < score) {
            sharedPreferences.edit().putInt("highscore", score).apply();
        }
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(intent);
                    }
                }, 500
        );
        countDownTimer.cancel();
    }
}