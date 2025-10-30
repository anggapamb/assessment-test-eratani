package com.anggapamb.assessmenttesteratani.ui.dataProcessing

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anggapamb.assessmenttesteratani.data.model.*
import com.anggapamb.assessmenttesteratani.ui.dataProcessing.repository.DataProcessingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DataProcessingViewModel @Inject constructor() : ViewModel() {

    private val repo = DataProcessingRepository()

    private val _uiState = MutableStateFlow(DataProcessingUiState())
    val uiState: StateFlow<DataProcessingUiState> = _uiState

    fun importFromCsv(contentResolver: ContentResolver, uri: Uri) {
        viewModelScope.launch {
            setLoading(true)

            val (products, sales, rangePair) = repo.importFromCsv(contentResolver, uri)
            val soldByProduct = sales.groupBy { it.productId }.mapValues { (_, list) -> list.sumOf { it.qty } }

            val rows = products.map { p ->
                val sold = soldByProduct[p.id] ?: 0
                val finalStock = (p.initialStock - sold).coerceAtLeast(0)
                StockRow(
                    id = p.id,
                    name = p.name,
                    initial = p.initialStock,
                    soldInPeriod = sold,
                    finalStock = finalStock
                )
            }

            val rangeLabel = formatRange(rangePair.first, rangePair.second)
            _uiState.value = _uiState.value.copy(
                imported = true,
                dateRangeLabel = rangeLabel,
                allRows = rows,
                query = ""
            )

            applyFilterOnly()
            setLoading(false)
        }
    }

    fun setQuery(q: String) {
        _uiState.value = _uiState.value.copy(query = q)
        applyFilterOnly()
    }

    private fun applyFilterOnly() {
        val state = _uiState.value
        val queryLower = state.query.trim().lowercase()
        var list = state.allRows

        if (queryLower.isNotEmpty()) {
            list = list.filter {
                it.name.lowercase().contains(queryLower) || it.id.lowercase().contains(queryLower)
            }
        }

        val summary = MonthlySummary(
            totalProducts = list.size,
            totalInitial = list.sumOf { it.initial },
            totalSold = list.sumOf { it.soldInPeriod },
            totalFinal = list.sumOf { it.finalStock }
        )

        _uiState.value = _uiState.value.copy(
            visibleRows = list,
            summary = summary
        )
    }

    private fun setLoading(v: Boolean) {
        _uiState.value = _uiState.value.copy(loading = v)
    }
}