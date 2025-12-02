package com.arthurtoso.pokedex

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.arthurtoso.pokedex.api.ApiClient
import com.arthurtoso.pokedex.api.Pokemon
import com.arthurtoso.pokedex.api.SessionManager
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private var user: String? = null
    private lateinit var tvRegisteredPokemons: TextView
    private lateinit var tvTopTypes: TextView
    private lateinit var tvTopAbilities: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnRegisterPokemon: Button
    private lateinit var btnListPokemons: Button
    private lateinit var btnSearchByType: Button
    private lateinit var btnSearchByAbility: Button
    private lateinit var btnExit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        user = intent.getStringExtra("USER_NAME")
        tvRegisteredPokemons = findViewById(R.id.tvRegisteredPokemons)
        tvTopTypes = findViewById(R.id.tvTopTypes)
        tvTopAbilities = findViewById(R.id.tvTopAbilities)
        progressBar = findViewById(R.id.progressBar)

        btnRegisterPokemon = findViewById(R.id.btnRegisterPokemon)
        btnListPokemons = findViewById(R.id.btnListPokemons)
        btnSearchByType = findViewById(R.id.btnSearchByType)
        btnSearchByAbility = findViewById(R.id.btnSearchByAbility)
        btnExit = findViewById(R.id.btnExit)

        setupButtonListeners()
    }

    override fun onResume() {
        super.onResume()
        updateDashboard()
    }

    private fun setupButtonListeners() {
        btnRegisterPokemon.setOnClickListener { 
            val intent = Intent(this, CadastroActivity::class.java)
            intent.putExtra("USER_NAME", SessionManager.usuarioLogado)
            startActivity(intent)
        }
        btnListPokemons.setOnClickListener { 
            startActivity(Intent(this, ListarActivity::class.java))
        }
        btnSearchByType.setOnClickListener { 
            startActivity(Intent(this, PesquisaTipoActivity::class.java))
        }
        btnSearchByAbility.setOnClickListener { 
            startActivity(Intent(this, PesquisaHabilidadeActivity::class.java))
        }
        btnExit.setOnClickListener { 
            finish()
        }
    }

    private fun updateDashboard() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getPokemons()
                if (response.isSuccessful && response.body() != null) {
                    val pokemons = response.body()!!
                    val totalRegistered = pokemons.size
                    tvRegisteredPokemons.text = "Número de Pokémons cadastrados: $totalRegistered"

                    val topTypes = pokemons.groupingBy { it.tipo }
                        .eachCount()
                        .entries
                        .sortedByDescending { it.value }
                        .take(3)
                        .joinToString("\n") { "- ${it.key} (${it.value})" }
                    tvTopTypes.text = "Top 3 tipos: \n$topTypes"

                    val topAbilities = pokemons.flatMap { it.habilidades }
                        .groupingBy { it }
                        .eachCount()
                        .entries
                        .sortedByDescending { it.value }
                        .take(3)
                        .joinToString("\n") { "- ${it.key} (${it.value})" }
                    tvTopAbilities.text = "top 3 habilidades:\n$topAbilities"

                } else {
                    Toast.makeText(this@DashboardActivity, "Erro ao carregar lista: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DashboardActivity, "Erro de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}