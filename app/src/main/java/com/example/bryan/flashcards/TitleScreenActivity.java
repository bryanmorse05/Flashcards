package com.example.bryan.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class TitleScreenActivity extends AppCompatActivity {

    Button additionGame, subtractGame, multiplicationGame;
    int difficulty, gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        difficulty = 2; //By default Moderate is selected

        additionGame = findViewById(R.id.additionGame);
        subtractGame = findViewById(R.id.subtractGame);
        multiplicationGame = findViewById(R.id.multiplyGame);

        additionGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameType = 1;

                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
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

                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
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

                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("gameType", gameType);
                startActivity(intent);
                Log.d("Passing Difficulty", String.valueOf(difficulty));
                Log.d("Passing gameType", String.valueOf(gameType));
            }
        });
    }

    public void onDifficultyButtonClick(View view) {

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
    }
}
