package com.example.clinicapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.clinicapp.Data.Companion.connect
import com.example.clinicapp.Data.Companion.sendData
import com.example.clinicapp.Data.Companion.getData
import com.example.clinicapp.Data.Companion.Doctors
import com.example.clinicapp.Data.Companion.disconnect
import com.google.android.material.snackbar.Snackbar
import java.net.Socket

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list : MutableList<String> = mutableListOf()
        val listDoc = findViewById<ListView>(R.id.listDoc)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
        listDoc.adapter = adapter

        listDoc.setOnItemClickListener { _, _, position, _ ->
            Data.selectedIndex = position
            startActivity(Intent(this, docPreview::class.java))
        }

        val thread = Thread {
            connect()
            sendData("get")
            val length = getData().toInt()
            Thread.sleep(5)

            getData(length).split(';').forEach {
                if(it != "") {
                    val doc = it.split(':')
                    list.add(doc[1])
                    Data.lisDoc.add(Doctors(doc[0].toInt(), doc[1], doc[2]))
                }
            }

            disconnect()
            adapter.notifyDataSetChanged()
        }

        thread.start()
        thread.join()
    }
}