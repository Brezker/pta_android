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
        val txtRutaDestinoBase : TextView
        val txtpasaPorBase : TextView
        val txtNumUnidadBase: TextView

        init {
            txtRutaDestinoBase = view.findViewById(R.id.txtRutaDestinoBase)
            txtpasaPorBase= view.findViewById(R.id.txtpasaPorBase)
            txtNumUnidadBase = view.findViewById(R.id.txtNumUnidadBase)
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
        viewHolder.txtRutaDestinoBase.text = dataSet[position]?.nombre_ruta
        viewHolder.txtpasaPorBase.text = "Pasa Por: ${dataSet[position]?.pasa_por}"
        viewHolder.txtNumUnidadBase.text = "Unidad: ${dataSet[position]?.num}"



    }

    // Devuelve el tamaño del dataset (invocado por el LayoutManager)
    override fun getItemCount() = dataSet.size


}