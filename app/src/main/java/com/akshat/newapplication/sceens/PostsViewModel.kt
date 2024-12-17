package com.akshat.newapplication.sceens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshat.newapplication.data.Resource
import com.akshat.newapplication.model.Posts
import com.akshat.newapplication.network.CheckInternetConnectivity
import com.akshat.newapplication.repository.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val postsRepository: PostsRepository,
                                         private val checkInternetConnectivity: CheckInternetConnectivity) : ViewModel() {

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected
    private val _posts = MutableStateFlow<Resource<Posts>>(Resource.Loading())
    val posts: StateFlow<Resource<Posts>> = _posts.asStateFlow()

    init {
        monitorConnectivity()
        if (_isConnected.value){
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

    private fun monitorConnectivity() {
        viewModelScope.launch {
            while (true) {
                _isConnected.value = checkInternetConnectivity()
                delay(2000) // Polling interval
            }
        }
    }

}