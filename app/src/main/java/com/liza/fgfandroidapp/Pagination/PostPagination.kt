package com.liza.fgfandroidapp.Pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.liza.fgfandroidapp.model.PostItems
import com.liza.fgfandroidapp.model.toDomain
import com.liza.fgfandroidapp.network.ApiServices
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PostPagingSource(
    private val apiService: ApiServices,
    private val dispatcher: CoroutineDispatcher
) : PagingSource<Int, PostItems>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostItems> {
        val page = params.key ?: 1
        return withContext(dispatcher) { // Use the injected dispatcher
            try {
                val response = apiService.getPosts(page, params.loadSize)
                LoadResult.Page(
                    data = response.map { it.toDomain() },
                    prevKey = if (page > 1) page - 1 else null,
                    nextKey = if (response.isNotEmpty()) page + 1 else null
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PostItems>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}