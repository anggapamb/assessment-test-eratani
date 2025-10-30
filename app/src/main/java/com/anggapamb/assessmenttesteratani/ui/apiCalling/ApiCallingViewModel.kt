package com.anggapamb.assessmenttesteratani.ui.apiCalling

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
class ApiCallingViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _users = MutableLiveData<ApiResponse<List<UserDto>>>()
    val users: LiveData<ApiResponse<List<UserDto>>> = _users

    fun loadUsers() = viewModelScope.launch {
        _users.value = ApiResponse(status = ApiStatus.LOADING)
        val result = ApiRunner.run { apiService.getUsers() }
        _users.value = result
    }
}
