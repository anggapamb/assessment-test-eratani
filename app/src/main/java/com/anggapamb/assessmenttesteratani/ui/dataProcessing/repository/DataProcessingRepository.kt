package com.anggapamb.assessmenttesteratani.ui.dataProcessing.repository

import android.content.ContentResolver
import android.net.Uri
import com.anggapamb.assessmenttesteratani.data.model.DailySale
import com.anggapamb.assessmenttesteratani.data.model.Product
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Locale

class DataProcessingRepository {

    fun importFromCsv(contentResolver: ContentResolver, uri: Uri): Triple<List<Product>, List<DailySale>, Pair<Long, Long>> {
        val productsMap = LinkedHashMap<String, Product>()
        val sales = ArrayList<DailySale>()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        var minMillis: Long = Long.MAX_VALUE
        var maxMillis = 0L

        contentResolver.openInputStream(uri)?.use { ins ->
            BufferedReader(InputStreamReader(ins)).use { br ->
                val header = br.readLine() ?: ""
                val lower = header.lowercase(Locale.ROOT)
                require(listOf("date","product_id","product_name","initial_stock","qty_sold").all { lower.contains(it) }) {
                    "Header CSV tidak sesuai. Harus: date,product_id,product_name,initial_stock,qty_sold"
                }
                var line = br.readLine()
                while (line != null) {
                    val parts = line.split(",")
                    if (parts.size >= 5) {
                        val dateStr = parts[0].trim()
                        val pid = parts[1].trim()
                        val name = parts[2].trim()
                        val init = parts[3].trim().toIntOrNull() ?: 0
                        val qty = parts[4].trim().toIntOrNull() ?: 0

                        val existing = productsMap[pid]
                        if (existing == null) {
                            productsMap[pid] = Product(pid, name, init)
                        } else if (init > existing.initialStock) {
                            productsMap[pid] = existing.copy(initialStock = init)
                        }

                        if (qty > 0) {
                            val millis = runCatching { sdf.parse(dateStr)?.time ?: 0L }.getOrDefault(0L)
                            if (millis > 0L) {
                                sales.add(DailySale(millis, pid, qty))
                                if (millis < minMillis) minMillis = millis
                                if (millis > maxMillis) maxMillis = millis
                            }
                        }
                    }
                    line = br.readLine()
                }
            }
        }

        if (minMillis == Long.MAX_VALUE) minMillis = 0L
        if (maxMillis == 0L) maxMillis = minMillis

        return Triple(productsMap.values.toList(), sales, minMillis to maxMillis)
    }
}