package com.liza.fgfandroidapp.sceens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.liza.fgfandroidapp.data.Resource
import com.liza.fgfandroidapp.model.PostItems
import com.liza.fgfandroidapp.model.Posts
import com.liza.fgfandroidapp.network.CheckInternetConnectivity
import com.liza.fgfandroidapp.repository.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val checkInternetConnectivity: CheckInternetConnectivity
) : ViewModel() {

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected
    private var _posts = MutableStateFlow<Resource<Posts>>(Resource.Loading())
    val posts: StateFlow<Resource<Posts>> = _posts.asStateFlow()


    private var currentPage = 1
    private var isCurrentlyLoading = false
    private val _postState: MutableStateFlow<PagingData<PostItems>> =
        MutableStateFlow(PagingData.empty())
    val postState: StateFlow<PagingData<PostItems>> = _postState.asStateFlow()

    init {
        monitorConnectivity()
        loadPost()
    }

    //without paging
    private fun loadPost() {
        if (_isConnected.value) {
            viewModelScope.launch(Dispatchers.IO) {
                _posts.value = Resource.Loading()
                postsRepository.posts.catch { throwable ->
                    _posts.value = when (throwable) {
                        is IOException -> Resource.Error("Network error")
                        else -> Resource.Error("An unexpected error occurred")
                    }
                }.collect {
                    _posts.value = it.data?.let { data -> Resource.Success(data) }
                        ?: Resource.Error("Data is null")
                }
            }
        }
    }


    fun likePost(id: String) {
        viewModelScope.launch {
            postsRepository.likePost(id)
        }
    }

    fun unlikePost(id: String) {
        viewModelScope.launch {
            postsRepository.unlikePost(id)
        }
    }

    fun comment(id: String, comment: String) = viewModelScope.launch {
        postsRepository.commentOnPost(id, comment)
    }

    //with paging
    private fun loadPosts() {
        if (_isConnected.value) {
            if (isCurrentlyLoading) return
            isCurrentlyLoading = true
            try {
                viewModelScope.launch(Dispatchers.IO) {  // Use the injected dispatcher
                    postsRepository.getPosts().cachedIn(viewModelScope).collect {
                        _postState.value = it
                        isCurrentlyLoading = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            val refreshed = postsRepository.getPosts().first()
            _postState.value = refreshed
        }
    }


    private fun monitorConnectivity() {
        viewModelScope.launch {
            while (true) {
                _isConnected.value = checkInternetConnectivity()
                delay(2000) // Polling interval
            }
        }
    }

}