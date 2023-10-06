/*package com.brezker.myapplication.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.brezker.myapplication.EnvUrl
import com.brezker.myapplication.R
import com.brezker.myapplication.databinding.FragmentNuevoPacienteBinding
import com.brezker.myapplication.extras.Models
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "json_paciente"
//private val tiposSangre = arrayOf("A","B","AB","O")
private var id_paciente: Int = 0
private lateinit var binding: FragmentNuevoPacienteBinding
private lateinit var spinner: Spinner
//private var selectedType: String? = null
private var selectedType: String = ""

/**
 * A simple [Fragment] subclass.
 * Use the [NuevoPacienteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class NuevoPacienteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var json_paciente: String? = null
    private var param2: String? = null

    private var _binding: FragmentNuevoPacienteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            json_paciente = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_nuevo_paciente, container, false)
        _binding = FragmentNuevoPacienteBinding.inflate(inflater, container, false)
        val view = binding.root

        //val tiposSangre = listOf("Seleccione un tipo de sangre", "A", "B", "AB", "O")
        //val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tiposSangre)
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //binding.spiSangre.adapter = adapter

        binding.spiSangre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0){
                    //Codigo para mostrar
                    //println(parent)
                }
                selectedType = parent?.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se seleccionó ningún valor
            }
        }

        if(json_paciente != null) {
            var gson = Gson()
            var objPaciente = gson.fromJson(json_paciente, Models.Paciente::class.java)

            id_paciente = objPaciente.id
            binding.edtNombre.setText(objPaciente.nombre)
            binding.edtNss.setText(objPaciente.nss)
            //binding.edtTSangre.setText(objPaciente.tipo_sangre)
            binding.edtAlergias.setText(objPaciente.alergias)
            binding.edtTelefono.setText(objPaciente.telefono)
            binding.edtDomicilio.setText(objPaciente.domicilio)
            //for
            /*for (){

            }*/
            //println(binding.spiSangre.selectedItem)
            println(objPaciente.tipo_sangre)
            val arrayStrings = resources.getStringArray(R.array.tipos_sangre)
            var count=0
            for (item in arrayStrings) {
                count=count+1
                if (item==objPaciente.tipo_sangre) {
                    //binding.spiSangre.setSelection(7)
                    count=count-1
                    println("HEllo World "+count.toString())
                    binding.spiSangre.setSelection(count)
                }
            }
            //println(objPaciente)
            //binding.spiSangre.setSelection(1)
        }

        binding.btnGuardar.setOnClickListener{
            guardarDatos()
        }
        binding.btnEliminar.setOnClickListener{
            eliminarDatos()
        }
        return  view
    }

    fun guardarDatos() {
        val client = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()
            .add("id", id_paciente.toString())
            .add("nombre", binding.edtNombre.text.toString())
            .add("nss", binding.edtNss.text.toString())
            .add("tipo_sangre", selectedType)
            .add("alergias", binding.edtAlergias.text.toString())
            .add("telefono", binding.edtTelefono.text.toString())
            .add("domicilio", binding.edtDomicilio.text.toString())
            .build()

        val request = Request.Builder()
            //.url("http://yourip:8000/api/paciente")
            .url("http://"+EnvUrl.UrlVal+":8000/api/paciente")
            .post(formBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(context, "Ocurrio un error: " + e.message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                var respuesta = response.body?.string()
                println(respuesta)
                activity?.runOnUiThread {
                    activity?.onBackPressed()
                }
            }
        })
    }

    fun eliminarDatos(){
        val client = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()
            .add("id", id_paciente.toString())
            .build()

        val request = Request.Builder()
            //.url("http://yourip:8000/api/paciente")
            .url("http://"+EnvUrl.UrlVal+":8000/api/paciente/delete")
            .post(formBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(context, "Ocurrio un error: " + e.message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                var respuesta = response.body?.string()
                println(respuesta)
                activity?.runOnUiThread {
                    activity?.onBackPressed()
                }
            }
        })
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NuevoPacienteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NuevoPacienteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)
                }
            }
    }
}*/