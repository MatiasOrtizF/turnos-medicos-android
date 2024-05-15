package com.mfo.turnosmedicos.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mfo.turnosmedicos.ui.home.MainActivity
import com.mfo.turnosmedicos.databinding.ActivityLoginBinding
import com.mfo.turnosmedicos.domain.model.LoginRequest
import com.mfo.turnosmedicos.utils.PreferencesHelper
import com.mfo.turnosmedicos.utils.PreferencesHelper.set
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        initUIState()
        initListeners()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.state.collect {
                    when(it) {
                        is LoginState.Error -> errorState(it.error)
                        LoginState.Loading -> {}
                        is LoginState.Success -> successSate(it)
                    }
                }
            }
        }
    }
    private fun initListeners() {
        binding.btnLogin.setOnClickListener {
            val dni = binding.etDni.text.toString().toIntOrNull() ?: 0
            val password = binding.etPassword.text.toString()
            val loginRequest = LoginRequest(dni, password)

            loginViewModel.authenticationUser(loginRequest)
            loadingState()
        }
    }

    private fun loadingState() {
        binding.pbLogin.isVisible = true
        binding.llLogin.isVisible = false
    }

    private fun errorState(error: String) {
        binding.pbLogin.isVisible = false
        binding.llLogin.isVisible = true
        val context = binding.root.context
        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
    }

    private fun successSate(state: LoginState.Success) {
        binding.pbLogin.isVisible = false
        binding.llLogin.isVisible = true
        val jwt: String = state.token
        createSessionPreference(jwt)
        goToHome()
    }

    private fun createSessionPreference(jwt: String) {
        val preferences = PreferencesHelper.defaultPrefs(this)
        preferences["jwt"] = jwt
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}