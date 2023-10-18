package com.example.public_transport_android.ui.Roles.Base

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.public_transport_android.EnvUrl
import com.example.public_transport_android.databinding.FragmentSecondBinding
import com.example.public_transport_android.extras.Models
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException
import java.util.Calendar


// Constantes para argumentos
private const val ARG_PARAM1 = "json_unidad"
private const val ARG_PARAM2 = "param2"

// Variables globales
private var id_unidad: Int = 0
private var idRuta: Int? =  null
private var idPara:Int? =  null
private  lateinit var binding: FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */



class SecondFragment : Fragment() {

    private  var json_unidad:String? =  null
    private var param2:String? = null
    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!

    /**Añadido Prueba**/

    // Método llamado al crear el fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            json_unidad = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    /**Finaliza Prueba**/

    // Método llamado al crear la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar la vista del fragmento
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val view = binding.root

        obtenerRuta()
        obtenerParadas()

        // Verificar si json_unidad no es nulo
        if (json_unidad != null){
            var gson = Gson()
            var objUnidad = gson.fromJson(json_unidad, Models.Unidad::class.java)

            // Asignar el ID de la unidad
            id_unidad = objUnidad.id
            binding.edtNumUnidad.setText(objUnidad.num)
            binding.editTextHoraSalida.setText(objUnidad.h_salida)
            binding.editTextHoraLlegada.setText(objUnidad.h_llegada)
            binding.edtPasaPor.setText(objUnidad.pasa_por)
        }else{
            id_unidad = 0
        }


        binding.editTextHoraSalida.setOnClickListener { showTimePicker(binding.editTextHoraSalida) }
        binding.editTextHoraLlegada.setOnClickListener { showTimePicker(binding.editTextHoraLlegada) }


        binding.btnAddUnidad.setOnClickListener {
            guardarUnidad()
        }

        return view
    }


    /**Comentado por prueba
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        /**binding.buttonSecond.setOnClickListener {
        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }**/
    }
    **/




    private fun showTimePicker(view: EditText) {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                view.setText(selectedTime)
            },
            hour,
            minute,
            true // true para formato de 24 horas, false para AM/PM
        )

        timePickerDialog.show()
    }
    fun guardarUnidad(){

        if(binding.edtNumUnidad.text.toString() == "" ||
            binding.editTextHoraSalida.text.toString() == ""  ||
            binding.editTextHoraLlegada.text.toString() == ""  ||
            binding.edtPasaPor.text.toString() == ""
            ){
            return Toast.makeText(context, "Completa los campos faltantes ", Toast.LENGTH_LONG).show()
        }


        val client =   OkHttpClient()


        val formBody: RequestBody =FormBody.Builder()
            .add("id", id_unidad.toString())
            .add("num", binding.edtNumUnidad.text.toString())
            .add("check", "false")
            .add("act_inact", "true")
            .add("h_salida", binding.editTextHoraSalida.text.toString())
            .add("h_llegada", binding.editTextHoraLlegada.text.toString())
            .add("pasa_por", binding.edtPasaPor.text.toString())
            .add("id_para", idPara.toString())
            .add("id_ruta", idRuta.toString())
            .add("nota", "Sin")
            .build()

        val request =  Request.Builder()
            .url("http://"+EnvUrl.UrlVal+":8000/api/unidad")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(context, "Ocurrio un error: " + e.message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                var respuesta = response.body?.string()
                Log.i("jackk", respuesta.toString())
                activity?.runOnUiThread {
                    activity?.onBackPressed()
                }
            }
        })
    }

    // Función para obtener la lista de rutas
    private fun obtenerRuta(){
        // Se construye la URL para obtener datos de rutas.
        val url = "http://" + EnvUrl.UrlVal + ":8000/api/rutas"

        // Se crea una solicitud HTTP GET utilizando OkHttp.
        val request = Request.Builder().url(url).get().build()
        val client = OkHttpClient()
        val objGson = Gson()

        // Se envía la solicitud HTTP asíncronamente y se define cómo manejar las respuestas y
        // los errores.
        client.newCall(request).enqueue(object : Callback{
            // Este método se ejecuta si la solicitud HTTP falla.
            override fun onFailure(call: Call, e: IOException) {
                Log.i("jackk",e.message.toString())
            }

            // Este método se ejecuta cuando se recibe una respuesta exitosa de la solicitud HTTP.
            override fun onResponse(call: Call, response: Response) {
                // Se obtiene la respuesta del servidor como una cadena de texto.
                val respuesta = response.body?.string()

                // Se convierte la respuesta JSON en un array de objetos de tipo
                // Models.Enfermedad utilizando Gson.
                val listaUnidades = objGson.fromJson(respuesta, Array<Models.Ruta>::class.java)

                if (listaUnidades != null){
                    var selectedPosition = -1

                    // Se crea un adaptador personalizado para el Spinner que muestra los datos de enfermedades.
                    val adapter = object : ArrayAdapter<Models.Ruta>(
                        requireActivity().baseContext,
                        android.R.layout.simple_spinner_item,
                        listaUnidades
                    ){

                        // Este método se utiliza para personalizar la vista de los elementos
                        // seleccionados en el Spinner.
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                            val view = super.getView(position, convertView, parent)
                            val ruta = listaUnidades[position]
                            val displayText =  "ID Ruta: ${ruta.id} : ${ruta.nom_ruta}"
                            (view as TextView).text = displayText
                            return view
                        }

                        // Este método se utiliza para personalizar la vista de los elementos
                        // en la lista desplegable del Spinner.
                        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = super.getDropDownView(position, convertView, parent)
                            val ruta = listaUnidades[position]
                            val displayText = ruta.nom_ruta
                            (view as TextView).text = displayText
                            return view
                        }
                    }

                    // Se establece el diseño de la lista desplegable del Spinner.
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    // Se actualiza el Spinner en la interfaz de usuario en el hilo principal.
                    activity?.runOnUiThread {
                        binding.spinnerRuta.adapter =  adapter

                        // Se obtiene un objeto Unidad a partir de un JSON (json_cita).
                        var gson =  Gson()
                        var objUnidad = gson.fromJson(json_unidad, Models.Unidad::class.java)

                        // Si se ha obtenido un objeto Cita, se busca la posición de la unidad
                        // correspondiente en el Spinner.
                        if (json_unidad != null){
                            for (i in listaUnidades.indices){
                                if (objUnidad.id_ruta.toString() == listaUnidades[i].id.toString()){
                                    selectedPosition = i
                                    binding.spinnerRuta.setSelection(selectedPosition)
                                    break
                                }
                            }
                        }

                        // Se establece un listener para el Spinner que se ejecuta cuando se selecciona
                        // un elemento.
                        binding.spinnerRuta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(
                                adapterView: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long) {
                                // Cuando se selecciona un elemento, se obtiene el ID de la unidad seleccionada.
                                val selectedRuta = listaUnidades[position]
                                idRuta = selectedRuta.id
                            }
                            override fun onNothingSelected(adapterView: AdapterView<*>) {

                            }
                        }
                    }

                }
            }
        })
    }

    private fun obtenerParadas(){
        // Se construye la URL para obtener datos de paradas.
        val url = "http://" + EnvUrl.UrlVal + ":8000/api/paradas"

        // Se crea una solicitud HTTP GET utilizando OkHttp.
        val request = Request.Builder().url(url).get().build()
        val client = OkHttpClient()
        val objGson = Gson()

        // Se envía la solicitud HTTP asíncronamente y se define cómo manejar las respuestas y los errores.
        client.newCall(request).enqueue(object : Callback {
            // Este método se ejecuta si la solicitud HTTP falla.
            override fun onFailure(call: Call, e: IOException) {
                // Aquí deberías manejar el error en caso de que la solicitud falle.
            }

            // Este método se ejecuta cuando se recibe una respuesta exitosa de la solicitud HTTP.
            override fun onResponse(call: Call, response: Response) {
                // Se obtiene la respuesta del servidor como una cadena de texto.
                val respuesta = response.body?.string()

                // Se convierte la respuesta JSON en un array de objetos de tipo Models.Parada
                // utilizando Gson.
                val listaParadas = objGson.fromJson(respuesta, Array<Models.Parada>::class.java)

                if (listaParadas != null){
                    var selectedPosition = -1

                // Se crea un adaptador personalizado para el Spinner que muestra los datos de
                // paradas.
                    val adapter = object:ArrayAdapter<Models.Parada>(
                        requireActivity().baseContext,
                        android.R.layout.simple_spinner_item,
                        listaParadas
                    ){
                        // Este método se utiliza para personalizar la vista de los elementos
                        // seleccionados en el Spinner.
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = super.getView(position, convertView, parent)
                            val parada = listaParadas[position]
                            val displayText = "ID Para. ${parada.id} : ${parada.nom_par}"
                            (view as TextView).text =  displayText
                            return view
                        }

                        // Este método se utiliza para personalizar la vista de los elementos en
                        // la lista desplegable del Spinner.
                        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = super.getDropDownView(position, convertView, parent)
                            val parada =  listaParadas[position]
                            val displayText = "${parada.nom_par}"
                            (view as TextView).text =  displayText
                            return view
                        }
                    }

                    // Se establece el diseño de la lista desplegable del Spinner.
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    // Se actualiza el Spinner en la interfaz de usuario en el hilo principal.
                    activity?.runOnUiThread {
                        binding.spinnerParada.adapter = adapter

                        // Se obtiene un objeto Cita a partir de un JSON (json_unidad).
                        var gson =  Gson()
                        var  objUnidad =gson.fromJson(json_unidad, Models.Unidad::class.java)

                        // Si se ha obtenido un objeto Cita, se busca la posición de la
                        // Unidad correspondiente en el Spinner.
                        if (json_unidad != null){
                            for (i in listaParadas.indices){
                                if (objUnidad.id_para.toString() ==  listaParadas[i].id.toString()){
                                    selectedPosition = i
                                    binding.spinnerParada.setSelection(selectedPosition)
                                    break
                                }
                            }
                        }

                        // Se establece un listener para el Spinner que se ejecuta cuando se
                        // selecciona un elemento.
                        binding.spinnerParada.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                                val selectedParada = listaParadas[position]
                                idPara =  selectedParada.id
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    

}