/*package com.brezker.myapplication.ui.cita

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.brezker.myapplication.EnvUrl
import com.brezker.myapplication.R
import com.brezker.myapplication.databinding.FragmentNuevoCitaBinding
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "json_cita"
private const val ARG_PARAM2 = "param2"
private var id_cita: Int = 0
//private var idDoctor: Int = 0
private var idDoctor: Int? = null
private var idEnfermedad: Int? = null
private var idPaciente: Int? = null

private lateinit var binding: FragmentNuevoCitaBinding

/**
 * A simple [Fragment] subclass.
 * Use the [NuevoCitaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NuevoCitaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var json_cita: String? = null
    private var param2: String? = null
    private lateinit var editTextDateTime: EditText
    private val calendar: Calendar = Calendar.getInstance()

    private var _binding: FragmentNuevoCitaBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            json_cita = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_nuevo_cita, container, false)
        _binding = FragmentNuevoCitaBinding.inflate(inflater, container, false)
        val view = binding.root
        obtenerDoctor()
        obtenerEnfermedad()
        obtenerPaciente()
        editTextDateTime = view.findViewById(R.id.edtFecha)
        editTextDateTime.setOnClickListener {
            showDateTimePicker()
        }
        if(json_cita != null) {
            var gson = Gson()
            var objCita = gson.fromJson(json_cita, Models.Cita::class.java)

            id_cita = objCita.id
            //binding.edtIdenfermedad.setText(objCita.id_enfermedad)
            //binding.edtIdpaciente.setText(objCita.id_paciente)
            //binding.edtIddoctor.setText(objCita.id_doctor)
            binding.edtConsultorio.setText(objCita.consultorio)
            binding.edtDomicilioc.setText(objCita.domicilio)
            binding.edtFecha.setText(objCita.fecha)
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
            .add("id", id_cita.toString())
            //.add("id_enfermedad", binding.edtIdenfermedad.text.toString())
            //.add("id_paciente", binding.edtIdpaciente.text.toString())
            //.add("id_doctor", binding.edtIddoctor.text.toString())
            .add("consultorio", binding.edtConsultorio.text.toString())
            .add("domicilio", binding.edtDomicilioc.text.toString())
            .add("fecha", binding.edtFecha.text.toString())
            .add("id_doctor",idDoctor.toString())
            .add("id_enfermedad", idEnfermedad.toString())
            .add("id_paciente", idPaciente.toString())
            .build()

        val request = Request.Builder()
            //.url("http://yourip:8000/api/paciente")
            .url("http://"+EnvUrl.UrlVal+":8000/api/cita")
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
            .add("id", id_cita.toString())
            .build()

        val request = Request.Builder()
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

    private fun obtenerDoctor() {
        val url = "http://" + EnvUrl.UrlVal + ":8000/api/doctores"

        val request = Request.Builder().url(url).get().build()
        val client = OkHttpClient()
        val objGson = Gson()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Manejar error en caso de fallo
            }

            override fun onResponse(call: Call, response: Response) {
                val respuesta = response.body?.string()

                val listaDoctores = objGson.fromJson(respuesta, Array<Models.Doctor>::class.java)

                if (listaDoctores != null) {
                    var selectedPosition = -1

                    val adapter = object : ArrayAdapter<Models.Doctor>(
                        requireActivity().baseContext,
                        android.R.layout.simple_spinner_item,
                        listaDoctores
                    ) {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = super.getView(position, convertView, parent)
                            val doctor = listaDoctores[position]
                            val displayText = "ID Doc: "+"${doctor.id}"
                            //val displayText = "${doctor.id} ${doctor.nombre}"
                            (view as TextView).text = displayText
                            return view
                        }

                        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = super.getDropDownView(position, convertView, parent)
                            val doctor = listaDoctores[position]
                            //val displayText = "${doctor.id} ${doctor.nombre}"
                            val displayText = "${doctor.nombre}"
                            (view as TextView).text = displayText
                            return view
                        }
                    }

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    activity?.runOnUiThread {
                        binding.spiDoctor.adapter = adapter

                        var gson = Gson()
                        var objCita = gson.fromJson(json_cita, Models.Cita::class.java)
                        //println(json_cita.toString())
                        if (json_cita != null) {
                            for (i in listaDoctores.indices) {
                                if (objCita.id_doctor == listaDoctores[i].id.toString()) {
                                    selectedPosition = i
                                    binding.spiDoctor.setSelection(selectedPosition)
                                    break
                                }
                            }
                        }

                        binding.spiDoctor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                                val selectedDoctor = listaDoctores[position]
                                idDoctor = selectedDoctor.id
                                println(idDoctor)
                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {
                                // Manejar caso de no selección si es necesario
                            }
                        }
                    }
                }
            }
        })
    }

    private fun obtenerEnfermedad() {
        val url = "http://" + EnvUrl.UrlVal + ":8000/api/enfermedades"

        val request = Request.Builder().url(url).get().build()
        val client = OkHttpClient()
        val objGson = Gson()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Manejar error en caso de fallo
            }

            override fun onResponse(call: Call, response: Response) {
                val respuesta = response.body?.string()

                val listaEnfermedades = objGson.fromJson(respuesta, Array<Models.Enfermedad>::class.java)

                if (listaEnfermedades != null) {
                    var selectedPosition = -1

                    val adapter = object : ArrayAdapter<Models.Enfermedad>(
                        requireActivity().baseContext,
                        android.R.layout.simple_spinner_item,
                        listaEnfermedades
                    ) {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = super.getView(position, convertView, parent)
                            val enfermedad = listaEnfermedades[position]
                            val displayText = "ID Enf: "+"${enfermedad.id}"
                            //val displayText = "${enfermedad.id} ${enfermedad.nombre}"
                            (view as TextView).text = displayText
                            return view
                        }

                        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = super.getDropDownView(position, convertView, parent)
                            val enfermedad = listaEnfermedades[position]
                            //val displayText = "${enfermedad.id} ${enfermedad.nombre}"
                            val displayText = "${enfermedad.nombre}"
                            (view as TextView).text = displayText
                            return view
                        }
                    }

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    activity?.runOnUiThread {
                        binding.spiEnfermedad.adapter = adapter

                        var gson = Gson()
                        var objCita = gson.fromJson(json_cita, Models.Cita::class.java)
                        //println(json_cita.toString())
                        if (json_cita != null) {
                            for (i in listaEnfermedades.indices) {
                                if (objCita.id_enfermedad == listaEnfermedades[i].id.toString()) {
                                    selectedPosition = i
                                    binding.spiEnfermedad.setSelection(selectedPosition)
                                    break
                                }
                            }
                        }

                        binding.spiEnfermedad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                                val selectedEnfermedad = listaEnfermedades[position]
                                idEnfermedad = selectedEnfermedad.id
                                println(idEnfermedad)
                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {
                                // Manejar caso de no selección si es necesario
                            }
                        }
                    }
                }
            }
        })
    }

    private fun obtenerPaciente() {
        val url = "http://" + EnvUrl.UrlVal + ":8000/api/pacientes"

        val request = Request.Builder().url(url).get().build()
        val client = OkHttpClient()
        val objGson = Gson()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Manejar error en caso de fallo
            }

            override fun onResponse(call: Call, response: Response) {
                val respuesta = response.body?.string()

                val listaPacientes = objGson.fromJson(respuesta, Array<Models.Paciente>::class.java)

                if (listaPacientes != null) {
                    var selectedPosition = -1

                    val adapter = object : ArrayAdapter<Models.Paciente>(
                        requireActivity().baseContext,
                        android.R.layout.simple_spinner_item,
                        listaPacientes
                    ) {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = super.getView(position, convertView, parent)
                            val paciente = listaPacientes[position]
                            val displayText = "ID Pac: "+"${paciente.id}"
                            //val displayText = "${enfermedad.id} ${enfermedad.nombre}"
                            (view as TextView).text = displayText
                            return view
                        }

                        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = super.getDropDownView(position, convertView, parent)
                            val paciente = listaPacientes[position]
                            //val displayText = "${enfermedad.id} ${enfermedad.nombre}"
                            val displayText = "${paciente.nombre}"
                            (view as TextView).text = displayText
                            return view
                        }
                    }

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    activity?.runOnUiThread {
                        binding.spiPaciente.adapter = adapter

                        var gson = Gson()
                        var objCita = gson.fromJson(json_cita, Models.Cita::class.java)
                        //println(json_cita.toString())
                        if (json_cita != null) {
                            for (i in listaPacientes.indices) {
                                if (objCita.id_paciente == listaPacientes[i].id.toString()) {
                                    selectedPosition = i
                                    binding.spiPaciente.setSelection(selectedPosition)
                                    break
                                }
                            }
                        }

                        binding.spiPaciente.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                                val selectedPaciente = listaPacientes[position]
                                idPaciente = selectedPaciente.id
                                println(idPaciente)
                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {
                                // Manejar caso de no selección si es necesario
                            }
                        }
                    }
                }
            }
        })
    }

    private fun showDateTimePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showTimePicker()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val dateTimeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
                val dateTime = dateTimeFormat.format(calendar.time)
                editTextDateTime.setText(dateTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NuevoCitaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NuevoCitaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}*/