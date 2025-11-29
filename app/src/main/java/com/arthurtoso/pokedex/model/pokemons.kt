package com.arthurtoso.pokedex.model

data class pokemons(
    val pokemon: String,
    val tipo: String,
    val habilidades: String,
    val nomeUsr:String
){
    override fun toString(): String {
        return pokemon
    }
}
