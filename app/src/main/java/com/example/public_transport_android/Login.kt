package com.example.public_transport_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.public_transport_android.ui.home.HomeFragment

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // En el método onCreate de tu actividad


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btnAccess = findViewById<Button>(R.id.btnAccess)

        btnAccess.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}