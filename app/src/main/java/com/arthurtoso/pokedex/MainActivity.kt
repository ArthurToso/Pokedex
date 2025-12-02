package com.arthurtoso.pokedex

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.arthurtoso.pokedex.api.ApiClient
import com.arthurtoso.pokedex.api.LoginRequest
import com.arthurtoso.pokedex.api.SessionManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var etLogin: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var pgBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        etLogin = findViewById(R.id.etLogin)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        pgBar = findViewById(R.id.progressBar)

        btnLogin.setOnClickListener {
            cliqueBotao()
        }

    }

    private fun cliqueBotao(){
        //evitar cliques duplos
        btnLogin.isEnabled = false
        val login = etLogin.text.toString().trim()
        val senha = etPassword.text.toString().trim()

        if (login.isEmpty() || senha.isEmpty()) {
            showLoginErrorDialog("Preencha todos os campos")
            btnLogin.isEnabled = true
            return
        }else{
            autenticaUsuario(login, senha)
        }
    }

    private fun autenticaUsuario(login: String, senha: String){
        lifecycleScope.launch {
            try {
                pgBar.visibility = ProgressBar.VISIBLE
                val request = LoginRequest(login, senha)
                val response = ApiClient.instance.login(request)
                if (response.isSuccessful && response.body()?.access_token != null){
                    val body = response.body()!!
                    SessionManager.token = body.access_token
                    SessionManager.usuarioLogado = login
                    redirecionaDashboard()
                }else{
                    showLoginErrorDialog("Login ou Senha incorretos")
                    btnLogin.isEnabled = true
                    pgBar.visibility = ProgressBar.GONE
                }
            }catch (e: Exception){
                Log.e("MainActivity", "Erro ao fazer login", e)
                showLoginErrorDialog("Não foi possível conectar ao servidor, tente novamente")
                btnLogin.isEnabled = true
                pgBar.visibility = ProgressBar.GONE
            }
        }
    }
    //Builder do AlertDialog
    private fun showLoginErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Falha na Autenticação")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun redirecionaDashboard(){
        val intent = Intent(this, ListarActivity::class.java)
        //Passando o login para o dashboard por intent também, além do SessionManager
        intent.putExtra("USER_LOGIN", SessionManager.usuarioLogado)
        startActivity(intent)
        finish()
    }

}