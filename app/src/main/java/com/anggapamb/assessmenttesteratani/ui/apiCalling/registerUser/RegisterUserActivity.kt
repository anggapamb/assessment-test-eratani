package com.anggapamb.assessmenttesteratani.ui.apiCalling.registerUser

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.anggapamb.assessmenttesteratani.R
import com.anggapamb.assessmenttesteratani.base.activity.BaseActivity
import com.anggapamb.assessmenttesteratani.databinding.ActivityRegisterUserBinding
import com.anggapamb.assessmenttesteratani.utils.applySystemWindowInsetsPadding
import com.anggapamb.assessmenttesteratani.utils.showTopToast
import com.nuvyz.core.data.model.response.ApiStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterUserActivity : BaseActivity<ActivityRegisterUserBinding, RegisterUserViewModel>() {

    override val binding by lazy { ActivityRegisterUserBinding.inflate(layoutInflater) }
    override val viewModel: RegisterUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.main.applySystemWindowInsetsPadding()

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val genders = listOf("male", "female")
        val statuses = listOf("active", "inactive")

        val genderAdapter = ArrayAdapter(this, R.layout.spinner_field_item, genders).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
        val statusAdapter = ArrayAdapter(this, R.layout.spinner_field_item, statuses).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        binding.spGender.adapter = genderAdapter
        binding.spStatus.adapter = statusAdapter

        binding.spGender.setSelection(0)
        binding.spStatus.setSelection(0)

        binding.btnSubmit.setOnClickListener {
            val name = binding.etName.text?.toString()?.trim().orEmpty()
            val email = binding.etEmail.text?.toString()?.trim().orEmpty()
            val gender = (binding.spGender.selectedItem as? String).orEmpty()
            val status = (binding.spStatus.selectedItem as? String).orEmpty()

            viewModel.createUser(name, email, gender, status)
        }

        observe()
    }

    private fun observe() {
        viewModel.create.observe(this) { resp ->
            dialogLoader.listen(resp.status)

            when (resp.status) {
                ApiStatus.SUCCESS -> {
                    showTopToast("User created: ${resp.data?.name ?: "-"}")
                    setResult(RESULT_OK)
                    finish()
                }
                ApiStatus.ERROR -> {
                    showTopToast(resp.message.ifBlank { getString(R.string.failed_create_user) }, isError = true)
                }
                ApiStatus.LOADING -> Unit
            }
        }
    }
}