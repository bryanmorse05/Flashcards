package com.example.bryan.flashcards;

public class GameData {

    int answerSelectionBatch;   //Used to figure out which batch of random answers to give
    int randomNumber1;          //The first number of the equation
    int randomNumber2;          //The second number of the equation
    int correctAnswer;          //The answer to the equation
    int difficulty;             //The difficulty level (easy, moderate, hard)
    int gameType;               //The type of game (add, sub, mult)
    int score;                  //Number of correct answers
    int wrong;                  //Number of incorrect answers
    int number1Min;             //The minimum number value for the equation
    int number1Max;             //The maximum number value for the equation
    int timerValue;             //The starting timer value
    int timerLength;            //The length the of the timer
    int timeElapsed;            //The amount of time that has elapsed

    public int getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(int timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public int getTimerLength() {
        return timerLength;
    }

    public void setTimerLength(int timerLength) {
        this.timerLength = timerLength;
    }

    public int getAnswerSelectionBatch() {
        return answerSelectionBatch;
    }

    public void setAnswerSelectionBatch(int answerSelectionBatch) {
        this.answerSelectionBatch = answerSelectionBatch;
    }

    public int getRandomNumber1() {
        return randomNumber1;
    }

    public void setRandomNumber1(int randomNumber1) {
        this.randomNumber1 = randomNumber1;
    }

    public int getRandomNumber2() {
        return randomNumber2;
    }

    public void setRandomNumber2(int randomNumber2) {
        this.randomNumber2 = randomNumber2;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public int getNumber1Min() {
        return number1Min;
    }

    public void setNumber1Min(int number1Min) {
        this.number1Min = number1Min;
    }

    public int getNumber1Max() {
        return number1Max;
    }

    public void setNumber1Max(int number1Max) {
        this.number1Max = number1Max;
    }

    public int getTimerValue() {
        return timerValue;
    }

    public void setTimerValue(int timerValue) {
        this.timerValue = timerValue;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public int additionGame(int randomNumber1, int randomNumber2) {

        correctAnswer = randomNumber1 + randomNumber2;
        return correctAnswer;
    }

    public void additionDifficulty(int difficulty) {

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
    }

    public int subtractionGame(int randomNumber1, int randomNumber2) {

        correctAnswer = randomNumber1 - randomNumber2;
        return correctAnswer;
    }

    public void subtractionDifficulty(int difficulty) {

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
    }

    public int multiplicationGame(int randomNumber1, int randomNumber2) {
        correctAnswer = randomNumber1 * randomNumber2;
        return correctAnswer;
    }

    public void multiplicationDifficulty(int difficulty) {

        switch (difficulty) {
            case 1:
                number1Min = 1;
                number1Max = 9;
                break;
            case 2:
                number1Min = 1;
                number1Max = 10;
                break;
            case 3:
                number1Min = 10;
                number1Max = 100;
                break;
        }
    }

}
