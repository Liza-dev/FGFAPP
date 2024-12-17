package com.akshat.newapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.akshat.newapplication.navigation.SampleNavigation
import com.akshat.newapplication.sceens.PostsScreen
import com.akshat.newapplication.ui.theme.NewApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewApplicationTheme {
                MyApp()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewApplicationTheme {
        MyApp()
    }
}

@Composable
fun MyApp(){
    Scaffold(modifier = Modifier.fillMaxSize()) { it ->
        Surface(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            SampleNavigation()
        }

    }
}