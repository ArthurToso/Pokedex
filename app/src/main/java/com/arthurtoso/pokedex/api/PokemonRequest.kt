package com.arthurtoso.pokedex.api

data class PokemonRequest(
    val nome: String,
    val tipo: String,
    val habilidades: List<String>,
    val usuario: String
)
