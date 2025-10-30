package com.anggapamb.assessmenttesteratani.ui.apiCalling

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.anggapamb.assessmenttesteratani.R
import com.anggapamb.assessmenttesteratani.base.activity.BaseActivity
import com.anggapamb.assessmenttesteratani.data.model.UserDto
import com.anggapamb.assessmenttesteratani.databinding.ActivityApiCallingBinding
import com.anggapamb.assessmenttesteratani.ui.apiCalling.registerUser.RegisterUserActivity
import com.anggapamb.assessmenttesteratani.utils.applySystemWindowInsetsPadding
import com.anggapamb.assessmenttesteratani.utils.showTopToast
import com.nuvyz.core.data.model.response.ApiStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApiCallingActivity : BaseActivity<ActivityApiCallingBinding, ApiCallingViewModel>() {

    override val binding by lazy { ActivityApiCallingBinding.inflate(layoutInflater) }
    override val viewModel: ApiCallingViewModel by viewModels()

    private val addUserLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if (res.resultCode == RESULT_OK) {
            viewModel.loadUsers()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.main.applySystemWindowInsetsPadding()

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.swipe.setOnRefreshListener { viewModel.loadUsers() }
        binding.fabAdd.setOnClickListener {
            addUserLauncher.launch(Intent(this, RegisterUserActivity::class.java))
        }

        observe()
        viewModel.loadUsers()
    }

    private fun observe() {
        viewModel.users.observe(this) { resp ->
            val loading = resp.status == ApiStatus.LOADING
            val success = resp.status == ApiStatus.SUCCESS
            val error = resp.status != ApiStatus.LOADING && !success

            binding.progress.isVisible = loading
            binding.swipe.isRefreshing = false

            if (success) {
                val data = resp.data.orEmpty()
                renderTable(data)
                binding.tvEmpty.isVisible = data.isEmpty()
            }

            if (error) {
                showTopToast(resp.message.ifBlank { getString(R.string.failed_load_users) }, isError = true)
                val data = resp.data.orEmpty()
                renderTable(data)
                binding.tvEmpty.isVisible = data.isEmpty()
            }
        }
    }

    private fun renderTable(items: List<UserDto>) {
        val table = binding.tableUsers
        table.removeAllViews()

        table.addView(buildRow(getString(R.string.lbl_name), getString(R.string.lbl_email), getString(R.string.lbl_gender), isHeader = true))

        items.forEach { u ->
            table.addView(buildRow(u.name, u.email, u.gender, isHeader = false))
        }
    }

    private fun buildRow(
        col1: String,
        col2: String,
        col3: String,
        isHeader: Boolean
    ): TableRow {
        val row = TableRow(this)
        TableRow.LayoutParams(
            0,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            1f
        )
        val pad = resources.getDimensionPixelSize(R.dimen.table_cell_padding)

        fun tv(txt: String, weight: Float, gravity: Int = Gravity.START) = TextView(this).apply {
            layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, weight)
            text = txt
            setPadding(pad, pad, pad, pad)
            this.gravity = gravity
            if (isHeader) {
                setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleSmall)
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            } else {
                setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyMedium)
            }
        }

        row.addView(tv(col1, weight = 3f))
        row.addView(tv(col2, weight = 5f))
        row.addView(tv(col3, weight = 2f, gravity = Gravity.END))

        if (!isHeader) {
            row.setBackgroundColor(ContextCompat.getColor(this, R.color.table_row_bg))
        }
        return row
    }
}