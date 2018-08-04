package com.example.bryan.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class TitleScreenActivity extends AppCompatActivity {

    Button additionGame, subtractGame, MultGame;
    int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        difficulty = 2; //By default Moderate is selected

        additionGame = findViewById(R.id.additionGame);
        subtractGame = findViewById(R.id.subtractGame);

        additionGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), GameAdditionActivity.class);
                intent.putExtra("difficulty", difficulty);
                startActivity(intent);
                Log.d("Passing Difficulty", String.valueOf(difficulty));

            }
        });

        subtractGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), GameSubtractionActivity.class);
                intent.putExtra("difficulty", difficulty);
                startActivity(intent);
                Log.d("Passing Difficulty", String.valueOf(difficulty));
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
            case R.id.difficultButton:
                if (checked) {
                    difficulty = 3;
                }
                break;
        }
    }
}
