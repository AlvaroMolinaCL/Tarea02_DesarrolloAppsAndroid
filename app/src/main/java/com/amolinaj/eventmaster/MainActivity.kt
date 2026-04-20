package com.amolinaj.eventmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.amolinaj.eventmaster.ui.navigation.Navigation
import com.amolinaj.eventmaster.ui.theme.EventMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventMasterTheme {
                Navigation()
            }
        }
    }
}

@Composable
fun EventMasterPreviewApp() {
    Navigation()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EventMasterTheme {
        EventMasterPreviewApp()
    }
}