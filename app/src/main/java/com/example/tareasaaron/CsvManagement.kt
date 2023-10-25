package com.example.tareasaaron

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class CsvManagement {




    companion object {
        fun saveOnCsv(taskList: List<MainActivity.Task>,context: Context) {
            try {
                val fileWriter = FileWriter(context.filesDir.path+ File.separator+"tareas.csv")
                val csvPrinter = CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader("Name", "Completed", "EndDate", "Priority"))

                for (task in taskList) {
                    csvPrinter.printRecord(task.name, task.completed.value, task.endDate, task.priority)
                }

                csvPrinter.flush()
                csvPrinter.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        fun loadFromCSV(context: Context): List<MainActivity.Task> {
            val taskList = mutableListOf<MainActivity.Task>()

            try {
                val fileReader = FileReader(context.filesDir.path+ File.separator+"tareas.csv")
                val csvParser = CSVParser(fileReader, CSVFormat.DEFAULT.withHeader().withSkipHeaderRecord())

                for (csvRecord in csvParser) {
                    val name = csvRecord.get("Name")
                    val completed = csvRecord.get("Completed").toBoolean()
                    val endDate = csvRecord.get("EndDate")
                    val priority = csvRecord.get("Priority")

                    taskList.add(MainActivity.Task(name, mutableStateOf(completed), endDate, priority))
                }

                csvParser.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return taskList
        }
    }

}