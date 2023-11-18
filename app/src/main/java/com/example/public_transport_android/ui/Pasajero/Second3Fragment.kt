package com.example.public_transport_android.ui.Pasajero

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.public_transport_android.EnvUrl
import com.example.public_transport_android.R
import com.example.public_transport_android.databinding.FragmentSecond3Binding
import com.example.public_transport_android.extras.Models
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


private const val ARG_PARAM1 = "json_parada"
private const val ARG_PARAM2 = "param2"

//Variable globales
private var idParada: Int? = null
private lateinit var binding: FragmentSecond3Binding

class Second3Fragment : Fragment(), UnidadesPasajeroAdapter.OnConfirmListener {
    private var json_parada: String? = null
    private var param2: String? = null
    private var _binding: FragmentSecond3Binding? = null

    private val binding get() = _binding!!

    //Definir una lista para las diferentes unidades
    private val listaParadasUnidad = mutableListOf<Models.Unidad>()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: UnidadesPasajeroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            json_parada = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecond3Binding.inflate(inflater, container, false)
        val root: View = binding.root
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayoutPasajero)


        if (json_parada != null) {
            var gson = Gson()
            var objUnidadPasajero = gson.fromJson(json_parada, Models.Parada::class.java)

            idParada = objUnidadPasajero.id

            swipeRefreshLayout.setOnRefreshListener {
                obtenerParadas(objUnidadPasajero.id)
            }

            adapter = UnidadesPasajeroAdapter(listaParadasUnidad, this)
            binding.rvDatosUnidadesPasajero.layoutManager = LinearLayoutManager(context)
            binding.rvDatosUnidadesPasajero.adapter = adapter

            obtenerParadas(objUnidadPasajero.id)
        }
        return root
    }

    private fun obtenerParadas(idParada: Int) {
        val url = "https://" + EnvUrl.UrlVal + "/api/unidades/pasajero/$idParada"

        val request = Request.Builder()
            .url(url)
            .header("Accept", "application/json")
            .get()
            .build()

        val client = OkHttpClient()
        val gson1 = Gson()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Muestra un mensaje de error en el contexto de la actividad.
                activity?.runOnUiThread {
                    Toast.makeText(
                        context,
                        "Ocurrió un error: " + e.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }

            override fun onResponse(call: Call, response: Response) {
                var respuesta = response.body?.string()
                listaParadasUnidad.clear()
                listaParadasUnidad.addAll(
                    gson1.fromJson(
                        respuesta,
                        Array<Models.Unidad>::class.java
                    )
                )
                activity?.runOnUiThread {
                    adapter.notifyDataSetChanged()
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })

    }

    override fun onConfirmAction() {
        idParada?.let {
            obtenerParadas(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}