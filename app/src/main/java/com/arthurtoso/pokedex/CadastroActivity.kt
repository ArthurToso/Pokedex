package com.arthurtoso.pokedex

import android.content.Intent
import android.os.Bundle
import android.util.Log.e
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.arthurtoso.pokedex.api.ApiClient
import com.arthurtoso.pokedex.api.PokemonRequest
import kotlinx.coroutines.launch

class CadastroActivity : AppCompatActivity() {

    var userLogin: String = ""
    lateinit var etNome: EditText
    lateinit var etTipo: EditText
    lateinit var etSkill1: EditText
    lateinit var etSkill2: EditText
    lateinit var etSkill3: EditText
    lateinit var pgBar: ProgressBar
    lateinit var btnSalvar: Button
    lateinit var tvTreinador: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)

        userLogin = intent.getStringExtra("USER_LOGIN") ?: "desconhecido"
        etNome = findViewById(R.id.etNome)
        etTipo = findViewById(R.id.etTipo)
        etSkill1 = findViewById(R.id.etSkill1)
        etSkill2 = findViewById(R.id.etSkill2)
        etSkill3 = findViewById(R.id.etSkill3)
        pgBar = findViewById(R.id.progressBar2)
        btnSalvar = findViewById(R.id.btnSalvar)
        pgBar.visibility = ProgressBar.GONE
        tvTreinador = findViewById(R.id.tvTreinador)
        tvTreinador.text = "Treinador: $userLogin"

        btnSalvar.setOnClickListener {
            cadastrarPokemon()
        }
    }

    private fun cadastrarPokemon() {
        btnSalvar.isEnabled = false
        pgBar.visibility = ProgressBar.VISIBLE

        val nome = etNome.text.toString().trim()
        val tipo = etTipo.text.toString().trim()
        val skill1 = etSkill1.text.toString().trim()
        val skill2 = etSkill2.text.toString().trim()
        val skill3 = etSkill3.text.toString().trim()

        if (nome.isEmpty()) {
            showDialog("Erro", "Preencha o nome do Pokémon")
            btnSalvar.isEnabled = true
            pgBar.visibility = ProgressBar.GONE
            return
        }
        if (tipo.isEmpty()) {
            showDialog("Erro", "Preencha o tipo do Pokémon")
            btnSalvar.isEnabled = true
            pgBar.visibility = ProgressBar.GONE
            return
        }
        if (skill1.isEmpty()) {
            showDialog("Erro", "Preencha a primeira habilidade do Pokémon")
            btnSalvar.isEnabled = true
            pgBar.visibility = ProgressBar.GONE
            return
        }

        val habilidades = mutableListOf<String>()
        habilidades.add(skill1)
        if (skill2.isNotEmpty()) {
            habilidades.add(skill2)
        }
        if (skill3.isNotEmpty()) {
            habilidades.add(skill3)
        }

        lifecycleScope.launch{
            try {
                val request = PokemonRequest(
                    nome = nome,
                    tipo = tipo,
                    habilidades = habilidades,
                    usuario = userLogin
                )

                val response = ApiClient.instance.addPokemon(request)

                if (response.isSuccessful) {
                    showDialogSuccess("Sucesso", "Pokemon cadastrado com sucesso!")
                } else {
                    if (response.code() == 400) {
                        showDialog("Erro", "Falha ao cadastrar: Pokemon Já Cadastrado!")
                    }
                    btnSalvar.isEnabled = true
                    pgBar.visibility = ProgressBar.GONE
                }
            }catch (e: Exception){
                showDialog("Erro", "Erro de conexão: ${e.message}")
                btnSalvar.isEnabled = true
                pgBar.visibility = ProgressBar.GONE
            }
        }
    }

    private fun showDialogSuccess(titulo: String, mensagem: String, onPositive: (() -> Unit)? = null) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensagem)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                val intent = Intent(this@CadastroActivity, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
            .create()
            .show()
    }

    private fun showDialog(titulo: String, mensagem: String, onPositive: (() -> Unit)? = null) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensagem)
            .setPositiveButton("OK") { _, _ ->
                onPositive?.invoke()
            }
            .create()
            .show()
    }

}
