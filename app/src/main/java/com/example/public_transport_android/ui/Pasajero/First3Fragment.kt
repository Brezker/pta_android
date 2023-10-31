package com.example.public_transport_android.ui.Pasajero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.public_transport_android.EnvUrl
import com.example.public_transport_android.databinding.FragmentFirst3Binding
import com.example.public_transport_android.extras.Models
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class First3Fragment : Fragment() {

    // La siguiente propiedad es utilizada para gestionar la vista del fragmento.
    private var _binding: FragmentFirst3Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Infla la vista definida en el archivo XML.
        _binding = FragmentFirst3Binding.inflate(inflater, container, false)
        val root:View  = binding.root

        obtenerUnidadesPasajero()

        return root

    }

    private fun obtenerUnidadesPasajero(){
        // Construye la URL para la solicitud.
        var url = "https://"+ EnvUrl.UrlVal+"/api/paradas"

        val request = Request.Builder()
            .url(url)
            .header("Accept", "application/json")
            .get()
            .build()

        val client= OkHttpClient()
        val gson  = Gson()


        // Realiza la solicitud HTTP de forma asíncrona.
        client.newCall(request).enqueue(object :  Callback{
            override fun onFailure(call: Call, e: IOException) {
                // Muestra un mensaje de error en el contexto de la actividad.
                activity?.runOnUiThread {
                    Toast.makeText(context, "Ocurrió un error: " + e.message.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            override fun onResponse(call: Call, response: Response) {
                //Procesar  la respuesta
                var respuestaPasajero = response.body?.string()

                //Actualizar la  vista con los datos recibidos desde la petición
                activity?.runOnUiThread {
                    //Procesamiento  de respuesta recibida
                    var listaItems = gson.fromJson(respuestaPasajero, Array<Models.Parada>::class.java)

                    //Crear el adaptador perzonalizado llamado UnidadPasajeroAdapter y pasarle la lista de elementos
                    val adapter = PasajeroUnidadAdapter(listaItems.toMutableList())
                    binding.rvDatosUnidadPasajero.layoutManager = LinearLayoutManager(context)
                    binding.rvDatosUnidadPasajero.adapter = adapter
                }
            }
        })
    }

    /**override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /** binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_First3Fragment_to_Second3Fragment)
        }**/
    }**/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}