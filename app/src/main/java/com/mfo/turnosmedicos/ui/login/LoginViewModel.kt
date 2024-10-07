package com.mfo.turnosmedicos.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.turnosmedicos.data.network.Constants.ERROR_NETWORK_GENERAL
import com.mfo.turnosmedicos.domain.model.LoginRequest
import com.mfo.turnosmedicos.domain.usecase.PostLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val postLoginUseCase: PostLoginUseCase): ViewModel() {

    private var _state = MutableStateFlow<LoginState>(LoginState.Loading)
    val state: StateFlow<LoginState> = _state

    fun authenticationUser(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            if(loginRequest.dni.toString().trim().isNotEmpty() && loginRequest.password.trim().isNotEmpty()) {
                try {
                    val result = withContext(Dispatchers.IO) { postLoginUseCase(loginRequest) }
                    if(result != null) {
                        _state.value = LoginState.Success(result.token)
                    } else {
                        _state.value = LoginState.Error(ERROR_NETWORK_GENERAL)
                    }
                } catch (e: Exception) {
                    val errorMessage: String = e.message.toString()
                    _state.value = LoginState.Error(errorMessage)
                }
            }
        }
    }
}