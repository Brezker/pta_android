package com.example.public_transport_android.ui.Roles.Base

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.public_transport_android.EnvUrl
import com.example.public_transport_android.R
import com.example.public_transport_android.databinding.FragmentFirstBinding
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

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val root:View  = binding.root


        binding.btnAAdir.setOnClickListener{
            var navController =  findNavController()
            navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("infoUser", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName","")
        val userRole = sharedPreferences.getString("userRole", "")

        val message = "¡Hola , $userName!!"
        binding.txtNameParada.text = message

        binding.btnAAdir.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}