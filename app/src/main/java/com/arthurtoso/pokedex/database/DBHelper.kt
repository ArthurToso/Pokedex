package com.arthurtoso.pokedex.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        const val DATABASE_NAME = "pokemons.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "pokemon"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME(
                pokemon TEXT PRIMARY KEY,
                tipo TEXT NOT NULL,
                habilidade TEXT NOT NULL,
                nomeUsr TEXT NOT NULL 
            )
            """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, neVersion: Int ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME ")
        onCreate(db)
    }
}