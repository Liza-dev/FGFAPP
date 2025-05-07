package com.liza.fgfandroidapp.model

import com.google.gson.annotations.SerializedName

data class PostItems(
    val id: String,
    val user: String,
    val imageUrl: String,
    val likes: Int,
    val comments: List<Comment>,
    var isLiked: Boolean = false  // Added isLiked property
)

// Data class representing the structure of a post as received from the API.
data class PostDto(
    val id: String,
    val user: String,
    @SerializedName("image")  //  adjust the property name to match the API response
    val imageUrl: String,
    val likes: Int,
    val comments: List<CommentDto>
)

// Extension function to convert PostDto to the domain model Post.
fun PostDto.toDomain(): PostItems {
    return PostItems(
        id = id,
        imageUrl = imageUrl,
        likes = likes,
        comments = emptyList(), // Initialize with empty list, will be populated later
        isLiked = false,
        user = user
    )
}
