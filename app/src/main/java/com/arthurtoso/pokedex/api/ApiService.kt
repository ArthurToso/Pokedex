package com.arthurtoso.pokedex.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("/token")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    @POST("/pokemons")
    suspend fun addPokemon(@Body request: PokemonRequest): Response<DefaultResponse>
    @GET("/pokemons-list")
    suspend fun getPokemons(): Response<List<Pokemon>>

    @PUT("pokemons/{id}") // Rota para atualizar
    suspend fun updatePokemon(
        @Path("id") id: Int,
        @Body pokemon: PokemonRequest
    ): Response<DefaultResponse>

    @DELETE("pokemons/{id}") // Rota para excluir
    suspend fun deletePokemon(
        @Path("id") id: Int
    ): Response<DefaultResponse>
}