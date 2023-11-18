package com.example.public_transport_android.ui.Pasajero

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

/**
 * Este archivo sirve para imprimir el recycler view de donde nos muestra todas las paradas
 * que  existen en la base de datos
 * **/
class PasajeroUnidadAdapter(private val dataSet: MutableList<Models.Parada>) :
    RecyclerView.Adapter<PasajeroUnidadAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombreParadaPasajero: TextView

        init {
            txtNombreParadaPasajero = view.findViewById(R.id.txtNombreParadaPasajero)
        }
    }

    var mRecyclerViewPasajero: RecyclerView? = null

    // Se llama cuando el adaptador se adjunta a un RecyclerView.
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerViewPasajero = recyclerView
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_pasajero_unidad, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.setOnClickListener {
            var objGson = Gson()
            var json_parada = objGson.toJson(dataSet[position])
            var navController = Navigation.findNavController(it)
            var bundle = bundleOf("json_parada" to json_parada)
            navController.navigate(R.id.action_First3Fragment_to_Second3Fragment, bundle)
        }

        viewHolder.txtNombreParadaPasajero.text = dataSet[position]?.nom_par
    }

    override fun getItemCount() = dataSet.size
}