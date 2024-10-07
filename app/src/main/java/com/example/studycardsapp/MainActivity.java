package com.example.studycardsapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnStartStudy;
    private MaterialButton btnAddCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartStudy = findViewById(R.id.btnStartStudy);
        btnAddCard = findViewById(R.id.btnAddCard);

        btnStartStudy.setOnClickListener(view -> startActivity(new Intent(this, StudyCardsActivity.class)));
        btnAddCard.setOnClickListener(view -> startActivity(new Intent(this, AddCardActivity.class)));
    }
}