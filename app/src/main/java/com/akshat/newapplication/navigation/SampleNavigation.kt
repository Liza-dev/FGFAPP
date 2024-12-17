package com.akshat.newapplication.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.akshat.newapplication.sceens.PostsScreen
import com.akshat.newapplication.sceens.PostsViewModel

@Composable
fun SampleNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "posts"
    ) {

        composable("posts") {
            val postsViewModel = hiltViewModel<PostsViewModel>()
            PostsScreen(navController = navController, postsViewModel = postsViewModel)
        }


    }


}