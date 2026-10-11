package com.PrepMate

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_attempts")
data class QuizAttempt(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val firestoreId: String,

    val timestamp: Long,

    val score: Int
)