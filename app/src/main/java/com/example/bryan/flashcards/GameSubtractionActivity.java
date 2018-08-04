package com.example.bryan.flashcards;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GameSubtractionActivity extends AppCompatActivity {

    int answerSelectionBatch;          //Used to figure out which batch of random answers to give
    int randomNumber1, randomNumber2, correctAnswer;
    int difficulty, score;
    int number1Min, number1Max;
    int timerValue;
    boolean timerActive;

    final int gameTimer = 60000;        //Game timer

    TextView result, playerScore, timer;
    Button equation, answerChoice1, answerChoice2, answerChoice3, answerChoice4, nextQuestion;

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

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Log.d("Difficulty Pass", "Failed");
            } else {
                difficulty = extras.getInt("difficulty");       //Passing the difficulty level from the title screen
            }
        }
        else {
            //Crashes when switching to landscape
            difficulty = (Integer) savedInstanceState.getSerializable("difficulty");
        }

        switch (difficulty) {
            case 1:
                number1Min = 1;
                number1Max = 10;
                break;
            case 2:
                number1Min = 10;
                number1Max = 100;
                break;
            case 3:
                number1Min = 1000;
                number1Max = 10000;
                break;
        }

        timerActive = false;

        ResetGameBoard();

    }

    public void Gameplay() {

        equation.setClickable(false);

        if (timerActive == false) {
            new CountDownTimer(gameTimer, 100) {
                public void onTick(long millisUntilFinished) {

                    String timeLeft = String.format(Locale.getDefault(), "%02d", timerValue);
                    timer.setText(timeLeft);
                    timerValue++;
                }

                public void onFinish() {
                    timerActive = false;
                    AlertDialog.Builder adb = new AlertDialog.Builder(GameSubtractionActivity.this);
                    adb.setTitle("TIME'S UP");
                    adb.setMessage("Your score: " + String.valueOf(score));
                    adb.setCancelable(false);                                                           //Prevents window from closing when clicking outside
                    adb.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ResetGameBoard();
                        }
                    });
                    adb.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getApplicationContext(), TitleScreenActivity.class);
                            startActivity(intent);
                        }
                    });
                    //Show and create dialogue window
                    adb.create();
                    adb.show();
                }
            }.start();
            timerActive = true;
        }

        //final int correctAnswer;
        final List<Integer> myArray = new ArrayList<>(4);      //Creating an array to put the 4 possible answers in

        //this will ensure that the value is at least 4
        while (correctAnswer < 4) {
            randomNumber1 = EquationRandomizer(number1Min, number1Max);           //1st number for the equation
            randomNumber2 = EquationRandomizer(number1Min, number1Max);           //2nd number for the equation
            correctAnswer = randomNumber1 - randomNumber2;                      //Determining the correct answer
        }
        answerSelectionBatch = EquationRandomizer(1, 4);

        equation.setText(randomNumber1 + " - " + randomNumber2);            //Showing text of the equation

        //Randomize the incorrect answers in relation to the correct value
        switch (difficulty) {

            //Easy difficulty
            case 1:
                switch (answerSelectionBatch) {
                    case 1:
                        myArray.add(correctAnswer);
                        myArray.add(correctAnswer + 1);
                        myArray.add(correctAnswer + 2);
                        myArray.add(correctAnswer + 3);
                        break;
                    case 2:
                        myArray.add(correctAnswer);
                        myArray.add(correctAnswer + 1);
                        myArray.add(correctAnswer - 1);
                        myArray.add(correctAnswer + 2);
                        break;
                    case 3:
                        myArray.add(correctAnswer);
                        myArray.add(correctAnswer + 1);
                        myArray.add(correctAnswer - 1);
                        myArray.add(correctAnswer - 2);
                        break;
                    case 4:
                        myArray.add(correctAnswer);
                        myArray.add(correctAnswer - 1);
                        myArray.add(correctAnswer - 2);
                        myArray.add(correctAnswer - 3);
                        break;
                }
                break;

            //Moderate difficulty
            case 2:
                switch (answerSelectionBatch) {
                    case 1:
                        myArray.add(correctAnswer);
                        myArray.add(correctAnswer + 10);
                        myArray.add(correctAnswer + 20);
                        myArray.add(correctAnswer + 30);
                        break;
                    case 2:
                        myArray.add(correctAnswer);
                        myArray.add(correctAnswer + 10);
                        myArray.add(correctAnswer - 10);
                        myArray.add(correctAnswer + 20);
                        break;
                    case 3:
                        myArray.add(correctAnswer);
                        myArray.add(correctAnswer + 10);
                        myArray.add(correctAnswer - 10);
                        myArray.add(correctAnswer - 20);
                        break;
                    case 4:
                        myArray.add(correctAnswer);
                        myArray.add(correctAnswer - 10);
                        myArray.add(correctAnswer - 20);
                        myArray.add(correctAnswer - 30);
                        break;
                }
                break;

            //Hardest difficulty
            case 3:
                switch (answerSelectionBatch) {
                    case 1:
                        myArray.add(correctAnswer);
                        myArray.add(correctAnswer + 100);
                        myArray.add(correctAnswer + 200);
                        myArray.add(correctAnswer + 300);
                        break;
                    case 2:
                        myArray.add(correctAnswer);
                        myArray.add(correctAnswer + 100);
                        myArray.add(correctAnswer - 100);
                        myArray.add(correctAnswer + 200);
                        break;
                    case 3:
                        myArray.add(correctAnswer);
                        myArray.add(correctAnswer + 100);
                        myArray.add(correctAnswer - 100);
                        myArray.add(correctAnswer - 200);
                        break;
                    case 4:
                        myArray.add(correctAnswer);
                        myArray.add(correctAnswer - 100);
                        myArray.add(correctAnswer - 200);
                        myArray.add(correctAnswer - 300);
                        break;
                }
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
                } else {
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
                } else {
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
                } else {
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
                } else {
                    result.setText("WRONG");
                }
            }
        });
    }

    //Randomizing the number to use in the equation
    public int EquationRandomizer ( int min, int max){
        return new Random().nextInt((max - min) + 1) + min;
    }

    //Setting everything on the game board back to 0, or empty, or blank
    public void ResetGameBoard() {
        timerValue = 0;
        score = 0;
        equation.setText("Press here to begin");
        timer.setText(String.valueOf(0));           //for debugging
        playerScore.setText(String.valueOf(0));     //for debugging
        answerChoice1.setText("");
        answerChoice2.setText("");
        answerChoice3.setText("");
        answerChoice4.setText("");
        result.setText("");
        nextQuestion.setText("");

        equation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion.setText("NEXT");
                Gameplay();
            }
        });
    }

}

