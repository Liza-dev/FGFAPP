package com.liza.fgfandroidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.liza.fgfandroidapp.navigation.FGFNavigation
import com.liza.fgfandroidapp.ui.theme.NewApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApp()
}

@Composable
fun MyApp() {
    NewApplicationTheme {
        FGFNavigation()
    }

}