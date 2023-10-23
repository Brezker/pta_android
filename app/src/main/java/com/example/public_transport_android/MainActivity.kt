package com.example.public_transport_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.public_transport_android.ui.Pasajero.PasajeroMainActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // En el método onCreate de tu actividad


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnChecador = findViewById<Button>(R.id.btnChecador)
        val btnPasajero = findViewById<Button>(R.id.btnPasajero)

        btnChecador.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        btnPasajero.setOnClickListener {
            val intent = Intent(this, PasajeroMainActivity::class.java)
            startActivity(intent)
        }
    }
}