package com.harrisonseitz.myapplication;

import java.util.ArrayList;

/**
 * Created by harrisonseitz on 5/31/17.
 */

public class ReportCard {
    // Declare class variables
    private String mStudentId;
    private ArrayList<Integer> mGrades;
    private float mGradeAverage;

    // Constructor for a new ReportCard
    public ReportCard(String studentId) {
        mStudentId = studentId;
        mGrades = new ArrayList<Integer>();
        mGradeAverage = 0.0f;
    }

    public void addGrade(int grade) {
        /* Originally the below read `mGrades.add(Integer.valueOf(grade));`, but Android Studio told
        me that was 'unnecessary boxing', so hopefully this works?. */
        mGrades.add(grade);
        mGradeAverage = getGradePercentage();
    }

    public String getStudentId() {
        return mStudentId;
    }

    public ArrayList<Integer> getRawGrades() {
        return mGrades;
    }

    public float getGradeAverage() {
        return mGradeAverage;
    }

    public void setStudentId(String studentId) {
        mStudentId = studentId;
    }

    public void setGrades(ArrayList<Integer> grades) {
        mGrades = grades;
    }

    public void setGradeAverage(float gradeAverage) {
        mGradeAverage = gradeAverage;
    }

    public float getGradePercentage() {
        int pointsEarned = 0;
        /* Going to assume all assignments were out of 100 points. Because this is my class and I
        make the rules :P */
        int pointsTotal = 100 * mGrades.size();
        for (int i = 0; i < mGrades.size(); i++) {
            pointsEarned += mGrades.get(i);
        }
        return (float) pointsEarned / pointsTotal;
    }

    @Override
    public String toString() {
        return "ReportCard{" +
                "mStudentId='" + mStudentId + '\'' +
                ", mGrades=" + mGrades +
                ", mGradeAverage=" + mGradeAverage +
                '}';
    }
}
