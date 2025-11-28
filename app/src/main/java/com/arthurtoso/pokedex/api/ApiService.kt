package com.arthurtoso.pokedex.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/token")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    @POST("/pokemons")
    suspend fun addPokemon(@Body request: PokemonRequest): Response<DefaultResponse>
}