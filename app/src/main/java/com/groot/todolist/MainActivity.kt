package com.groot.todolist

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var taskInput: EditText
    private lateinit var addButton: Button
    private lateinit var taskListView: ListView

    private var taskList = ArrayList<String>()

    var fileHelper = FileHelper()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
        }

        setContentView(R.layout.activity_main)

        taskInput = findViewById(R.id.editText)
        addButton = findViewById(R.id.button)
        taskListView = findViewById(R.id.list)

        taskList = fileHelper.readData(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, taskList)
        taskListView.adapter = adapter

        addButton.setOnClickListener {
            val taskName: String = taskInput.text.toString()
            taskList.add(taskName)
            taskInput.setText("")
            fileHelper.writeData(taskList, applicationContext)
            adapter.notifyDataSetChanged()
        }

        taskListView.setOnItemClickListener { _, _, position, _ ->

            val alert = AlertDialog.Builder(this)
            alert.setTitle("Delete")
            alert.setMessage("Do you want to delete this task from the list?")
            alert.setCancelable(false)
            alert.setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            alert.setPositiveButton("Yes") { _, _ ->
                taskList.removeAt(position)
                adapter.notifyDataSetChanged()
                fileHelper.writeData(taskList, applicationContext)
            }

            val alertDialog = alert.create()
            alertDialog.setOnShowListener {
                // Set text color for positive button
                val positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                positiveButton?.setTextColor(resources.getColor(android.R.color.white, null))

                // Set text color for negative button
                val negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                negativeButton?.setTextColor(resources.getColor(android.R.color.white, null))
            }

            alertDialog.show()
        }
    }
}
