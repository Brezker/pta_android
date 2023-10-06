/*package com.brezker.myapplication.ui.cita

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.brezker.myapplication.EnvUrl
import com.brezker.myapplication.R
import com.brezker.myapplication.databinding.FragmentCitaBinding
import com.brezker.myapplication.extras.CitaAdapter
import com.brezker.myapplication.extras.Models
import com.brezker.myapplication.extras.DoctorAdapter
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class CitaFragment : Fragment() {

    private var _binding: FragmentCitaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val citaViewModel =
            ViewModelProvider(this).get(CitaViewModel::class.java)

        _binding = FragmentCitaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabNuevoCita.setOnClickListener {
            var navController = findNavController()
            navController.navigate(R.id.nav_nuevo_cita)
        }

        obtenerDatos()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun obtenerDatos(){
        //var url("http://yourip:8000/api/login")
        var url = "http://"+EnvUrl.UrlVal+":8000/api/citas"

        var request = Request.Builder()
            .url(url)
            .header("Accept", "application/json")
            //.header("Autorization", "Bearer" + TOKEN)
            .get()
            .build()
        val client = OkHttpClient()
        var gson = Gson()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(context, "Ocurrio un error: " + e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                var respuesta = response.body?.string()

                println("Respuesta: "+ respuesta)

                activity?.runOnUiThread {
                    var listItems = gson.fromJson(respuesta, Array<Models.Cita>::class.java)
                    val adapter = CitaAdapter(listItems.toMutableList())
                    binding.rvDatosCita.layoutManager = LinearLayoutManager(context)
                    binding.rvDatosCita.adapter = adapter
                }
            }
        })
    }
}*/