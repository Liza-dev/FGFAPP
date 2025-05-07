package com.liza.fgfandroidapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.liza.fgfandroidapp.Pagination.PostPagingSource
import com.liza.fgfandroidapp.data.Resource
import com.liza.fgfandroidapp.model.PostItems
import com.liza.fgfandroidapp.model.Posts
import com.liza.fgfandroidapp.network.ApiServices
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val apiService: ApiServices,
    private val dispatcher: CoroutineDispatcher
) {

    //without paging
    val posts: Flow<Resource<Posts>> = flow {
        val posts = apiService.getPostData()
        emit(Resource.Success(posts))
    }

    //with paging
    fun getPosts(): Flow<PagingData<PostItems>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PostPagingSource(apiService, dispatcher) }
    ).flow.flowOn(dispatcher)

    suspend fun likePost(id: String) = apiService.likePost(id)

    suspend fun unlikePost(id: String) = apiService.unlikePost(id)

    suspend fun commentOnPost(id: String, comment: String) = apiService.commentOnPost(id, comment)


}

