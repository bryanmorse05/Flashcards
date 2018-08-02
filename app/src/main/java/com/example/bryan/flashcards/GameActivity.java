package com.example.bryan.flashcards;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    int answerSelectionBatch;          //Used to figure out which batch of random answers to give
    int randomNumber1, randomNumber2;
    int level, score;
    int level1Min, level1Max;
    int timerValue;
    boolean timerActive;

    TextView equation, result, playerScore, timer;
    Button answerChoice1, answerChoice2, answerChoice3, answerChoice4, nextQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        equation = findViewById(R.id.equation);
        result = findViewById(R.id.result);
        playerScore = findViewById(R.id.playerScore);
        timer = findViewById(R.id.timer);
        answerChoice1 = findViewById(R.id.answerChoice1);
        answerChoice2 = findViewById(R.id.answerChoice2);
        answerChoice3 = findViewById(R.id.answerChoice3);
        answerChoice4 = findViewById(R.id.answerChoice4);
        nextQuestion = findViewById(R.id.nextQuestion);

        score = 0;
        timerValue = 0;
        nextQuestion.setText("BEGIN");





        level1Min = 1;
        level1Max = 10;
        timerActive = false;

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion.setText("NEXT");
                Gameplay();
            }
        });
    }

    public void Gameplay() {

        if (timerActive == false) {
            new CountDownTimer(60000, 100) {
                public void onTick(long millisUntilFinished) {

                    String timeLeft = String.format(Locale.getDefault(), "%02d", timerValue);
                    timer.setText(timeLeft);
                    timerValue++;
                }

                public void onFinish() {
                    timerActive = false;
                    AlertDialog.Builder adb = new AlertDialog.Builder(GameActivity.this);
                    adb.setTitle("TIME'S UP");
                    adb.setMessage("Your score: " + String.valueOf(score));
                    adb.setCancelable(false);                                                           //Prevents window from closing when clicking outside
                    adb.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ResetGameBoard();
                            GameActivity.this.recreate();
                        }
                    });
                    adb.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GameActivity.this.finish();
                        }
                    });
                    //Show and create dialogue window
                    adb.create();
                    adb.show();
                }
            }.start();
            timerActive = true;
        }

        final int correctAnswer;
        final List<Integer> myArray = new ArrayList<Integer>(4);      //Creating an array to put the 4 possible answers in

        randomNumber1 = EquationRandomizer(level1Min, level1Max);           //1st number for the equation
        randomNumber2 = EquationRandomizer(level1Min, level1Max);           //2nd number for the equation
        correctAnswer = randomNumber1 + randomNumber2;                      //Determining the correct answer
        answerSelectionBatch = EquationRandomizer(1, 4);

        equation.setText(randomNumber1 + " + " + randomNumber2);            //Showing text of the equation

        //Randomize the incorrect answers in relation to the correct value
        switch (answerSelectionBatch) {
            case 1 :
                myArray.add(correctAnswer);
                myArray.add(correctAnswer + 1);
                myArray.add(correctAnswer + 2);
                myArray.add(correctAnswer + 3);
                break;
            case 2 :
                myArray.add(correctAnswer);
                myArray.add(correctAnswer + 1);
                myArray.add(correctAnswer - 1);
                myArray.add(correctAnswer + 2);
                break;
            case 3 :
                myArray.add(correctAnswer);
                myArray.add(correctAnswer + 1);
                myArray.add(correctAnswer - 1);
                myArray.add(correctAnswer - 2);
                break;
            case 4 :
                myArray.add(correctAnswer);
                myArray.add(correctAnswer - 1);
                myArray.add(correctAnswer - 2);
                myArray.add(correctAnswer - 3);
                break;
        }

        Collections.shuffle(myArray);               //Shuffle the array so the answer will appear in a random location

        //Logs
        Log.d("Equation:", String.valueOf(randomNumber1) + " " + String.valueOf(randomNumber2));
        Log.d("Shuffled", myArray.get(0) + " " + myArray.get(1) + " " + myArray.get(2) + " " + myArray.get(3));
        Log.d("AnswerShuffleValue", String.valueOf(answerSelectionBatch));

        
        answerChoice1.setText(myArray.get(0).toString());
        answerChoice2.setText(myArray.get(1).toString());
        answerChoice3.setText(myArray.get(2).toString());
        answerChoice4.setText(myArray.get(3).toString());

        answerChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myArray.get(0) == correctAnswer) {
                    result.setText("CORRECT");
                    score += 1;
                    playerScore.setText(String.valueOf(score));
                    Gameplay();
                }
                else {
                    result.setText("WRONG");
                }
            }
        });

        answerChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myArray.get(1) == correctAnswer) {
                    result.setText("CORRECT");
                    score += 1;
                    playerScore.setText(String.valueOf(score));
                    Gameplay();
                }
                else {
                    result.setText("WRONG");
                }
            }
        });

        answerChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myArray.get(2) == correctAnswer) {
                    result.setText("CORRECT");
                    score += 1;
                    playerScore.setText(String.valueOf(score));
                    Gameplay();
                }
                else {
                    result.setText("WRONG");
                }
            }
        });

        answerChoice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myArray.get(3) == correctAnswer) {
                    result.setText("CORRECT");
                    score += 1;
                    playerScore.setText(String.valueOf(score));
                    Gameplay();
                }
                else {
                    result.setText("WRONG");
                }
            }
        });


    }

    //Randomizing the number to use in the equation
    public int EquationRandomizer(int min, int max) {
        return new Random().nextInt((max-min) + 1) + min;
    }

    public void AnswerRandomizer(int answer) {

    }

    public void ResetGameBoard() {
        timerValue = 0;
        score = 0;
        timer.setText(String.valueOf(0));
        playerScore.setText(String.valueOf(0));
    }
}

