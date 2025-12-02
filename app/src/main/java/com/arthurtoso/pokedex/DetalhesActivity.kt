package com.arthurtoso.pokedex

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.arthurtoso.pokedex.api.ApiClient
import com.arthurtoso.pokedex.api.Pokemon
import com.arthurtoso.pokedex.api.PokemonRequest
import kotlinx.coroutines.launch

class DetalhesActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etTipo: EditText
    private lateinit var etSkill1: EditText
    private lateinit var etSkill2: EditText
    private lateinit var etSkill3: EditText
    private lateinit var etUsuario: EditText
    private lateinit var btnAtualizar: Button
    private lateinit var btnExcluir: Button
    private lateinit var pgBar: ProgressBar

    private var pokemonAtual: Pokemon? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalhes)

        etNome = findViewById(R.id.etDetNome)
        etTipo = findViewById(R.id.etDetTipo)
        etSkill1 = findViewById(R.id.etDetSkill1)
        etSkill2 = findViewById(R.id.etDetSkill2)
        etSkill3 = findViewById(R.id.etDetSkill3)
        etUsuario = findViewById(R.id.etDetUsuario)
        btnAtualizar = findViewById(R.id.btnAtualizar)
        btnExcluir = findViewById(R.id.btnExcluir)
        pgBar = findViewById(R.id.pgBar)
        pgBar.visibility = ProgressBar.GONE

        pokemonAtual = intent.getSerializableExtra("POKEMON_DATA") as? Pokemon

        if (pokemonAtual != null) {
            preencherCampos(pokemonAtual!!)
        } else {
            Toast.makeText(this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnAtualizar.setOnClickListener { salvarAlteracoes() }
        btnExcluir.setOnClickListener { confirmarExclusao() }
    }

    private fun preencherCampos(p: Pokemon) {
        etNome.setText(p.nome)
        etTipo.setText(p.tipo)
        etUsuario.setText(p.dono_login)

        if (p.habilidades.isNotEmpty()) etSkill1.setText(p.habilidades[0])
        if (p.habilidades.size > 1) etSkill2.setText(p.habilidades[1])
        if (p.habilidades.size > 2) etSkill3.setText(p.habilidades[2])
    }

    private fun salvarAlteracoes() {
        pgBar.visibility = ProgressBar.VISIBLE

        val novoNome = etNome.text.toString()
        val novoTipo = etTipo.text.toString()

        val skills = mutableListOf<String>()
        if (etSkill1.text.isNotEmpty()) skills.add(etSkill1.text.toString())
        if (etSkill2.text.isNotEmpty()) skills.add(etSkill2.text.toString())
        if (etSkill3.text.isNotEmpty()) skills.add(etSkill3.text.toString())

        val request = PokemonRequest(
            nome = novoNome,
            tipo = novoTipo,
            habilidades = skills,
            dono_login = pokemonAtual!!.dono_login
        )

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.updatePokemon(pokemonAtual!!.id!!, request)
                if (response.isSuccessful) {
                    Toast.makeText(this@DetalhesActivity, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@DetalhesActivity, "Erro ao atualizar", Toast.LENGTH_SHORT).show()
                    pgBar.visibility = ProgressBar.GONE
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                pgBar.visibility = ProgressBar.GONE
            }
        }
    }

    private fun confirmarExclusao() {
        AlertDialog.Builder(this)
            .setTitle("Excluir")
            .setMessage("Tem certeza que deseja excluir este Pokémon?")
            .setPositiveButton("Sim") { _, _ ->
                deletarPokemon()
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun deletarPokemon() {
        pgBar.visibility = ProgressBar.VISIBLE
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.deletePokemon(pokemonAtual!!.id!!)
                if (response.isSuccessful) {
                    Toast.makeText(this@DetalhesActivity, "Excluído!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@DetalhesActivity, "Erro ao excluir", Toast.LENGTH_SHORT).show()
                    pgBar.visibility = ProgressBar.GONE
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetalhesActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                pgBar.visibility = ProgressBar.GONE
            }
        }
    }

}