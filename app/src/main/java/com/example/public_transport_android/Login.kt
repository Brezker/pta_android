package com.example.public_transport_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.public_transport_android.databinding.ActivityLoginBinding
import com.example.public_transport_android.extras.Models
import com.example.public_transport_android.ui.gallery.GalleryFragment
import com.example.public_transport_android.ui.home.HomeFragment
import com.example.public_transport_android.ui.home.HomeViewModel
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

    /**linea añadida
    private  lateinit var homeViewModel : HomeViewModel
    **/
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        //setContentView(R.layout.activity_login)
        //val btnAccess = findViewById<Button>(R.id.btnAccess)


        /** Inicializar HomeViewModel
        homeViewModel =  ViewModelProvider(this).get(HomeViewModel::class.java)
         **/

        binding.btnAccess.setOnClickListener {
            fnLogin()
            //val intent = Intent(this, HomeActivity::class.java)
            //startActivity(intent)
        }
        setContentView(view)
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
                //println(response.body?.string())

                // Se crea una instancia de la clase Gson para desestructurar la respuesta JSON
                var objGson = Gson()

                // Obtener la respuesta del servidor en formato de cadena
                var respuesta = response.body?.string()

                // Desestructurar la respuesta JSON en un objeto RespLogin utilizando Gson
                var objResp = objGson.fromJson(respuesta, Models.RespLogin::class.java)

                // Verificar si el token en el objeto RespLogin está vacío
                if(objResp.token == ""){

                    // Si el token está vacío, mostrar un mensaje de error en el hilo principal de la interfaz de usuario
                    runOnUiThread {
                        Toast.makeText(baseContext, objResp.error, Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Si se recibe un token, obtener el rol del usuario desde el objeto RespLogin
                    val userRole = objResp.rol

                    // Verificar el rol del usuario (true o false)
                    if (userRole == true){

                        /**homeViewModel.(objResp.nombre);**/

                        // Si el rol es true, redirigir a la actividad HomeActivity
                        val intent = Intent(baseContext, HomeActivity::class.java)
                        startActivity(intent)
                    }else if (userRole == false){
                        runOnUiThread {
                            Toast.makeText(baseContext, "Usuario con rol 0", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }
}