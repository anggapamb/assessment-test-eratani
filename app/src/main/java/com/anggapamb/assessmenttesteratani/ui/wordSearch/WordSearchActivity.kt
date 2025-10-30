package com.anggapamb.assessmenttesteratani.ui.wordSearch

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.anggapamb.assessmenttesteratani.base.activity.BaseActivity
import com.anggapamb.assessmenttesteratani.databinding.ActivityWordSearchBinding
import com.anggapamb.assessmenttesteratani.utils.applySystemWindowInsetsPadding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordSearchActivity : BaseActivity<ActivityWordSearchBinding, WordSearchViewModel>() {

    override val binding by lazy { ActivityWordSearchBinding.inflate(layoutInflater) }
    override val viewModel: WordSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.main.applySystemWindowInsetsPadding()

        setSupportActionBar(binding.toolbar)

        observe()
        initListener()
    }

    private fun observe() {
        viewModel.singleText.observe(this) { binding.tvSingleText.text = it }
        viewModel.arrayText.observe(this) { list ->
            binding.tvArrayText.text = list.joinToString("\n") { "- $it" }
        }
        viewModel.result.observe(this) { binding.tvResult.text = it }
    }

    private fun triggerSearch() {
        viewModel.search(
            query = binding.etQuery.text?.toString().orEmpty(),
            caseSensitive = binding.swCaseSensitive.isChecked
        )
    }

    private fun initListener() {
        binding.btnSearch.setOnClickListener { triggerSearch() }
        binding.tilQuery.setEndIconOnClickListener { triggerSearch() }

        binding.etQuery.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                triggerSearch()
                true
            } else false
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}

