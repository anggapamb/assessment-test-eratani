package com.anggapamb.assessmenttesteratani.ui.apiCalling.registerUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anggapamb.assessmenttesteratani.data.model.UserDto
import com.anggapamb.assessmenttesteratani.data.source.remote.ApiRunner
import com.anggapamb.assessmenttesteratani.data.source.remote.ApiService
import com.nuvyz.core.data.model.response.ApiResponse
import com.nuvyz.core.data.model.response.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _create = MutableLiveData<ApiResponse<UserDto>>()
    val create: LiveData<ApiResponse<UserDto>> = _create

    fun createUser(name: String, email: String, gender: String, status: String) = viewModelScope.launch {
        if (name.isBlank() || email.isBlank() || gender.isBlank() || status.isBlank()) {
            _create.value = ApiResponse(status = ApiStatus.ERROR, message = "All fields are required.")
            return@launch
        }
        _create.value = ApiResponse(status = ApiStatus.LOADING)
        val result = ApiRunner.run { apiService.createUser(name, email, gender, status) }
        _create.value = result
    }
}
