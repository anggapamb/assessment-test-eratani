package com.anggapamb.assessmenttesteratani.ui.wordSearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class WordSearchViewModel @Inject constructor() : ViewModel() {

    private val _singleText = MutableLiveData(
        "My name is Angga Pambudi Setiawan."
    )
    val singleText: LiveData<String> = _singleText

    private val _arrayText = MutableLiveData(
        listOf(
            "Android",
            "Flutter",
            "Kotlin Coroutines",
            "Jetpack Compose",
            "DATA Processing",
            "API Calling"
        )
    )
    val arrayText: LiveData<List<String>> = _arrayText

    private val _result = MutableLiveData("Belum ada pencarian.")
    val result: LiveData<String> = _result

    fun search(query: String, caseSensitive: Boolean) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            _result.value = "Kata kunci kosong."
            return
        }

        val single = _singleText.value.orEmpty()
        val arr = _arrayText.value.orEmpty()

        val foundInSingle = kmpContains(single, trimmed, caseSensitive)
        val matchedItems = arr.filter { kmpContains(it, trimmed, caseSensitive) }

        val mode = if (caseSensitive) "case-sensitive" else "case-insensitive"
        val singleMsg = if (foundInSingle) "• Single String: DITEMUKAN" else "• Single String: TIDAK ditemukan"
        val arrayMsg = if (matchedItems.isEmpty()) {
            "• Array: TIDAK ditemukan di item mana pun."
        } else {
            val list = matchedItems.joinToString(separator = "\n  - ", prefix = "\n  - ")
            "• Array: DITEMUKAN pada:${list}"
        }

        _result.value = "Hasil untuk \"$trimmed\" ($mode):\n$singleMsg\n$arrayMsg"
    }

    private fun kmpContains(text: String, pattern: String, caseSensitive: Boolean): Boolean {
        val t = if (caseSensitive) text else text.lowercase()
        val p = if (caseSensitive) pattern else pattern.lowercase()
        if (p.isEmpty()) return true
        if (p.length > t.length) return false

        val lps = buildLps(p)
        var i = 0
        var j = 0

        while (i < t.length) {
            if (t[i] == p[j]) {
                i++; j++
                if (j == p.length) return true
            } else {
                j = if (j != 0) lps[j - 1] else 0
                if (j == 0) i++
            }
        }
        return false
    }

    private fun buildLps(p: String): IntArray {
        val lps = IntArray(p.length)
        var len = 0
        var i = 1
        while (i < p.length) {
            if (p[i] == p[len]) {
                lps[i++] = ++len
            } else if (len != 0) {
                len = lps[len - 1]
            } else {
                lps[i++] = 0
            }
        }
        return lps
    }
}