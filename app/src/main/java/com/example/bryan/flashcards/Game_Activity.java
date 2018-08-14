package com.example.bryan.flashcards;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Game_Activity extends AppCompatActivity {

    CountDownTimer countDownTimer;

    boolean timerActive;

    final int gameTimer = 10000;        //Game timer

    GameData gameData = new GameData(); //Class for the game

    TextView result;                    //Right or wrong
    TextView playerScore, timer;        //For debugging purposes, will remove
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Log.d("Difficulty Pass", "Failed");
            } else {
                gameData.setDifficulty(extras.getInt("difficulty"));       //Passing the difficulty level from the title screen
                gameData.setGameType(extras.getInt("gameType"));
            }
        }
        else {
            //Crashes when switching to landscape
//            gameData.setDifficulty((Integer) savedInstanceState.getSerializable("difficulty"));
//            gameData.setGameType((Integer) savedInstanceState.getSerializable("gameType"));
        }

        if (gameData.gameType == 1) {
            gameData.additionDifficulty(gameData.difficulty);
        }
        else if (gameData.gameType == 2) {
            gameData.subtractionDifficulty(gameData.difficulty);
        }
        else if (gameData.gameType == 3) {
            gameData.multiplicationDifficulty(gameData.difficulty);
        }

        timerActive = false;

        ResetGameBoard();

    }

    public void Gameplay() {

        equation.setClickable(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (timerActive == false) {
            countDownTimer = new CountDownTimer(gameTimer, 100) {
                public void onTick(long millisUntilFinished) {

                    String timeLeft = String.format(Locale.getDefault(), "%02d", gameData.timerValue);
                    timer.setText(timeLeft);
                    gameData.timerValue++;
                }

                public void onFinish() {
                    timerActive = false;
                    AlertDialog.Builder adb = new AlertDialog.Builder(Game_Activity.this);
                    adb.setTitle("TIME'S UP");
                    adb.setMessage("Your score: " + String.valueOf(gameData.getScore()) +
                            "\nIncorrect: " + String.valueOf(gameData.getWrong()));
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
                            Intent intent = new Intent(getApplicationContext(), TitleScreen_Activity.class);
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

        if (gameData.gameType == 3 && gameData.difficulty == 2) {
            gameData.randomNumber1 = EquationRandomizer(gameData.number1Min, gameData.number1Max);           //1st number for the equation
            gameData.randomNumber2 = EquationRandomizer(gameData.number1Min, gameData.number1Max) + 10;           //2nd number for the equation
        }
        else {
            gameData.randomNumber1 = EquationRandomizer(gameData.number1Min, gameData.number1Max);           //1st number for the equation
            gameData.randomNumber2 = EquationRandomizer(gameData.number1Min, gameData.number1Max);           //2nd number for the equation
        }

        switch (gameData.gameType) {
            case 1:
                gameData.correctAnswer = gameData.additionGame(gameData.randomNumber1, gameData.randomNumber2);  //Determining the correct answer
                equation.setText(gameData.randomNumber1 + " + " + gameData.randomNumber2);            //Showing text of the equation
                break;
            case 2:
                //Make sure the first number is larger than the second number
                if (gameData.randomNumber2 > gameData.randomNumber1) {
                    if (gameData.difficulty == 1) {
                        gameData.randomNumber1 = gameData.randomNumber2 + EquationRandomizer(1, 4);
                    }
                    else if (gameData.difficulty == 2) {
                        gameData.randomNumber1 = gameData.randomNumber2 + EquationRandomizer(40,80);
                    }
                    else if (gameData.difficulty == 3) {
                        gameData.randomNumber1 = gameData.randomNumber2 + EquationRandomizer(400, 800);
                    }
                    else {
                        Log.d("Missing difficulty", "something broke");
                    }
                }
                gameData.correctAnswer = gameData.subtractionGame(gameData.randomNumber1, gameData.randomNumber2);

                //In case the answer is less than 4, add four to the total
                if (gameData.correctAnswer < 4) {
                    gameData.randomNumber1 += 4;
                    gameData.correctAnswer = gameData.randomNumber1 - gameData.randomNumber2;
                }

                equation.setText(gameData.randomNumber1 + " - " + gameData.randomNumber2);            //Showing text of the equation
                break;
            case 3:
                gameData.correctAnswer = gameData.multiplicationGame(gameData.randomNumber1, gameData.randomNumber2);
                equation.setText(gameData.randomNumber1 + " * " + gameData.randomNumber2);            //Showing text of the equation
                break;
        }

        gameData.answerSelectionBatch = EquationRandomizer(1, 4);

        //Randomize the incorrect answers in relation to the correct value
        switch (gameData.difficulty) {

            //Easy difficulty
            case 1:
                switch (gameData.answerSelectionBatch) {
                    case 1:
                        myArray.add(gameData.correctAnswer);
                        myArray.add(gameData.correctAnswer + 1);
                        myArray.add(gameData.correctAnswer + 2);
                        myArray.add(gameData.correctAnswer + 3);
                        break;
                    case 2:
                        myArray.add(gameData.correctAnswer);
                        myArray.add(gameData.correctAnswer + 1);
                        myArray.add(gameData.correctAnswer - 1);
                        myArray.add(gameData.correctAnswer + 2);
                        break;
                    case 3:
                        myArray.add(gameData.correctAnswer);
                        myArray.add(gameData.correctAnswer + 1);
                        myArray.add(gameData.correctAnswer - 1);
                        myArray.add(gameData.correctAnswer - 2);
                        break;
                    case 4:
                        myArray.add(gameData.correctAnswer);
                        myArray.add(gameData.correctAnswer - 1);
                        myArray.add(gameData.correctAnswer - 2);
                        myArray.add(gameData.correctAnswer - 3);
                        break;
                }
                break;

            //Moderate difficulty
            case 2:
                switch (gameData.answerSelectionBatch) {
                    case 1:
                        myArray.add(gameData.correctAnswer);
                        myArray.add(gameData.correctAnswer + 10);
                        myArray.add(gameData.correctAnswer + 20);
                        myArray.add(gameData.correctAnswer + 30);
                        break;
                    case 2:
                        myArray.add(gameData.correctAnswer);
                        myArray.add(gameData.correctAnswer + 10);
                        myArray.add(gameData.correctAnswer - 10);
                        myArray.add(gameData.correctAnswer + 20);
                        break;
                    case 3:
                        myArray.add(gameData.correctAnswer);
                        myArray.add(gameData.correctAnswer + 10);
                        myArray.add(gameData.correctAnswer - 10);
                        myArray.add(gameData.correctAnswer - 20);
                        break;
                    case 4:
                        myArray.add(gameData.correctAnswer);
                        myArray.add(gameData.correctAnswer - 10);
                        myArray.add(gameData.correctAnswer - 20);
                        myArray.add(gameData.correctAnswer - 30);
                        break;
                }
                break;

            //Hardest difficulty
            case 3:
                switch (gameData.answerSelectionBatch) {
                    case 1:
                        myArray.add(gameData.correctAnswer);
                        myArray.add(gameData.correctAnswer + 100);
                        myArray.add(gameData.correctAnswer + 200);
                        myArray.add(gameData.correctAnswer + 300);
                        break;
                    case 2:
                        myArray.add(gameData.correctAnswer);
                        myArray.add(gameData.correctAnswer + 100);
                        myArray.add(gameData.correctAnswer - 100);
                        myArray.add(gameData.correctAnswer + 200);
                        break;
                    case 3:
                        myArray.add(gameData.correctAnswer);
                        myArray.add(gameData.correctAnswer + 100);
                        myArray.add(gameData.correctAnswer - 100);
                        myArray.add(gameData.correctAnswer - 200);
                        break;
                    case 4:
                        myArray.add(gameData.correctAnswer);
                        myArray.add(gameData.correctAnswer - 100);
                        myArray.add(gameData.correctAnswer - 200);
                        myArray.add(gameData.correctAnswer - 300);
                        break;
                }
                break;
        }


        Collections.shuffle(myArray);               //Shuffle the array so the answer will appear in a random location

        //Logs
        Log.d("Equation:", String.valueOf(gameData.randomNumber1) + " " + String.valueOf(gameData.randomNumber2));
        Log.d("Shuffled", myArray.get(0) + " " + myArray.get(1) + " " + myArray.get(2) + " " + myArray.get(3));
        Log.d("AnswerShuffleValue", String.valueOf(gameData.answerSelectionBatch));


        answerChoice1.setText(myArray.get(0).toString());
        answerChoice2.setText(myArray.get(1).toString());
        answerChoice3.setText(myArray.get(2).toString());
        answerChoice4.setText(myArray.get(3).toString());

        answerChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myArray.get(0) == gameData.correctAnswer) {
                    result.setText("CORRECT");
                    gameData.score += 1;
                    playerScore.setText(String.valueOf(gameData.score));
                    Gameplay();
                } else {
                    result.setText("WRONG");
                    gameData.wrong += 1;
                }
            }
        });

        answerChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myArray.get(1) == gameData.correctAnswer) {
                    result.setText("CORRECT");
                    gameData.score += 1;
                    playerScore.setText(String.valueOf(gameData.score));
                    Gameplay();
                } else {
                    result.setText("WRONG");
                    gameData.wrong += 1;
                }
            }
        });

        answerChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myArray.get(2) == gameData.correctAnswer) {
                    result.setText("CORRECT");
                    gameData.score += 1;
                    playerScore.setText(String.valueOf(gameData.score));
                    Gameplay();
                } else {
                    result.setText("WRONG");
                    gameData.wrong += 1;
                }
            }
        });

        answerChoice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myArray.get(3) == gameData.correctAnswer) {
                    result.setText("CORRECT");
                    gameData.score += 1;
                    playerScore.setText(String.valueOf(gameData.score));
                    Gameplay();
                } else {
                    result.setText("WRONG");
                    gameData.wrong += 1;
                }
            }
        });

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gameplay();
            }
        });

        //onBackPressed();
    }

    //Randomizing the number to use in the equation
    public int EquationRandomizer ( int min, int max){
        return new Random().nextInt((max - min) + 1) + min;
    }

    //Setting everything on the game board back to 0, or empty, or blank
    public void ResetGameBoard() {
        gameData.setTimerValue(0);
        gameData.setScore(0);
        gameData.setWrong(0);
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
                nextQuestion.setText("SKIP");
                Gameplay();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){

        finish();
        return true;
    }

    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
        finish();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//    }
}

