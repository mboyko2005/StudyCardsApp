package com.example.studycardsapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Card {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String question;
    private String answer;

    // Конструктор
    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) { this.answer = answer; }
}
