package com.example.public_transport_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.public_transport_android.databinding.ActivityLoginBinding
import com.example.public_transport_android.extras.Models
import com.example.public_transport_android.ui.home.HomeFragment
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

private lateinit var binding: ActivityLoginBinding
class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(R.layout.activity_login)
        val btnAccess = findViewById<Button>(R.id.btnAccess)
        btnAccess.setOnClickListener {
            fnLogin()
            //val intent = Intent(this, HomeActivity::class.java)
            //startActivity(intent)
        }
    }
    fun fnLogin(){
        //Toast.makeText(baseContext, binding.txtEmail.text, Toast.LENGTH_LONG).show();
        //Toast.makeText(baseContext, binding.edtPassword.text, Toast.LENGTH_LONG).show();

        val client = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()
            .add("email", binding.edtNombreChec.text.toString())
            .add("password", binding.edtPassword.text.toString())
            .build()

        val request = Request.Builder()
            //.url("http://yourip:8000/api/login")
            .url("http://"+EnvUrl.UrlVal+":8000/api/login")
            .post(formBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    //println(formBody.toString())
                    Toast.makeText(baseContext, "Error: " + e.message.toString(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.body?.string())

                var objGson = Gson()
                var respuesta = response.body?.string()
                var objResp = objGson.fromJson(respuesta, Models.RespLogin::class.java)

                if(objResp.token == ""){
                    runOnUiThread {
                        Toast.makeText(baseContext, objResp.error, Toast.LENGTH_LONG).show()
                    }
                } else {
                    /*
                    runOnUiThread {
                        Toast.makeText(baseContext, "Correcto!", Toast.LENGTH_LONG).show()
                    }*/
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }
}