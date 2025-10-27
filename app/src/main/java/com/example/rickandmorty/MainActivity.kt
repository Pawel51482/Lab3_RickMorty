package com.example.rickandmorty

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.data.Repository
import com.example.rickandmorty.ui.CharacterAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private val repo = Repository()


    private lateinit var rv: RecyclerView
    private lateinit var adapter: CharacterAdapter


    private lateinit var etId: EditText
    private lateinit var btnAll: Button
    private lateinit var btnById: Button


    private lateinit var ivAvatar: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvMeta: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        rv = findViewById(R.id.rvCharacters)
        etId = findViewById(R.id.etCharacterId)
        btnAll = findViewById(R.id.btnLoadAll)
        btnById = findViewById(R.id.btnLoadById)
        ivAvatar = findViewById(R.id.ivAvatar)
        tvName = findViewById(R.id.tvName)
        tvMeta = findViewById(R.id.tvMeta)


        adapter = CharacterAdapter()
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter


        btnAll.setOnClickListener { loadAll() }
        btnById.setOnClickListener { loadById() }
    }


    private fun loadAll() {
        lifecycleScope.launch {
            try {
                val list = withContext(Dispatchers.IO) { repo.getAllCharacters() }
                adapter.submit(list)
// czyść sekcję szczegółów
                tvName.text = ""
                tvMeta.text = ""
                ivAvatar.setImageDrawable(null)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Błąd: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun loadById() {
        val id = etId.text.toString().toIntOrNull()
        if (id == null) {
            Toast.makeText(this, "Podaj poprawne ID", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            try {
                val ch = withContext(Dispatchers.IO) { repo.getCharacterById(id) }
// pokaż w sekcji szczegółów
                tvName.text = ch.name ?: "(brak)"
                tvMeta.text = "${ch.status ?: "?"} • ${ch.species ?: "?"}"
                Glide.with(ivAvatar).load(ch.image).into(ivAvatar)
// wyczyść listę
                adapter.submit(emptyList())
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Błąd: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}