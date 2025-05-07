package com.liza.fgfandroidapp.model

data class Comment(
    val id: String,
    val userId: String,
    val userName: String,
    val text: String,
    val timestamp: Long
)

data class CommentDto(
    val id: String,
    val userId: String,
    val userName: String,
    val text: String,
    val timestamp: Long
)