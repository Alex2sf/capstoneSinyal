package com.example.sinyal

data class Question(
    val imageResourceId: Int,
    val correctAnswer: String,
    val options: List<String>
)
