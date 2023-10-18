package com.example.public_transport_android.ui.Roles.Base

import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.public_transport_android.R
import com.example.public_transport_android.extras.Models
import com.google.gson.Gson
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UnidadAdapter (private val dataSet: MutableList<Models.Unidad>):
RecyclerView.Adapter<UnidadAdapter.ViewHolder>(){

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtIdParada : TextView
        val txtIntervalo : TextView
        val txtpasaPor: TextView

        init {
            txtIdParada = view.findViewById(R.id.txtIdParada)
            txtIntervalo= view.findViewById(R.id.txtIntervalo)
            txtpasaPor = view.findViewById(R.id.txtpasaPor)
        }
    }

    var mRecyclerView: RecyclerView? = null


    // Se llama cuando el adaptador se adjunta a un RecyclerView.
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {

        // Llama al método de la clase base (super) para realizar cualquier trabajo
        // necesario en la implementación base.
        super.onAttachedToRecyclerView(recyclerView!!)

        // Asigna el RecyclerView pasado como argumento a la variable "mRecyclerView" del adaptador.
        mRecyclerView = recyclerView
    }


    // Este método se llama para crear nuevas vistas (elementos de la lista) en el RecyclerView.
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        // Se crea una nueva vista inflando el diseño (layout) llamado "adapter_unidad"
        // desde el contexto del ViewGroup proporcionado
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_unidad, viewGroup, false)

        // Se devuelve una instancia de la clase ViewHolder que contiene la vista recién creada.
        return ViewHolder(view)
    }

    // Método para eliminar un elemento de la lista.
    fun removeItem(position: Int) {
        dataSet.removeAt(position)
        notifyItemRemoved(position)
    }

    // Rellena el contenido de una vista (invocado por el LayoutManager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Configura un click listener en el elemento de la vista para navegar a otra pantalla.
        viewHolder.itemView.setOnClickListener{
            var objGson =  Gson()
            var json_unidad = objGson.toJson(dataSet[position])
            var navController = Navigation.findNavController(it)
            val bundle = bundleOf("json_unidad" to json_unidad)
            navController.navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
        }

        // Asigna los valores de los datos del dataset a las vistas en el ViewHolder.
        viewHolder.txtIdParada.text = dataSet[position]?.id_para.toString()
        viewHolder.txtpasaPor.text = "Pasa Por: ${dataSet[position]?.pasa_por}"

        val tiempo_estimado = calcularIntervalo(dataSet[position].h_llegada)

        viewHolder.txtIntervalo.text = "${tiempo_estimado} min."
    }

    // Devuelve el tamaño del dataset (invocado por el LayoutManager)
    override fun getItemCount() = dataSet.size

    private fun calcularIntervalo(horaLlegada:String):String{

        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val horaActual =  sdf.format(calendar.time)

        try {

            val dateHoraActual = sdf.parse(horaActual)
            val dateHoraLlegada = sdf.parse(horaLlegada)

            //calcular la diferencia de milisegundos
//            val diferencia = dateHoraLlegada.time - dateHoraActual.time
            val diferencia = dateHoraActual.time - dateHoraLlegada.time

            //Calcular las horas, minutos y  segundos
            val horas = diferencia / 3600000
            val minutos = (diferencia % 3600000) / 60000

            //Formatear el intervalo de tiempo en formato HH:mm
            val intervalo = String.format("%02d:%02d", horas, minutos)

            return intervalo
        }catch (e:ParseException){
            e.printStackTrace()
            return  "Error en el calculo del intervalo"
        }
 }
}