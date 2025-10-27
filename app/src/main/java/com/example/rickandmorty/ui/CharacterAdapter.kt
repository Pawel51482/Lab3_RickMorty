package com.example.rickandmorty.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.data.Character


class CharacterAdapter : RecyclerView.Adapter<CharacterAdapter.VH>() {
    private val items = mutableListOf<Character>()


    fun submit(list: List<Character>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return VH(v)
    }


    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatar: ImageView = itemView.findViewById(R.id.ivItemAvatar)
        private val name: TextView = itemView.findViewById(R.id.tvItemName)
        private val meta: TextView = itemView.findViewById(R.id.tvItemMeta)
        fun bind(model: Character) {
            name.text = model.name ?: "(brak)"
            meta.text = "${model.status ?: "?"} • ${model.species ?: "?"}"
            Glide.with(avatar).load(model.image).into(avatar)
        }
    }
}