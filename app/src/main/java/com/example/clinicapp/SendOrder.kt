package com.example.clinicapp

import android.app.DatePickerDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import java.util.*

class SendOrder : AppCompatActivity() {

    var orderDate = ""

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_order)

        val spinnerDate = findViewById<Spinner>(R.id.spinner)
        spinnerDate.adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, Data.listDate)
        val personField = findViewById<EditText>(R.id.editTextTextPersonName)
        val phoneField = findViewById<EditText>(R.id.editTextPhone)
        val birthdayField = findViewById<EditText>(R.id.editTextBithday)
        val firstField = findViewById<RadioButton>(R.id.radioButton2)

        val thread = Thread {
            val data = "" + Data.lisDoc[Data.selectedIndex].ID + "|" + personField.text.toString() + "|" + phoneField.text.toString() + "|" + birthdayField.text.toString() + "|" + orderDate + "|" + if (firstField.isChecked) "+" else "-"
            Data.connect()
            Data.sendData("order")
            Data.getData()
            Data.sendData(data.toByteArray().size.toString())
            Data.getData()
            Data.sendData(data)
            Data.disconnect()
        }

        findViewById<Button>(R.id.selectBirthdayButton).setOnClickListener {
            val datePicker = DatePickerDialog(it.context)
            datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
                birthdayField.setText("$dayOfMonth.$month.$year")
            }
            datePicker.show()
        }

        findViewById<Button>(R.id.sendButton).setOnClickListener {
            orderDate = Data.listDate[spinnerDate.selectedItemPosition]
            if(checkFields()) {
                thread.start()
                val progressDialog = ProgressDialog(it.context)
                progressDialog.setTitle("Отправка")
                progressDialog.setMessage("Идет отправка сообщения!")
                progressDialog.show()
                thread.takeIf { !thread.isAlive }?.apply {
                    progressDialog.hide()
                    Snackbar.make(it, "Отправлено!", Snackbar.LENGTH_LONG).show()
                    finish()
                }
            }
            else Snackbar.make(it, "Не все поля правильно заполнены!", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun checkFields() : Boolean {
        val personField = findViewById<EditText>(R.id.editTextTextPersonName)
        val phoneField = findViewById<EditText>(R.id.editTextPhone)
        val birthdayField = findViewById<EditText>(R.id.editTextBithday)
        var count = 0;

        if (personField.text.toString() == "")
            return false
        else {
            personField.text.toString().forEach { if (it == ' ') count++ }
            if(count != 2)
                return false
            count = 0
        }

        if (phoneField.text.toString() == "")
            return false
        else if (phoneField.text.toString()[0] != '+')
            return false

        if (birthdayField.text.toString() == "")
            return false
        else {
            birthdayField.text.toString().forEach { if(it == '.') count++ }
            if (count != 2)
                return false
            count = 0
        }

        if (orderDate == "")
            return false

        return true
    }
}