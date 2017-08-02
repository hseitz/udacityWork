package com.harrisonseitz.sevenstracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int teamAScore = 0;
    int teamBScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void updateScoreA(View button) {
        switch (button.getId()) {
            case (R.id.tryA):
                teamAScore += 5;
                break;
            case (R.id.conversionA):
                teamAScore += 2;
                break;
            case (R.id.dropGoalA):
                teamAScore += 3;
        }
        displayForTeamA(teamAScore);
    }

    public void updateScoreB(View button){
        switch(button.getId()){
            case (R.id.tryB):
                teamBScore += 5;
                break;
            case (R.id.conversionB):
                teamBScore += 2;
                break;
            case (R.id.dropGoalB):
                teamBScore += 3;
        }
        displayForTeamB(teamBScore);
    }

    /**
     * Displays the given score for Team A.
     */
    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }
    /**
     * Displays the given score for Team B.
     */
    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Resets scores for both teams
     */
    public void reset(View button){
        teamAScore = teamBScore = 0;
        displayForTeamA(teamAScore);
        displayForTeamB(teamBScore);
    }
}
