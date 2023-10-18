package com.example.public_transport_android.ui.Roles.Parada

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.public_transport_android.EnvUrl
import com.example.public_transport_android.R
import com.example.public_transport_android.databinding.FragmentFirst2Binding
import com.example.public_transport_android.extras.Models
import com.example.public_transport_android.ui.Roles.Base.UnidadAdapter
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Este archivo define la clase First2Fragment, que es un fragmento de android.
 * Se encarga de mostrar una lista de unidades y gestionar la navegación
 * **/
class First2Fragment : Fragment(),UnidadParadaAdapter.OnConfirmListener {


    // La siguiente propiedad es utilizada para gestionar la vista del fragmento.
    private var _binding: FragmentFirst2Binding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    // Define una lista para tus elementos de parada (debes adaptar esto a tus necesidades).
    private val listaItemsParada = mutableListOf<Models.Unidad>()
    private  lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: UnidadParadaAdapter

    // Método llamado al crear la vista del fragmento.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Infla la vista definida en el archivo XML.
        _binding = FragmentFirst2Binding.inflate(inflater, container, false)
        val root = binding.root

        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout)

        // Configura el SwipeRefreshLayout para la recarga de datoss
        swipeRefreshLayout.setOnRefreshListener {
            obtenerUnidadesParada()
        }

        // Configura el RecyclerView y su adaptador
        adapter = UnidadParadaAdapter(listaItemsParada, this)
        binding.rvDatosUnidadParada.layoutManager = LinearLayoutManager(context)
        binding.rvDatosUnidadParada.adapter = adapter

        // Carga los datos cuando se crea el fragmento
        obtenerUnidadesParada()

        return root
    }

    private fun obtenerUnidadesParada(){
        // Construye la URL para la solicitud.
        var url = "http://"+ EnvUrl.UrlVal+":8000/api/unidades/parada"

        val request = Request.Builder()
            .url(url)
            .header("Accept", "application/json")
            .get()
            .build()

        val client = OkHttpClient()
        val gson = Gson()

        // Realiza la solicitud HTTP de forma asíncrona.
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Muestra un mensaje de error en el contexto de la actividad.
                activity?.runOnUiThread {
                    Toast.makeText(context, "Ocurrió un error: " + e.message.toString(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.isRefreshing = false
                }
            }


            override fun onResponse(call: Call, response: Response) {
                // Procesa la respuesta de la solicitud HTTP.
                var respuesta  = response.body?.string()
                listaItemsParada.clear() // Limpia la lista antes de cargar los nuevos datos.
                listaItemsParada.addAll(gson.fromJson(respuesta, Array<Models.Unidad>::class.java))


                // Actualiza la vista en el hilo principal con los datos recibidos.
                activity?.runOnUiThread {
                    adapter.notifyDataSetChanged()
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })

    }

    // Método llamado cuando se confirma la acción en el adaptador.
    override fun onConfirmAction() {
        obtenerUnidadesParada()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}