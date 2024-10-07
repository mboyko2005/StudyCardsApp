package com.example.studycardsapp;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.button.MaterialButton;
import com.example.studycardsapp.database.AppDatabase;
import com.example.studycardsapp.database.Card;

import java.util.ArrayList;
import java.util.List;

public class StudyCardsActivity extends AppCompatActivity {

    private boolean showingAnswer = false;
    private List<Card> cardList = new ArrayList<>();
    private int currentIndex = 0;
    private TextView questionTextView;
    private MaterialButton flipButton;
    private MaterialButton nextButton;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_cards);

        questionTextView = findViewById(R.id.textViewQuestion);
        flipButton = findViewById(R.id.buttonFlip);
        nextButton = findViewById(R.id.buttonNext);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "cards-database")
                .allowMainThreadQueries()
                .build();

        loadCards();

        if (!cardList.isEmpty()) {
            displayQuestion();
        } else {
            questionTextView.setText("Нет доступных карточек для изучения.");
        }

        flipButton.setOnClickListener(view -> flipCard());
        nextButton.setOnClickListener(view -> showNextCard());
    }

    private void flipCard() {
        if (showingAnswer) {
            displayQuestion();
        } else {
            displayAnswer();
        }
        showingAnswer = !showingAnswer;
    }

    private void displayQuestion() {
        if (currentIndex < cardList.size()) {
            questionTextView.setText(cardList.get(currentIndex).getQuestion());
            questionTextView.setGravity(Gravity.CENTER);
            flipButton.setText(R.string.show_answer);
        }
    }

    private void displayAnswer() {
        if (currentIndex < cardList.size()) {
            questionTextView.setText(cardList.get(currentIndex).getAnswer());
            questionTextView.setGravity(Gravity.CENTER);
            flipButton.setText(R.string.show_question);
        }
    }

    private void showNextCard() {
        if (!cardList.isEmpty()) {
            currentIndex = (currentIndex + 1) % cardList.size();
            displayQuestion();
            showingAnswer = false;
        }
    }

    private void loadCards() {
        cardList = db.cardDao().getAllCards();
        if (cardList == null) {
            cardList = new ArrayList<>();
        }
    }
}
