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
    int difficulty, gameType;
    RadioGroup difficultyButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        additionGame = findViewById(R.id.additionGame);
        subtractGame = findViewById(R.id.subtractGame);
        multiplicationGame = findViewById(R.id.multiplyGame);
        difficultyButtons = findViewById(R.id.difficultyButtons);

        difficulty = 1;     //By default

        SharedPreferences prefs = getSharedPreferences("MATH_FLASHCARDS", MODE_PRIVATE);
        difficulty = prefs.getInt("difficulty", 0);


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


        additionGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameType = 1;

                Intent intent = new Intent(getApplicationContext(), Game_Activity.class);
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("gameType", gameType);
                startActivity(intent);
                Log.d("Passing Difficulty", String.valueOf(difficulty));
                Log.d("Passing gameType", String.valueOf(gameType));

            }
        });

        subtractGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameType = 2;

                Intent intent = new Intent(getApplicationContext(), Game_Activity.class);
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("gameType", gameType);
                startActivity(intent);
                Log.d("Passing Difficulty", String.valueOf(difficulty));
                Log.d("Passing gameType", String.valueOf(gameType));

            }
        });

        multiplicationGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameType = 3;

                Intent intent = new Intent(getApplicationContext(), Game_Activity.class);
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("gameType", gameType);
                startActivity(intent);
                Log.d("Passing Difficulty", String.valueOf(difficulty));
                Log.d("Passing gameType", String.valueOf(gameType));
            }
        });
    }

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

    public void onBackPressed() {
        finish();

    }
}
