package com.example.task1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Map<Integer, Boolean> isClicked;
    List<Integer> values;
    List<String> usedOperations;
    List<Double> answers;
    GridLayout gridLayout2;
    GridLayout gridLayout, gridLayout5, gridLayout3, gridLayout4, gridLayout1;
    String[] operations = {"+", "-", "x", "/"};
    String selectedAnswer;
    DecimalFormat decimalFormat;
    int livesCount = 3, score = 0;

    public void createPuzzle() {

        isClicked = new HashMap<Integer, Boolean>();
        values = new ArrayList<Integer>();
        usedOperations = new ArrayList<String>();
        answers = new ArrayList<Double>();

        for (int i = 10; i <= 19; i++) {
            isClicked.put(i, false);
            int rand = new Random().nextInt(100) + 1;
            values.add(rand);
        }
        Log.i("values", String.valueOf(values));
        for(int i = 0; i < gridLayout2.getChildCount(); i++) {
            TextView child = (TextView) gridLayout2.getChildAt(i);
            int randIndex = new Random().nextInt(4);
            child.setText(operations[randIndex]);
            usedOperations.add(operations[randIndex]);
        }
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
                answers.add(quotient);
            }
        }
        Collections.shuffle(values);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button child = (Button) gridLayout.getChildAt(i);
            child.setText(String.valueOf(values.get(i)));
            child.setTag(String.valueOf(values.get(i)));
        }
        Log.i("values", String.valueOf(values));
        for (int i = 0; i < gridLayout5.getChildCount(); i++) {
            TextView child = (TextView) gridLayout5.getChildAt(i);
            double getAnswer = Double.parseDouble(String.valueOf(answers.get(i)));
            child.setText(String.valueOf(decimalFormat.format(getAnswer)));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decimalFormat = new DecimalFormat("#.00");

        gridLayout2 = (GridLayout) findViewById(R.id.gridLayout2);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout5 = (GridLayout) findViewById(R.id.gridLayout5);
        gridLayout3 = (GridLayout) findViewById(R.id.gridLayout3);
        gridLayout4 = (GridLayout) findViewById(R.id.gridLayout4);
        gridLayout1 = (GridLayout) findViewById(R.id.gridLayout1);

        createPuzzle();
    }

    public void clickInput(View view) {
        int activeButtonsCount = 0;
        int index = Integer.parseInt((String) view.getTag());
        for (Map.Entry<Integer, Boolean> entry : isClicked.entrySet()){
            if (entry.getValue()) {
                activeButtonsCount = activeButtonsCount+1;
            }
        }
        if (activeButtonsCount != 0) {
            view.setBackgroundColor(Color.WHITE);
            isClicked.put(index, false);
            selectedAnswer = "";
            for (int i = 0; i < gridLayout1.getChildCount(); i++) {
                Button child = (Button) gridLayout1.getChildAt(i);
                child.setClickable(false);
                child.setBackgroundColor(Color.GRAY);
            }
            for (int i = 0; i < gridLayout3.getChildCount(); i++) {
                Button child = (Button) gridLayout3.getChildAt(i);
                child.setClickable(false);
                child.setBackgroundColor(Color.GRAY);
            }
        }
        else {
            view.setBackgroundColor(Color.GRAY);
            isClicked.put(index, true);
            Button button = (Button) view;
            selectedAnswer = (String) button.getText();
            for (int i = 0; i < gridLayout1.getChildCount(); i++) {
                Button child = (Button) gridLayout1.getChildAt(i);
                child.setClickable(true);
                child.setBackgroundColor(Color.WHITE);
            }
            for (int i = 0; i < gridLayout3.getChildCount(); i++) {
                Button child = (Button) gridLayout3.getChildAt(i);
                child.setClickable(true);
                child.setBackgroundColor(Color.WHITE);
            }
        }
        Log.i("answer", String.valueOf(selectedAnswer));
    }
    public void onAnswerClick(View view) {
        Button button = (Button) view;
        if (String.valueOf(button.getText()) == "") {
            button.setText(selectedAnswer);
            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                Button child = (Button) gridLayout.getChildAt(i);
                if (String.valueOf((child.getText())) == selectedAnswer) {
                    child.setText("");
                }
            }
        }
        else {
            selectedAnswer = String.valueOf(button.getText());
            button.setText("");
            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                Button child = (Button) gridLayout.getChildAt(i);
                if (String.valueOf((child.getTag())) == selectedAnswer) {
                    child.setText(selectedAnswer);
                }
            }
            selectedAnswer = "";
        }
    }
    public void submitAnswer(View view) {
        int wrongAnswerCount = 0;

        for (int i = 0; i < 5; i++) {
            Button child1 = (Button) gridLayout1.getChildAt(i);
            TextView child2 = (TextView) gridLayout2.getChildAt(i);
            Button child3 = (Button) gridLayout3.getChildAt(i);
            TextView child5 = (TextView) gridLayout5.getChildAt(i);

            double a = Double.parseDouble(String.valueOf(child1.getText()));
            Log.i("a", String.valueOf(a));
            double b = Double.parseDouble(String.valueOf(child3.getText()));
            Log.i("a", String.valueOf(b));
            String operation = String.valueOf(child2.getText());
            double ans = Double.parseDouble(String.valueOf(child5.getText()));

            if (operation == "+") {
                if (a+b != ans) {
                    wrongAnswerCount += 1;
                    Log.i("ans", String.valueOf(a+b));
                    Log.i("ans", String.valueOf(ans));
                }
            }
            if (operation == "-") {
                if (a-b != ans) {
                    wrongAnswerCount += 1;
                    Log.i("ans", String.valueOf(a-b));
                    Log.i("ans", String.valueOf(ans));
                }
            }
            if (operation == "x") {
                if (a*b != ans) {
                    wrongAnswerCount += 1;
                    Log.i("ans", String.valueOf(a*b));
                    Log.i("ans", String.valueOf(ans));
                }
            }
            if (operation == "/") {
                if (String.valueOf(decimalFormat.format(a/b)) != String.valueOf(decimalFormat.format(ans))) {
                    wrongAnswerCount += 1;
                    Log.i("ans", String.valueOf(decimalFormat.format(a/b)));
                    Log.i("ans", String.valueOf(decimalFormat.format(ans)));
                }

            }
        }
        if (wrongAnswerCount == 0) {
            Log.i("Right", "correct");
            score += 1;
            Toast.makeText(getApplicationContext(), "Your score is " + String.valueOf(score), Toast.LENGTH_SHORT).show();
        }
        else {
            Log.i("Right", "wrong");
            livesCount -= 1;
            Toast.makeText(getApplicationContext(), "You have lost a life " + String.valueOf(livesCount) + " lives remaining", Toast.LENGTH_SHORT).show();
        }
        if (livesCount == 0){
            TextView textView = findViewById(R.id.textView7);
            textView.setVisibility(View.VISIBLE);
        }
        else {
            createPuzzle();
        }
    }
}