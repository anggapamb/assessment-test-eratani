package com.anggapamb.assessmenttesteratani.ui.dataProcessing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anggapamb.assessmenttesteratani.R
import com.anggapamb.assessmenttesteratani.data.model.StockRow

class StockAdapter : ListAdapter<StockRow, StockAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<StockRow>() {
            override fun areItemsTheSame(oldItem: StockRow, newItem: StockRow) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: StockRow, newItem: StockRow) = oldItem == newItem
        }
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val tvName: TextView = view.findViewById(R.id.tvName)
        private val tvCode: TextView = view.findViewById(R.id.tvCode)
        private val tvInitial: TextView = view.findViewById(R.id.tvInitial)
        private val tvSold: TextView = view.findViewById(R.id.tvSold)
        private val tvFinal: TextView = view.findViewById(R.id.tvFinal)

        fun bind(row: StockRow) {
            tvName.text = row.name
            tvCode.text = row.id
            tvInitial.text = row.initial.toString()
            tvSold.text = row.soldInPeriod.toString()
            tvFinal.text = row.finalStock.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_stock_row, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}