package com.example.public_transport_android.extras

import java.math.BigInteger

class Models {
    data class RespLogin(
        var idUsr: Int,
        var token: String,
        var nombre: String,
        var error: String,
        var rol: Boolean
    )

    data class Ruta(
        var id: Int,
        var nom_ruta: String
    )

    data class Unidad(
        var id: Int,
        var id_para: BigInteger,
        var id_ruta: BigInteger,
        var num: String,
        var check: Boolean,
        var act_inact: Boolean,
        var h_salida: String,
        var h_llegada: String,
        var pasa_por: String,
        var nota: String,
        var nombre_ruta: String,
        var nombre_parada: String
    )

    data class Parada(
        var id: Int,
        var id_usr: BigInteger,
        var nom_par: String,

        )
    /*data class Paciente(
        var id : Int,
        var nombre: String,
        var nss: String,
        var tipo_sangre: String,
        var alergias: String,
        var telefono: String,
        var domicilio: String,
    )
    data class Doctor(
        var id : Int,
        var nombre: String,
        var cedula: String,
        var especialidad: String,
        var turno: String,
        var telefono: String,
        var email: String,
    )
    data class Enfermedad(
        var id : Int,
        var nombre: String,
        var tipo: String,
        var descripcion: String,
    )
    data class Cita(
        var id : Int,
        var id_enfermedad: String,
        var id_paciente: String,
        var id_doctor: String,
        var consultorio: String,
        var domicilio: String,
        var fecha: String,
        var nombre_enfermedad: String,
        var nombre_paciente: String,
        var nombre_doctor: String,
    )*/
}