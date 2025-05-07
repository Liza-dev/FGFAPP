package com.liza.fgfandroidapp.network

import com.liza.fgfandroidapp.model.PostDto
import com.liza.fgfandroidapp.model.Posts
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET(value = "posts")
    suspend fun getPostData(): Posts

    @GET("posts")
    suspend fun getPosts(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): List<PostDto>


    @POST("posts/{postId}/like")
    suspend fun likePost(@Path("postId") postId: String): retrofit2.Response<Unit>

    @DELETE("posts/{postId}/unlike")
    suspend fun unlikePost(@Path("postId") postId: String): retrofit2.Response<Unit>

    @POST("posts/{id}/comment")
    suspend fun commentOnPost(@Path("id") id: String, @Body comment: String): retrofit2.Response<Unit>
}