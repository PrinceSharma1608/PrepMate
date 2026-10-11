package com.PrepMate

data class AttemptResponse(
    val questionId: String,
    val question: String,
    val correctOption: String,
    var selectedOption: String?= null
)