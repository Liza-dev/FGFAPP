package com.akshat.newapplication.sceens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.akshat.newapplication.components.SampleAppBar
import com.akshat.newapplication.data.Resource
import com.akshat.newapplication.model.PostItems

@Composable
fun PostsScreen(
    navController: NavController, postsViewModel: PostsViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        SampleAppBar(
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
                        Text(text = "Loading...", fontSize = 15.sp, textAlign = TextAlign.Center)
                    }

                    is Resource.Success -> {
                        LazyColumn {
                            posts.data?.data?.children?.let { it ->
                                items(items = it) { children ->
                                    PostRow(children.data)
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


            }
            else{
                Text(text = "No Internet Connection", fontSize = 15.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun PostRow(
    data: PostItems
) {

    var expanded by remember {
        mutableStateOf(false)
    }

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

            if (data.thumbnail != "self") {
                Image(
                    painter = rememberImagePainter(data.thumbnail),
                    contentDescription = "icon image",
                    modifier = Modifier
                        .size(width = 450.dp, height = 200.dp)
                        .padding(top = 4.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 6.dp, top = 10.dp, bottom = 4.dp, end = 6.dp)
                    .fillMaxWidth(), horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Title : ${data.title}",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2
                )
                Text(
                    text = "Author : ${data.author}", style = MaterialTheme.typography.bodyMedium
                )

                Column(
                    modifier = Modifier
                        .padding(
                            start = 6.dp, top = 10.dp, bottom = 4.dp, end = 6.dp
                        )
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    if (data.selftext != "") {
                        HorizontalDivider(modifier = Modifier.padding(top = 5.dp))


                        Text(
                            text = "Click below arrow for description",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Thin,
                            textAlign = TextAlign.Center
                        )
                        AnimatedVisibility(visible = expanded) {
                            Column() {
                                Text(buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            color = Color.DarkGray, fontSize = 13.sp
                                        )
                                    ) {
                                        append("Description : ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            color = Color.DarkGray,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append(data.selftext)
                                    }
                                }, maxLines = 8)
                            }
                        }
                        Icon(
                            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Down Arrow",
                            modifier = Modifier
                                .size(25.dp)
                                .clickable {
                                    expanded = !expanded
                                },
                            tint = Color.DarkGray
                        )
                    }

                }


            }

        }

    }


}