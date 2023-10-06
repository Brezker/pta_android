package com.example.public_transport_android.extras

class Models {
    data class RespLogin(
        var idUsr:Int,
        var token:String,
        var nombre:String,
        var error:String,
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