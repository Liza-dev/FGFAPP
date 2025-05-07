package com.liza.fgfandroidapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.liza.fgfandroidapp.sceens.PostsScreen
import com.liza.fgfandroidapp.sceens.PostsViewModel

@Composable
fun FGFNavigation() {
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