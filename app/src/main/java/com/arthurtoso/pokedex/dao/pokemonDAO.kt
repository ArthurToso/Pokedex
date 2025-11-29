package com.arthurtoso.pokedex.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.arthurtoso.pokedex.database.DBHelper
import com.arthurtoso.pokedex.model.pokemons
import kotlin.text.insert

class pokemonDAO(private val context: Context) {
    private val dbHelper = DBHelper(context)

    fun addPokemon(pokemons: pokemons) : Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("pokemon", pokemons.pokemon)
            put("tipo", pokemons.tipo)
            put("habilidades", pokemons.habilidades)
            put("nomeUsr", pokemons.nomeUsr)
        }
        val name = db.insert("pokemon", null, values)
        db.close()
        return name
    }
    fun getAllPokemons(): List<pokemons> {
        val db = dbHelper.readableDatabase
        val cursor : Cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null)
        val pokemons = mutableListOf<pokemons>()
        while (cursor.moveToNext()){
            val pokemon = cursor.getString(cursor.getColumnIndexOrThrow("pokemon"))
        }
        cursor.close()
        db.close()
        return pokemons

    }
}
