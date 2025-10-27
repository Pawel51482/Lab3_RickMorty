package com.example.rickandmorty.data


class Repository(private val api: RickAndMortyService = ApiClient.service) {
    /** Pobiera WSZYSTKIE strony (kilkaset postaci) */
    suspend fun getAllCharacters(): List<Character> {
        val all = mutableListOf<Character>()
        var page: Int? = 1
        while (true) {
            val resp = api.getCharacters(page)
            val results = resp.results.orEmpty()
            all += results
            val next = resp.info?.next
            if (next.isNullOrEmpty()) break
// next ma format ...?page=N — wyciągamy N
            page = next.substringAfter("page=").toIntOrNull()
            if (page == null) break
        }
        return all
    }


    suspend fun getCharacterById(id: Int): Character = api.getCharacterById(id)
}