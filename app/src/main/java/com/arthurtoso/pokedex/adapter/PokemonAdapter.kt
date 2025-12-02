package com.arthurtoso.pokedex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arthurtoso.pokedex.R
import com.arthurtoso.pokedex.api.Pokemon

class PokemonAdapter(
    private var lista: List<Pokemon>,
    private val onClick: (Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvPokemonName)
        val tvType: TextView = itemView.findViewById(R.id.tvPokemonType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = lista[position]
        holder.tvName.text = pokemon.nome
        holder.tvType.text = "Tipo: ${pokemon.tipo}"

        holder.itemView.setOnClickListener {
            onClick(pokemon)
        }
    }

    override fun getItemCount() = lista.size

    fun updateList(novaLista: List<Pokemon>) {
        lista = novaLista
        notifyDataSetChanged()
    }
}