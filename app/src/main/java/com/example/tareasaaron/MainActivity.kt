package com.example.tareasaaron

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tareasaaron.ui.theme.TareasAaronTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TareasAaronTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth().padding(top=16.dp).height(32.dp),
                            horizontalArrangement = Arrangement.Center,
                        ){
                            Escribir()
                            Button(
                                modifier = Modifier.fillMaxHeight(),
                                onClick = {
                                   
                                }
                            ) {
                                Text("Añadir")
                            }
                        }
                    }
                }
            }

        }
    }

    @Composable
    fun Escribir() {
        var textState by remember { mutableStateOf(TextFieldValue("")) }
        BasicTextField(
            value = textState,
            onValueChange = { textState = it },
            modifier= Modifier.
                background(Color.White).
                border(1.dp,Color.Black).
                fillMaxHeight(),
            textStyle= TextStyle(
                color= Color.Black,
                fontSize = 30.sp
            )
        )
    }
}