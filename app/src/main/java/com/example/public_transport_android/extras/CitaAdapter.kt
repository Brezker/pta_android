/*package com.example.public_transport_android.extras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.brezker.myapplication.R
import com.google.gson.Gson

class CitaAdapter (private val dataSet: MutableList<Models.Cita>) :
    RecyclerView.Adapter<CitaAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtIdenfermedad: TextView
        val txtIdpaciente: TextView

        init {
            // Define click listener for the ViewHolder's View.
            txtIdenfermedad = view.findViewById(R.id.txtIdenfermedad)
            txtIdpaciente = view.findViewById(R.id.txtIdpaciente)
        }
    }

    var mRecyclerView: RecyclerView? = null


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView!!)
        mRecyclerView = recyclerView
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_item_cita, viewGroup, false)

        return ViewHolder(view)
    }

    fun removeItem(position: Int) {
        dataSet.removeAt(position)
        notifyItemRemoved(position)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.itemView.setOnClickListener {
            var objGson = Gson()
            var json_cita = objGson.toJson(dataSet[position])
            var navController = Navigation.findNavController(it)
            val bundle = bundleOf("json_cita" to json_cita)
            navController.navigate(R.id.nav_nuevo_cita, bundle)
        }

        viewHolder.txtIdenfermedad.text = dataSet[position]?.nombre_enfermedad
        viewHolder.txtIdpaciente.text = dataSet[position]?.nombre_paciente

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}*/