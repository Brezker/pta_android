package com.example.public_transport_android

class EnvUrl {
    companion object {
        val UrlVal: String = "publictransportapi-production.up.railway.app"
    }
}

fun main() {
    println(EnvUrl.UrlVal)
}