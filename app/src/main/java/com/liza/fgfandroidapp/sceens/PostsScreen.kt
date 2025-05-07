package com.liza.fgfandroidapp.sceens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.liza.fgfandroidapp.components.FGFAppBar
import com.liza.fgfandroidapp.data.Resource
import com.liza.fgfandroidapp.model.PostItems

@Composable
fun PostsScreen(
    navController: NavController, postsViewModel: PostsViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        FGFAppBar(
            navController = navController, elevation = 7.dp
        )
    }) { it ->
        Surface(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize()
        ) {
            val isConnected by postsViewModel.isConnected.collectAsState(initial = false)
            var showData by remember { mutableStateOf(false) }


            LaunchedEffect(isConnected) {
                if (isConnected) {
                    showData = true
                }
            }

            if (showData) {
                val posts by postsViewModel.posts.collectAsStateWithLifecycle()
                when (posts) {
                    is Resource.Loading -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is Resource.Success -> {
                        SwipeRefresh(
                            state = rememberSwipeRefreshState(false),
                            onRefresh = { postsViewModel.refresh() }
                        ) {
                            LazyColumn {
                                posts.data?.data?.children?.let {
                                    items(items = it) { children ->
                                        PostRow(children.data, postsViewModel)
                                    }
                                }
                            }
                        }

                    }

                    is Resource.Error -> {
                        Text(
                            text = posts.message.toString(),
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }


            } else {
                Text(
                    text = "No Internet Connection",
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PostRow(
    data: PostItems, postsViewModel: PostsViewModel
) {

    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {},
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        colors = CardColors(
            Color.White, Color.Black, Color.White, Color.White
        ),
        elevation = CardDefaults.cardElevation(6.dp)

    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = data.user,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2
            )
            if (data.imageUrl != "") {
                Image(
                    painter = rememberImagePainter(data.imageUrl),
                    contentDescription = "icon image",
                    modifier = Modifier
                        .size(width = 450.dp, height = 200.dp)
                        .padding(top = 4.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    if (data.isLiked) postsViewModel.unlikePost(data.id) else postsViewModel.likePost(
                        data.id
                    )
                }) {
                    Icon(
                        imageVector = if (data.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null
                    )
                }
                Text("${data.likes} likes")


            }

            Column {
                data.comments.forEach { comment ->
                    Text("", style = MaterialTheme.typography.bodySmall)
                }
                var newComment by remember { mutableStateOf("") }
                Row {
                    TextField(
                        value = newComment,
                        onValueChange = { newComment = it },
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = {
                        if (newComment.isNotBlank()) {
                            postsViewModel.comment(data.id, newComment)
                            newComment = ""
                        }
                    }) {
                        Text("Send")
                    }
                }
            }

        }

    }
}