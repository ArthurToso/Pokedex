package com.arthurtoso.pokedex.api

import java.io.Serializable

data class Pokemon(
    val id: Int?,
    val nome: String,
    val tipo: String,
    val habilidades: List<String>,
    val dono_login: String
) : Serializable