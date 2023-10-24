package com.example.tareasaaron.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val priority: String,
    val date: String,
    val completed: Boolean
)
