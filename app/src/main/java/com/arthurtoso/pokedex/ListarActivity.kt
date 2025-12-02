package com.arthurtoso.pokedex

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arthurtoso.pokedex.adapter.PokemonAdapter
import com.arthurtoso.pokedex.api.ApiClient
import kotlinx.coroutines.launch

class ListarActivity : AppCompatActivity() {

    private lateinit var adapter: PokemonAdapter
    private lateinit var rvPokemons: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar)
        rvPokemons = findViewById(R.id.rvPokemons)
        progressBar = findViewById(R.id.progressBar)

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        carregarPokemons()
    }

    private fun setupRecyclerView() {
        // Inicializa o adapter com uma lista vazia e a ação de clique
        adapter = PokemonAdapter(emptyList()) { pokemon ->
            // Ação ao clicar: Abre a tela de Detalhes passando o objeto Pokemon
            val intent = Intent(this, DetalhesActivity::class.java)
            intent.putExtra("POKEMON_DATA", pokemon)
            startActivity(intent)
        }

        rvPokemons.layoutManager = LinearLayoutManager(this)
        rvPokemons.adapter = adapter
    }
    private fun carregarPokemons() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getPokemons()
                if (response.isSuccessful && response.body() != null) {
                    val lista = response.body()!!
                    adapter.updateList(lista)
                } else {
                    Toast.makeText(this@ListarActivity, "Erro ao carregar lista: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ListarActivity, "Erro de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}