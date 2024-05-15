package com.mfo.turnosmedicos.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.turnosmedicos.domain.usecase.GetUserInfoUseCase
import com.mfo.turnosmedicos.ui.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getUserInfoUseCase: GetUserInfoUseCase): ViewModel() {
    private var _state = MutableStateFlow<MainState>(MainState.Loading)
    val state: StateFlow<MainState> = _state

    fun getUserInfo(token: String) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getUserInfoUseCase(token) }
                if(result != null) {
                    _state.value = MainState.Success(result)
                } else {
                    _state.value = MainState.Error("ocurrio un error, por favor intente mas tarde")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = MainState.Error(errorMessage)
            }
        }
    }
}