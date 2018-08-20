package com.example.bryan.flashcards;

import android.content.DialogInterface;
import android.graphics.Color;
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

    CountDownTimer countDownTimer;      //Countown timer for gameplay

    boolean timerActive;                //Is the timer going?
    int timePassed;                     //How much time has passed before being paused?

    GameData gameData = new GameData(); //Class for the game

    TextView result;                    //Right or wrong
    TextView playerScore, timer;        //For debugging purposes, will remove
    Button equationButton;                    //The equation in the center, is button because it's how the game starts
    Button answerChoice1;               //Top left selection
    Button answerChoice2;               //Tpp right selection
    Button answerChoice3;               //Bottom left selection
    Button answerChoice4;               //Bottom right selection
    Button nextQuestion;                //Skip to the next question button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Widgets
        equationButton = findViewById(R.id.equationButton);
        result = findViewById(R.id.result);
        playerScore = findViewById(R.id.playerScore);
        timer = findViewById(R.id.timer);
        answerChoice1 = findViewById(R.id.answerChoice1);
        answerChoice2 = findViewById(R.id.answerChoice2);
        answerChoice3 = findViewById(R.id.answerChoice3);
        answerChoice4 = findViewById(R.id.answerChoice4);
        nextQuestion = findViewById(R.id.nextQuestion);

        //Setting up and enabling the back button in the support bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");

        //Loading game data from the title screen.  Difficulty, game type, and timer length
        //If any of these are null, then it's not going to work
        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Log.d("Game_Activity", "Intent data failed");
            } else {
                gameData.setDifficulty(extras.getInt("difficulty"));       //Passing the difficulty level from the title screen
                gameData.setGameType(extras.getInt("gameType"));
                gameData.setTimerLength(extras.getInt("timerLength"));
            }
        }

        //After determining the game type, then the difficulty level is set up as well
        //The difficulty method in the Gamedata class takes care of the number ranges
        if (gameData.gameType == 1) {
            gameData.additionDifficulty(gameData.difficulty);
        }
        else if (gameData.gameType == 2) {
            gameData.subtractionDifficulty(gameData.difficulty);
        }
        else if (gameData.gameType == 3) {
            gameData.multiplicationDifficulty(gameData.difficulty);
        }

        timerActive = false;    //Timer is not currently active

        //ResetGameBaord is called first because it clears the board
        //The game is started from this method.
        ResetGameBoard();

    }

    public void Gameplay() {

        equationButton.setClickable(false);   //Disable the button so the user can't press it

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //Back button in support bar enabled

        //Setting up the CountDownTimer.  Begin only if TimerActive is false
        if (timerActive == false) {

            //CountDownTimer is in milliseconds.  Calulation is timer length that the user set * 1000 so it's
            //changed into seconds.  Second parameter is the interval it counts per tick
            countDownTimer = new CountDownTimer((gameData.getTimerLength() * 1000) - (gameData.getTimeElapsed() * 1000), 100) {

                //Ticks until length is reached
                public void onTick(long millisUntilFinished) {

                    //This is what is displayed.  Currently for debugging info, but may want to keep
                    //Format %02d is to display two digits.  However, mine is 3, don't know why
                    //TimerValue starts at 0, then adds the length of the timer
                    String timeLeft = String.format(Locale.getDefault(), "%02d", gameData.timerValue + gameData.getTimerLength() * 10);

                    timer.setText(timeLeft);    //Display the amount of time left
                    gameData.timerValue--;      //Subtract one of the timer every 100 ticks (this is NOT what's displayed)
                    timePassed++;
                    gameData.setTimeElapsed(timePassed);

                }

                //Once the timer is completed
                public void onFinish() {
                    timerActive = false;
                    countDownTimer.cancel();
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
//                            Intent intent = new Intent(getApplicationContext(), TitleScreen_Activity.class);
//                            startActivity(intent);
                            finish();
                        }
                    });
                    //Show and create dialogue window
                    adb.create();
                    adb.show();
                }
            }.start();

            result.setBackgroundColor(Color.argb(200, 250, 181, 133));
            result.setTextColor(Color.BLACK);
            result.setText("GO!");
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
                equationButton.setText(gameData.randomNumber1 + "\n + " + gameData.randomNumber2);            //Showing text of the equation
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

                equationButton.setText(gameData.randomNumber1 + "\n - " + gameData.randomNumber2);            //Showing text of the equation
                break;
            case 3:
                gameData.correctAnswer = gameData.multiplicationGame(gameData.randomNumber1, gameData.randomNumber2);
                equationButton.setText(gameData.randomNumber2 + "\n * " + gameData.randomNumber1);            //Showing text of the equation
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
        Log.d("Shuffled:", myArray.get(0) + " " + myArray.get(1) + " " + myArray.get(2) + " " + myArray.get(3));
        Log.d("AnswerShuffleValue:", String.valueOf(gameData.answerSelectionBatch));


        answerChoice1.setText(myArray.get(0).toString());
        answerChoice2.setText(myArray.get(1).toString());
        answerChoice3.setText(myArray.get(2).toString());
        answerChoice4.setText(myArray.get(3).toString());

        answerChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myArray.get(0) == gameData.correctAnswer) {
                    result.setTextColor(Color.rgb(5, 122, 48));
                    result.setText("CORRECT");
                    gameData.score += 1;
//                    playerScore.setText(String.valueOf(gameData.score));
                    Gameplay();
                } else {
                    result.setTextColor(Color.RED);
                    result.setText("WRONG");
                    gameData.wrong += 1;
                    Gameplay();
                }
            }
        });

        answerChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myArray.get(1) == gameData.correctAnswer) {
                    result.setTextColor(Color.rgb(5, 122, 48));
                    result.setText("CORRECT");
                    gameData.score += 1;
//                    playerScore.setText(String.valueOf(gameData.score));
                    Gameplay();
                } else {
                    result.setTextColor(Color.RED);
                    result.setText("WRONG");
                    gameData.wrong += 1;
                    Gameplay();
                }
            }
        });

        answerChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myArray.get(2) == gameData.correctAnswer) {
                    result.setTextColor(Color.rgb(5, 122, 48));
                    result.setText("CORRECT");
                    gameData.score += 1;
//                    playerScore.setText(String.valueOf(gameData.score));
                    Gameplay();
                } else {
                    result.setTextColor(Color.RED);
                    result.setText("WRONG");
                    gameData.wrong += 1;
                    Gameplay();
                }
            }
        });

        answerChoice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myArray.get(3) == gameData.correctAnswer) {
                    result.setTextColor(Color.rgb(5, 122, 48));
                    result.setText("CORRECT");
                    gameData.score += 1;
//                    playerScore.setText(String.valueOf(gameData.score));
                    Gameplay();
                } else {
                    result.setTextColor(Color.RED);
                    result.setText("WRONG");
                    gameData.wrong += 1;
                    Gameplay();
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
        gameData.setTimeElapsed(0);
        timePassed = 0;
        timerActive = false;
        equationButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        equationButton.setText("Press to begin");
//        timer.setText(String.valueOf(0));           //for debugging
//        playerScore.setText(String.valueOf(0));     //for debugging
        answerChoice1.setText("");
        answerChoice1.setVisibility(View.INVISIBLE);
        answerChoice1.setClickable(false);
        answerChoice2.setText("");
        answerChoice2.setVisibility(View.INVISIBLE);
        answerChoice2.setClickable(false);
        answerChoice3.setText("");
        answerChoice3.setVisibility(View.INVISIBLE);
        answerChoice3.setClickable(false);
        answerChoice4.setText("");
        answerChoice4.setVisibility(View.INVISIBLE);
        answerChoice4.setClickable(false);
        result.setText("");
        result.setBackgroundColor(Color.TRANSPARENT);
        nextQuestion.setText("");
        timer.setText("");
        nextQuestion.setVisibility(View.INVISIBLE);
        nextQuestion.setClickable(false);

        equationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion.setText("SKIP");
                answerChoice1.setVisibility(View.VISIBLE);
                answerChoice1.setClickable(true);
                answerChoice2.setVisibility(View.VISIBLE);
                answerChoice2.setClickable(true);
                answerChoice3.setVisibility(View.VISIBLE);
                answerChoice3.setClickable(true);
                answerChoice4.setVisibility(View.VISIBLE);
                answerChoice4.setClickable(true);
                nextQuestion.setVisibility(View.VISIBLE);
                nextQuestion.setClickable(true);

                equationButton.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                //Setting the padding based on the difficulty
                switch (gameData.difficulty) {
                    case 1:
                        equationButton.setPadding(0,0,160,0);
                        break;
                    case 2:
                        equationButton.setPadding(0,0,145,0);
                        break;
                    case 3:
                        equationButton.setPadding(0,0,100,0);
                        if (gameData.gameType == 3) {
                            equationButton.setPadding(0,0,145,0);   //Since multiplication only used two-digit numbers, they can be closer
                        }
                        break;
                }

                Gameplay();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (timerActive == true) {
            countDownTimer.cancel();
        }
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        if (timerActive == true) {
            countDownTimer.cancel();
        }
        super.onBackPressed();
        finish();
    }

    public void onPause() {
        if (timerActive == true) {
            countDownTimer.cancel();
        }
        super.onPause();
    }

    public void onResume() {

        if (timerActive == true) {
//            countDownTimer.onFinish();

            AlertDialog.Builder adb = new AlertDialog.Builder(Game_Activity.this);
            adb.setTitle("GAME WAS INTERRUPTED\n(also no pausing to cheat)");
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
//                            Intent intent = new Intent(getApplicationContext(), TitleScreen_Activity.class);
//                            startActivity(intent);
                    finish();
                }
            });
            //Show and create dialogue window
            adb.create();
            adb.show();

        }
        super.onResume();
    }
}

