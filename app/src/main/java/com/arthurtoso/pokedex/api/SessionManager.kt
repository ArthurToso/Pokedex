package com.arthurtoso.pokedex.api

//Cria objeto SessionManager para evitar passar o token por Intents todas as vezes
object SessionManager {
    var token: String? = null
    var usuarioLogado: String? = null

    // Helper para verificar se está logado
    fun isLogged(): Boolean = !token.isNullOrEmpty()
}