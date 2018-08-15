package com.example.bryan.flashcards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TitleScreen_Activity extends AppCompatActivity {

    Button additionGame, subtractGame, multiplicationGame;
    int difficulty, gameType, timerLength;
    RadioGroup difficultyButtons, timerButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        additionGame = findViewById(R.id.additionGame);
        subtractGame = findViewById(R.id.subtractGame);
        multiplicationGame = findViewById(R.id.multiplyGame);
        difficultyButtons = findViewById(R.id.difficultyButtons);
        timerButtons = findViewById(R.id.timerButtons);

        //Load up the saved file to see which radio button is selected
        SharedPreferences prefs = getSharedPreferences("MATH_FLASHCARDS", MODE_PRIVATE);
        difficulty = prefs.getInt("difficulty", 1); //Defaults to Simple
        timerLength = prefs.getInt("timerLength", 30);


        //Check the intended DIFFICULTY radio button
        switch (difficulty) {
            case 1:
                difficultyButtons.check(R.id.beginnerButton);
                break;
            case 2:
                difficultyButtons.check(R.id.moderateButton);
                break;
            case 3:
                difficultyButtons.check(R.id.brutalButton);
                break;
        }

        //Check the intended TIMER radio button
        switch (timerLength) {
            case 30:
                timerButtons.check(R.id.thirtySecButton);
                break;
            case 45:
                timerButtons.check(R.id.fortyFiveSecButton);
                break;
            case 60:
                timerButtons.check(R.id.sixtySecButton);
                break;
        }

        //ADDITION
        additionGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameType = 1;

                Intent intent = new Intent(getApplicationContext(), Game_Activity.class);
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("gameType", gameType);
                intent.putExtra("timerLength", timerLength);
                startActivity(intent);
                Log.d("Passing Difficulty", String.valueOf(difficulty));
                Log.d("Passing gameType", String.valueOf(gameType));

            }
        });

        //SUBTRACTION
        subtractGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameType = 2;

                Intent intent = new Intent(getApplicationContext(), Game_Activity.class);
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("gameType", gameType);
                intent.putExtra("timerLength", timerLength);
                startActivity(intent);
                Log.d("Passing Difficulty", String.valueOf(difficulty));
                Log.d("Passing gameType", String.valueOf(gameType));

            }
        });

        //MULTIPLICATION
        multiplicationGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameType = 3;

                Intent intent = new Intent(getApplicationContext(), Game_Activity.class);
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("gameType", gameType);
                intent.putExtra("timerLength", timerLength);
                startActivity(intent);
                Log.d("Passing Difficulty", String.valueOf(difficulty));
                Log.d("Passing gameType", String.valueOf(gameType));
            }
        });
    }

    //Whenever one of the difficulty radio buttons is selected
    public void onDifficultyButtonClick(View view) {

        SharedPreferences.Editor editor = getSharedPreferences("MATH_FLASHCARDS", MODE_PRIVATE).edit();

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.beginnerButton:
                if (checked) {
                    difficulty = 1;
                }
                break;
            case R.id.moderateButton:
                if (checked) {
                    difficulty = 2;
                }
                break;
            case R.id.brutalButton:
                if (checked) {
                    difficulty = 3;
                }
                break;
        }
        editor.putInt("difficulty", difficulty);
        editor.apply();
        editor.commit();
    }

    public void onTimerButtonClick(View view) {

        SharedPreferences.Editor editor = getSharedPreferences("MATH_FLASHCARDS", MODE_PRIVATE).edit();

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.thirtySecButton:
                if (checked) {
                    timerLength = 30;
                }
                break;
            case R.id.fortyFiveSecButton:
                if (checked) {
                    timerLength = 45;
                }
                break;
            case R.id.sixtySecButton:
                if (checked) {
                    timerLength = 60;
                }
                break;
        }
        editor.putInt("timerLength", timerLength);
        editor.apply();
        editor.commit();
    }

    public void onBackPressed() {
        finish();   //Close the app, do not go back to previous screen

    }
}
