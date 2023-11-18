package com.example.public_transport_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.public_transport_android.databinding.ActivityLoginBinding
import com.example.public_transport_android.extras.Models
import com.example.public_transport_android.ui.Roles.Base.HomeBaseActivity
import com.example.public_transport_android.ui.Roles.Parada.HomeParadaActivity
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
        //setContentView(R.layout.activity_login)
        //val btnAccess = findViewById<Button>(R.id.btnAccess)


        binding.btnAccess.setOnClickListener {
            fnLogin()
            //val intent = Intent(this, HomeActivity::class.java)
            //startActivity(intent)
        }
        setContentView(view)
    }

    fun fnLogin() {
        //Toast.makeText(baseContext, binding.txtEmail.text, Toast.LENGTH_LONG).show();
        //Toast.makeText(baseContext, binding.edtPassword.text, Toast.LENGTH_LONG).show();

        val client = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()
            .add("email", binding.edtNombreChec.text.toString())
            .add("password", binding.edtPassword.text.toString())
            .build()

        val request = Request.Builder()
            //.url("http://yourip:8000/api/login")
            .url("https://" + EnvUrl.UrlVal + "/api/login")
            .post(formBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    //println(formBody.toString())
                    Toast.makeText(baseContext, "Error: " + e.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.i("jackko", e.message.toString())
                }
            }

            override fun onResponse(call: Call, response: Response) {
                //println(response.body?.string())

                // Se crea una instancia de la clase Gson para desestructurar la respuesta JSON
                var objGson = Gson()

                // Obtener la respuesta del servidor en formato de cadena
                var respuesta = response.body?.string()

                // Desestructurar la respuesta JSON en un objeto RespLogin utilizando Gson
                var objResp = objGson.fromJson(respuesta, Models.RespLogin::class.java)

                // Verificar si el token en el objeto RespLogin está vacío
                if (objResp.token == "") {

                    // Si el token está vacío, mostrar un mensaje de error en el hilo principal de la interfaz de usuario
                    runOnUiThread {
                        Toast.makeText(baseContext, objResp.error, Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Si se recibe un token, obtener el rol del usuario desde el objeto RespLogin
                    val userRole = objResp.rol

                    ///Variable que obtiene a las preferencias compartidas
                    val infoUser = getSharedPreferences("infoUser", Context.MODE_PRIVATE)

                    // Edita las preferencias compartidas.
                    infoUser.edit().apply() {
                        // Almacena el nombre de usuario en las preferencias compartidas.
                        putString("userName", objResp.nombre)

                        // Almacena el rol de usuario en las preferencias compartidas.
                        putString("userRole", userRole.toString())

                        // Aplica los cambios en las preferencias compartidas.
                        apply()
                    }


                    // Verificar el rol del usuario (true o false)
                    if (userRole == true) {


                        // Si el rol es true, redirigir a la actividad HomeActivity
                        //val intent = Intent(baseContext, HomeActivity::class.java)
                        val intent = Intent(baseContext, HomeBaseActivity::class.java)
                        startActivity(intent)
                    } else if (userRole == false) {
                        val intent = Intent(baseContext, HomeParadaActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        })
    }
}