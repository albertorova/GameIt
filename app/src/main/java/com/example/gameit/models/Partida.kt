package com.example.gameit.models

import java.io.Serializable

class Partida() : Serializable {

    var portada:String? = null
    var creador: String? = null
    var nombre: String? = null
    var nivel: String? = null
    var apuesta: Int? = null
    var codigo: String? = null
    var isAccepted: Boolean = false
    var isFinished: Boolean? = false
    var isVictory: Boolean? = false
    var id: String? = null

}