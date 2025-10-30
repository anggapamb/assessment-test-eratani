package com.anggapamb.assessmenttesteratani.data.model

import java.text.SimpleDateFormat
import java.util.Locale

data class Product(
    val id: String,
    val name: String,
    val initialStock: Int
)

data class DailySale(
    val dateMillis: Long,
    val productId: String,
    val qty: Int
)

data class StockRow(
    val id: String,
    val name: String,
    val initial: Int,
    val soldInPeriod: Int,
    val finalStock: Int
)

data class MonthlySummary(
    val totalProducts: Int = 0,
    val totalInitial: Int = 0,
    val totalSold: Int = 0,
    val totalFinal: Int = 0
)

data class DataProcessingUiState(
    val imported: Boolean = false,
    val dateRangeLabel: String = "Silakan impor CSV",
    val allRows: List<StockRow> = emptyList(),
    val visibleRows: List<StockRow> = emptyList(),
    val summary: MonthlySummary = MonthlySummary(),
    val query: String = "",
    val loading: Boolean = false
)

internal fun formatDate(millis: Long, locale: Locale = Locale("id", "ID")): String {
    return SimpleDateFormat("d MMMM yyyy", locale).format(java.util.Date(millis))
}

internal fun formatRange(minMillis: Long, maxMillis: Long, locale: Locale = Locale("id", "ID")): String {
    return if (minMillis == maxMillis) {
        "Data Penjualan tanggal ${formatDate(minMillis, locale)}"
    } else {
        "Data Penjualan dari ${formatDate(minMillis, locale)} - ${formatDate(maxMillis, locale)}"
    }
}