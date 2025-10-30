package com.anggapamb.assessmenttesteratani.ui.dataProcessing

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.anggapamb.assessmenttesteratani.base.activity.BaseActivity
import com.anggapamb.assessmenttesteratani.databinding.ActivityDataProcessingBinding
import com.anggapamb.assessmenttesteratani.ui.dataProcessing.adapter.StockAdapter
import com.anggapamb.assessmenttesteratani.utils.applySystemWindowInsetsPadding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DataProcessingActivity :
    BaseActivity<ActivityDataProcessingBinding, DataProcessingViewModel>() {

    override val binding by lazy { ActivityDataProcessingBinding.inflate(layoutInflater) }
    override val viewModel: DataProcessingViewModel by viewModels()

    private val pickCsvLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri ?: return@registerForActivityResult
        try {
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } catch (_: Exception) { }
        viewModel.importFromCsv(contentResolver, uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.main.applySystemWindowInsetsPadding()

        setupToolbar()
        setupList()
        setupClicks()
        observeUi()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupList() {
        binding.rvStock.adapter = StockAdapter()
    }

    private fun setupClicks() = with(binding) {
        btnImportCsv.setOnClickListener {
            pickCsvLauncher.launch(arrayOf("text/*", "text/csv", "application/csv"))
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setQuery(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeUi() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { s ->
                    binding.progress.isVisible = s.loading

                    binding.tvRangeTitle.text = s.dateRangeLabel

                    val adapter = binding.rvStock.adapter as StockAdapter
                    adapter.submitList(s.visibleRows)

                    binding.emptyView.isVisible = !s.loading && s.visibleRows.isEmpty()

                    binding.tvSummaryProducts.text = s.summary.totalProducts.toString()
                    binding.tvSummaryInitial.text = s.summary.totalInitial.toString()
                    binding.tvSummarySold.text = s.summary.totalSold.toString()
                    binding.tvSummaryFinal.text = s.summary.totalFinal.toString()
                }
            }
        }
    }
}