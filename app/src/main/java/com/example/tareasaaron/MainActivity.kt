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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tareasaaron.ui.theme.TareasAaronTheme

class MainActivity : ComponentActivity() {
    var firstTime = true;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TareasAaronTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }

    @Composable
    fun MyApp() {
        var textState by remember { mutableStateOf(TextFieldValue("")) }

        var taskList by remember { mutableStateOf(mutableStateListOf<Task>()) }

        if(firstTime) {
            for (i in 1..20) {
                taskList.add(Task("Task $i", remember { mutableStateOf(false) }))
            }
            firstTime=false
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(32.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                myTextField(textState) { newTextState ->
                    textState = newTextState
                }
                Button(
                    modifier = Modifier.fillMaxHeight(),
                    shape = RectangleShape,
                    onClick = {
                        if (!textState.text.isEmpty()) {
                            taskList.add(Task(textState.text, mutableStateOf(false) ))
                            textState = TextFieldValue("")
                        }
                    }
                ) {
                    Text("Añadir")
                }
            }

            LazyColumn {
                items(taskList) { task ->
                    TaskItem(task = task,
                        onTaskCheckedChange = { isChecked ->
                        task.completed.value = isChecked
                    }, onDeleteClick = {taskList.remove(task)})
                }
            }
        }
    }

    @Composable
    fun myTextField(
        textState: TextFieldValue,
        onValueChange: (TextFieldValue) -> Unit
    ) {
        BasicTextField(
            value = textState,
            onValueChange = { newTextState ->
                onValueChange(newTextState)
            },
            modifier = Modifier
                .background(Color.White)
                .border(1.dp, Color.Black)
                .fillMaxHeight(),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 30.sp
            )
        )
    }

    data class Task(val name: String, val completed: MutableState<Boolean>)

    @Composable
    fun TaskItem(task: Task, onTaskCheckedChange: (Boolean) -> Unit, onDeleteClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed.value,
                onCheckedChange = { isChecked ->
                    onTaskCheckedChange(isChecked)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = task.name,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar tarea",
                )
            }
        }
    }
}
