package com.example.tareasaaron.Data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)


    @Query("SELECT * from Tasks WHERE id = :id")
    fun getTask(id:Int):LiveData<Task>

    @Query("SELECT * from Tasks ORDER BY id ASC")
    fun getAllTasks():LiveData<List<Task>>
}
