package com.example.public_transport_android.ui.Roles.Base

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.public_transport_android.EnvUrl
import com.example.public_transport_android.R
import com.example.public_transport_android.databinding.FragmentFirstBinding
import com.example.public_transport_android.extras.Models
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Este archivo define la clase FirstFragment, que es un fragmento de android.
 * Se encarga de mostrar una lista de unidades y gestionar la navegación
 * **/
class FirstFragment : Fragment() {

    // La siguiente propiedad es utilizada para gestionar la vista del fragmento.
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    // Método llamado al crear la vista del fragmento.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Infla la vista definida en el archivo XML.
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val root:View  = binding.root

        // Configura un OnClickListener para el botón flotante.
        binding.fabNuevaUnidad.setOnClickListener{
            var navController =  findNavController()
            navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        // Realiza la obtención de datos desde un recurso externo.
        obtenerDatos()
        return root
    }

    fun obtenerDatos(){

        // Construye la URL para la solicitud.
        var url = "https://"+ EnvUrl.UrlVal+"/api/unidades/base"

        val request = Request.Builder()
            .url(url)
            .header("Accept", "application/json")
            .get()
            .build()

        val client =  OkHttpClient()
        val gson =  Gson()

        // Realiza la solicitud HTTP de forma asíncrona.
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                // Muestra un mensaje de error en el contexto de la actividad.
                activity?.runOnUiThread {
                    Toast.makeText(context, "Ocurrió un error: " + e.message.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            override fun onResponse(call: Call, response: Response) {
                // Procesa la respuesta de la solicitud HTTP.
                var respuesta  =  response.body?.string()

                // Actualiza la vista en el hilo principal con los datos recibidos.
                activity?.runOnUiThread {
                    // Se procesa la respuesta recibida, que suele ser en formato JSON.
                    var listaItems = gson.fromJson(respuesta, Array<Models.Unidad>::class.java)

                    // Se crea un adaptador personalizado llamado "EnfermedadAdapter" y
                    // se le pasa la lista de elementos.
                    val adapter =  UnidadAdapter(listaItems.toMutableList())

                    // Se configura el RecyclerView "rvDatosUnidad" con un administrador de diseño lineal.
                    // Esto establece cómo se mostrarán los elementos en la lista.

                    binding.rvDatosUnidad.layoutManager = LinearLayoutManager(context)

                    // Se asocia el adaptador personalizado al RecyclerView.
                    binding.rvDatosUnidad.adapter = adapter
                }
            }
        })
    }


    /**
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("infoUser", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName","")
        val userRole = sharedPreferences.getString("userRole", "")

        val message = "¡Hola , $userName!!"
        /**binding.txtNameParada.text = message

        binding.btnAAdir.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }**/

    }**/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}