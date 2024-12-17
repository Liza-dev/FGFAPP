package com.akshat.newapplication.network

import com.akshat.newapplication.model.Posts
import retrofit2.http.GET

interface PostsAPI {
    @GET(value = "r/travel/top.json")
    suspend fun getPostData(): Posts
}