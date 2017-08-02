package com.harrisonseitz.sevenstrivia;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get highScore from SharedPrefs
        SharedPreferences sharedPrefs = this.getSharedPreferences("sevensPrefs", Context.MODE_PRIVATE);
        highScore = sharedPrefs.getInt("score", 0);
        TextView highScoreTV = (TextView) findViewById(R.id.highScore);
        String highScoreText = getResources().getString(R.string.defaultHighScore) + " " + highScore;
        highScoreTV.setText(highScoreText);
    }

    public void finishQuiz(View view) {
        // scroll back to the top
        ScrollView mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
        mainScrollView.fullScroll(ScrollView.FOCUS_UP);
        // crunch the numbers
        int currentScore = calculateScore();
        // new high score?
        if (currentScore > highScore) {
            Toast.makeText(getApplicationContext(), "Congratulations! You set a new high score!",
                    Toast.LENGTH_SHORT).show();
            // set new high Score
            SharedPreferences sharedPrefs = this.getSharedPreferences("sevensPrefs", Context.MODE_PRIVATE);
            highScore = currentScore;
            sharedPrefs.edit().putInt("score", highScore).apply();
            TextView highScoreTV = (TextView) findViewById(R.id.highScore);
            String highScoreText = getResources().getString(R.string.defaultHighScore) + " " + highScore;
            highScoreTV.setText(highScoreText);
        }
        // no points :(
        else if (currentScore == 0) {
            Toast.makeText(getApplicationContext(), "Ouch, you did horrible :(\nTry again!",
                    Toast.LENGTH_SHORT).show();
        }
        // not a new high score but not terrible
        else {
            Toast.makeText(getApplicationContext(), "Not bad! You got " + currentScore + " answer(s) right.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private int calculateScore() {
        int currentScore = 0;
        // grab the EditText for q1
        EditText q1Answer = (EditText) findViewById(R.id.q1answer);
        // is Question 1 correct?
        if (q1Answer.getText().toString().equalsIgnoreCase("Portia Woodman")) {
            currentScore += 1;
        }
        // grab the radiobutton for q2's answer
        RadioButton q2Answer = (RadioButton) findViewById(R.id.q2a3);
        // is Question 2 correct?
        if (q2Answer.isChecked()) {
            currentScore += 1;
        }
        // grab the checkboxes for q3's answer
        CheckBox q3AnswerNZ = (CheckBox) findViewById(R.id.nzCheckBox);
        CheckBox q3AnswerAU = (CheckBox) findViewById(R.id.ausCheckBox);
        CheckBox q3AnswerUS = (CheckBox) findViewById(R.id.usCheckBox);
        CheckBox q3AnswerENG = (CheckBox) findViewById(R.id.engCheckBox);
        CheckBox q3AnswerCA = (CheckBox) findViewById(R.id.caCheckBox);
        CheckBox q3AnswerNL = (CheckBox) findViewById(R.id.nlCheckBox);
        // is Question 3 correct?
        if (q3AnswerNZ.isChecked() && q3AnswerAU.isChecked() && !q3AnswerUS.isChecked() &&
                !q3AnswerENG.isChecked() && !q3AnswerCA.isChecked() && !q3AnswerNL.isChecked()) {
            currentScore += 1;
        }
        // grab the EditText for q4
        EditText q4Answer = (EditText) findViewById(R.id.q4Answer);
        // is Question 4 correct?
        if (q4Answer.getText().toString().equalsIgnoreCase("Portia Woodman")) {
            currentScore += 1;
        }
        // grab the radiobutton for q5's answer
        RadioButton q5answer = (RadioButton) findViewById(R.id.q5a2);
        // is Question 5 correct?
        if (q5answer.isChecked()) {
            currentScore += 1;
        }
        return currentScore;
    }
}

