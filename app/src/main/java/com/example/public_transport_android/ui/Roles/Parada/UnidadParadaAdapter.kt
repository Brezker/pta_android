package com.example.public_transport_android.ui.Roles.Parada

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.public_transport_android.EnvUrl
import com.example.public_transport_android.R
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

class UnidadParadaAdapter(
    private val dataSet: MutableList<Models.Unidad>,
    private val confirmListener: OnConfirmListener
):
RecyclerView.Adapter<UnidadParadaAdapter.ViewHolder>(){

    interface OnConfirmListener{
        fun onConfirmAction()
    }
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        val txtNumUnidad: TextView
        val txtH_Salida: TextView
        val txtH_Llegada: TextView
        val btnCheck : Button
         init {
             txtNumUnidad = view.findViewById(R.id.txtNumUnidadParada)
             txtH_Llegada = view.findViewById(R.id.txtH_LlegadaParada)
             txtH_Salida =  view.findViewById(R.id.txtH_SalidaParada)
             btnCheck =  view.findViewById(R.id.btnCheck)
         }
    }

    var mRecyclerViewParada: RecyclerView? = null

    // Se llama cuando el adaptador se adjunta a un RecyclerView.
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        // Llama al método de la clase base (super) para realizar cualquier trabajo
        // necesario en la implementación base.
        super.onAttachedToRecyclerView(recyclerView!!)

        // Asigna el RecyclerView pasado como argumento a la variable "mRecyclerView" del adaptador.
        mRecyclerViewParada = recyclerView
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_unidad_parada, viewGroup, false)

        return ViewHolder(view)
    }

    // Rellena el contenido de una vista (invocado por el LayoutManager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Configura un click listener en el elemento de la vista para navegar a otra pantalla.
        viewHolder.btnCheck.setOnClickListener{

            val builder = AlertDialog.Builder(viewHolder.itemView.context) // Declarar AlertDialog.Builder
            builder.setTitle("Confirmación")
            builder.setMessage("¿Estás seguro de marcar como pasada esta unidad?")
            // Agregar el botón "Cancelar"
            builder.setNegativeButton("Cancelar") { dialog, which ->
                // Si el usuario hace clic en "Cancelar", no se realizará ninguna acción
                dialog.dismiss()
            }

            builder.setPositiveButton("Confirmar") { dialog, which ->

                var objGson =  Gson()
                var json_unidad = objGson.toJson(dataSet[position])
                val bundle = bundleOf("json_unidad" to json_unidad)

                val  client = OkHttpClient()

                val formBody: RequestBody = FormBody.Builder()
                    .add("id", dataSet[position]?.id.toString())
                    .add("num", dataSet[position]?.num.toString())
                    .add("check", "true")
                    .add("act_inact", "true")
                    .add("h_salida", dataSet[position]?.h_salida.toString())
                    .add("h_llegada", dataSet[position]?.h_llegada.toString())
                    .add("pasa_por", dataSet[position]?.pasa_por.toString())
                    .add("id_para", dataSet[position]?.id_para.toString())
                    .add("id_ruta", dataSet[position]?.id_ruta.toString())
                    .add("nota", "Sin")
                    .build()

                val request = Request.Builder()
                    .url("https://"+EnvUrl.UrlVal+"/api/unidad")
                    .post(formBody)
                    .build()

                client.newCall(request).enqueue(object : Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        Log.i("jackk", e.message.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        var respuesta = response.body?.string()
                        Log.i("jackk",respuesta.toString())


                    }

                })
                confirmListener.onConfirmAction()
                dialog.dismiss()
            }
            builder.show()
        }


        viewHolder.txtNumUnidad.text = "Num. Unidad: ${dataSet[position]?.num}"
        viewHolder.txtH_Salida.text = "Hora de Salida: ${dataSet[position]?.h_salida}"
        viewHolder.txtH_Llegada.text = "Hora de Llegada: ${dataSet[position]?.h_llegada}"
    }

    override fun getItemCount() = dataSet.size


}