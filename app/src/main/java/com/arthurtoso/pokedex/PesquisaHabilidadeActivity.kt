package com.arthurtoso.pokedex

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar

class PesquisaHabilidadeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HabilidadeAdapter
    private lateinit var listaOriginal: List<String>
    private var listaFiltrada: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pesquisa_habilidade)

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        listaOriginal = emptyList()
        listaFiltrada.addAll(listaOriginal)

        adapter = HabilidadeAdapter(listaFiltrada){ item ->
            //chamaar  api de busca
            Toast.makeText(this, "Clicou em $item", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        // Configurar Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Habilidades de Pokémon"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu.findItem(R.id.searchview)

        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Pesquisar habilidades..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarLista(newText ?: "")
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun filtrarLista(texto: String) {
        listaFiltrada.clear()

        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaOriginal)
        } else {
            val textoLower = texto.lowercase()
            for (item in listaOriginal) {
                if (item.lowercase().contains(textoLower)) {
                    listaFiltrada.add(item)
                }
            }
        }

        adapter.notifyDataSetChanged()
    }

    // Adapter para o RecyclerView
    inner class HabilidadeAdapter(private val habilidades: List<String>, private val onItemClick: (String) -> Unit) :
        RecyclerView.Adapter<HabilidadeAdapter.HabilidadeViewHolder>() {

        inner class HabilidadeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.textView)

            init {
                itemView.setOnClickListener {

                    val posicao = adapterPosition
                    if (posicao != RecyclerView.NO_POSITION) {
                        val habilidadeSelecionada = habilidades[posicao]
                        onItemClick(habilidadeSelecionada)
                        // Retornar resultado ou navegar
                        // finish() // ou setResult()
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabilidadeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item, parent, false)
            return HabilidadeViewHolder(view)
        }

        override fun onBindViewHolder(holder: HabilidadeViewHolder, position: Int) {
            holder.textView.text = habilidades[position]
        }

        override fun getItemCount(): Int = habilidades.size
    }
}