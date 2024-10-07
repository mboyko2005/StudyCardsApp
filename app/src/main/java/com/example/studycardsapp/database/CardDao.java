package com.example.studycardsapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CardDao {
    @Insert
    void insert(Card card);

    @Query("SELECT * FROM Card")
    List<Card> getAllCards();
}
