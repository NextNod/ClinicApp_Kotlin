package com.example.clinicapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class docPreview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_preview)

        findViewById<TextView>(R.id.nameDoc).text = Data.lisDoc[Data.selectedIndex].Name
        findViewById<TextView>(R.id.discDoc).text = Data.lisDoc[Data.selectedIndex].Description

        findViewById<Button>(R.id.selectButton).setOnClickListener {
            startActivity(Intent(this, SendOrder::class.java))
        }
    }
}