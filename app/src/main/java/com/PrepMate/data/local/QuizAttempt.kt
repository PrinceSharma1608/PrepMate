package com.PrepMate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_attempts")
data class QuizAttempt(

    @PrimaryKey
    val firestoreId: String,

    val subject: String = "General Quiz",

    val score: Int,

    val totalQuestions: Int = 10,

    val timestamp: Long
)
