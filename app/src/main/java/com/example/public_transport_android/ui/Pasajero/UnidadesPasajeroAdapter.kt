package com.example.public_transport_android.ui.Pasajero

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.public_transport_android.R
import com.example.public_transport_android.extras.Models
import com.example.public_transport_android.ui.Roles.Parada.UnidadParadaAdapter
import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Este archivo sirve para imprimir el recycler view de donde nos muestra todas las unidades
 * que  estan por pasar en la parada seleccionada
 * **/
class UnidadesPasajeroAdapter (
    private val dataSet:MutableList<Models.Unidad>,
    private val confirmListener: OnConfirmListener
):
    RecyclerView.Adapter<UnidadesPasajeroAdapter.ViewHolder>(){

    interface OnConfirmListener{
        fun onConfirmAction()
    }
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtRutaPasajeroUnidades:TextView
        val txtTiempoEstimado: TextView
        val txtpasaPorBase: TextView

        init {
            txtRutaPasajeroUnidades = view.findViewById(R.id.txtRutaPasajero)
            txtTiempoEstimado = view.findViewById(R.id.txtTiempoEstimado)
            txtpasaPorBase = view.findViewById(R.id.txtpasaPorBase)
        }
    }

    var mRecyclerViewUnidadesPasajero: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerViewUnidadesPasajero = recyclerView
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_unidades_pasajero, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtRutaPasajeroUnidades.text = dataSet[position]?.nombre_ruta
        holder.txtpasaPorBase.text = dataSet[position]?.pasa_por

        val tiempo_estimado = calcularIntervalo(dataSet[position].h_llegada)

        holder.txtTiempoEstimado.text = "${tiempo_estimado} min."
    }

    private fun calcularIntervalo(horaLlegada:String):String{

        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val horaActual =  sdf.format(calendar.time)

        try {

            val dateHoraActual = sdf.parse(horaActual)
            val dateHoraLlegada = sdf.parse(horaLlegada)

            //calcular la diferencia de milisegundos
            val diferencia = dateHoraLlegada.time - dateHoraActual.time

            //Calcular las horas, minutos y  segundos
            val horas = diferencia / 3600000
            val minutos = (diferencia % 3600000) / 60000

            //Formatear el intervalo de tiempo en formato HH:mm
            val intervalo = String.format("%02d:%02d", horas, minutos)

            return intervalo
        }catch (e: ParseException){
            e.printStackTrace()
            return  "Error en el calculo del intervalo"
        }
    }

    override fun getItemCount() = dataSet.size


}