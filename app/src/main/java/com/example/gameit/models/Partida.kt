package com.example.gameit.models

import java.io.Serializable

class Partida(): Serializable {

    var creador: String? = null
    var nombre: String? = null
    var nivel: String? = null
    var apuesta: String? = null
    var finalizada: Boolean? = false

}