package com.example.clinicapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class docPreview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_preview)

        findViewById<TextView>(R.id.nameDoc).text = Data.lisDoc[Data.selectedIndex].Name
        findViewById<TextView>(R.id.discDoc).text = Data.lisDoc[Data.selectedIndex].Description
        Data.listDate.clear()

        val thread = Thread {
            Data.connect()
            Data.sendData("orderDate")
            Data.getData()
            Data.sendData(Data.lisDoc[Data.selectedIndex].ID.toString())
            val length = Data.getData()
            val data = Data.getData(length.toInt())
            data.split(',').forEach { if (it != "") Data.listDate.add(it) }
        }
        thread.start()

        findViewById<Button>(R.id.selectButton).setOnClickListener {
            if(!thread.isAlive)
                if(Data.listDate.size != 0)
                    startActivity(Intent(this, SendOrder::class.java))
                else
                    Snackbar.make(it, "Данный доктор не имеет свободного времени!", Snackbar.LENGTH_LONG).show()
            else
                Snackbar.make(it, "Ожидайте загрузку расписания!", Snackbar.LENGTH_LONG).show()
        }
    }
}