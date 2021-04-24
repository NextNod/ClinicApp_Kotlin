package com.example.clinicapp

import java.net.Socket

class Data {
    companion object {
        private const val host = "134.17.135.133"
        private const val port = 801
        var selectedIndex = 0
        val lisDoc = mutableListOf<Doctors>()
        private var socket : Socket = Socket()

        data class Doctors(val ID : Int, val Name : String, val Description : String)

        fun connect() {
            socket = Socket(host, port)
        }
        fun disconnect() {
            socket.close()
        }
        fun getData(length : Int = 255) : String {
            val reader = socket.getInputStream()
            val data = ByteArray(length)
            var result = ByteArray(0)
            reader.read(data)

            data.forEach {
                if(it.toInt() != 0)
                    result += it
            }

            return String(result)
        }
        fun sendData(data : String) {
            val writer = socket.getOutputStream()
            writer.write(data.toByteArray())
        }
    }
}