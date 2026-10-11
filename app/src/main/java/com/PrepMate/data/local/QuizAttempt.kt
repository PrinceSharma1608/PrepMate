package com.PrepMate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_attempts")
data class QuizAttempt(

    @PrimaryKey
    val firestoreId: String,

    val timestamp: Long,

    val score: Int
)