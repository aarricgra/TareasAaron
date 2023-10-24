package com.example.tareasaaron

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tareasaaron.Data.TaskDatabase
import com.example.tareasaaron.Data.TaskRepository
import com.example.tareasaaron.ui.theme.TareasAaronTheme
import java.util.Calendar

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TareasAaronTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.LightGray
                ) {
                    val taskDatabase = TaskDatabase.getDatabase(this)
                    val taskDao = taskDatabase.taskDao()
                    val repository = TaskRepository(taskDao)
                    val allTasks = repository.allTasks
                    MyApp()
                }
            }
        }
    }

    @Composable
    fun MyApp() {
        var textState by remember { mutableStateOf(TextFieldValue("")) }
        var selectedDateText by remember { mutableStateOf("") }
        var taskList by remember { mutableStateOf(mutableStateListOf<Task>()) }
        val priorityList = listOf("Urgente","Alta", "Media", "Baja")
        var selectedPriority by remember { mutableStateOf(priorityList[3]) }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            //Campo de texto y prioridad
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(32.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                myTextField(textState) { newTextState ->
                    textState = newTextState
                }
                Spacer(modifier = Modifier.padding(8.dp))
                //Desplegable prioridades
                var expanded by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .width(120.dp)
                )
                {
                    var expanded by remember { mutableStateOf(false) }
                    Text(
                        text = selectedPriority,
                        modifier = Modifier
                            .fillMaxHeight()
                            .clickable { expanded = true }
                            .padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center,
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        priorityList.forEach { priority ->
                            DropdownMenuItem({
                                Text(
                                    text = priority,
                                    color = Color.Black
                                )
                            },onClick = {
                                selectedPriority = priority
                                expanded = false
                            })
                        }
                    }
                }

            }

            //Fecha limite
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(32.dp),
                horizontalArrangement = Arrangement.Center,
            ){
                val context = LocalContext.current
                val calendar = Calendar.getInstance()


                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH]
                val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

                val datePicker = DatePickerDialog(
                    context,
                    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                        selectedDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                    }, year, month, dayOfMonth
                )
                datePicker.datePicker.minDate = calendar.timeInMillis

                Button(
                    modifier = Modifier.fillMaxHeight(),
                    shape = RectangleShape,
                    onClick = {
                        datePicker.show()
                    }
                ) {
                    Text(text = "Fecha Límite")
                }
            }

            //Boton añadir
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(32.dp),
                horizontalArrangement = Arrangement.Center,
            ){
                Button(
                    modifier = Modifier.fillMaxHeight(),
                    shape = RectangleShape,
                    onClick = {
                        if (!textState.text.isEmpty()&&!selectedDateText.isEmpty()) {
                            taskList.add(Task(textState.text, mutableStateOf(false),selectedDateText,selectedPriority))
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

    data class Task(val name: String, val completed: MutableState<Boolean>, val endDate: String,val priority:String)

    @Composable
    fun TaskItem(task: Task, onTaskCheckedChange: (Boolean) -> Unit, onDeleteClick: () -> Unit) {
        var backgroundColor = Color.Green
        if (task.priority.equals("Media")) {
            backgroundColor = Color.Yellow
        }
        if (task.priority.equals("Alta")) {
            backgroundColor = Color(255, 153, 0)
        }
        if (task.priority.equals("Urgente")) {
            backgroundColor = Color.Red
        }

        Row(
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row {
                    Text(
                        text = "Prioridad: "+task.priority,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(
                    modifier = Modifier.padding(top=8.dp),
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
                    Text(
                        text = task.endDate,
                        textAlign = TextAlign.End,
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
        Spacer(modifier = Modifier.padding(8.dp))
    }
}
