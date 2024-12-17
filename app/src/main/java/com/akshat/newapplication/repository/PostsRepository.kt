package com.akshat.newapplication.repository

import com.akshat.newapplication.data.Resource
import com.akshat.newapplication.model.Posts
import com.akshat.newapplication.network.PostsAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostsRepository @Inject constructor(private val apiService: PostsAPI) {

    val posts: Flow<Resource<Posts>> = flow {
        val posts = apiService.getPostData()
        emit(Resource.Success(posts))
    }
}