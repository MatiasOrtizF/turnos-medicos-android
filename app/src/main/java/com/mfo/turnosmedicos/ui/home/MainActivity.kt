package com.mfo.turnosmedicos.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.mfo.turnosmedicos.databinding.ActivityMainBinding
import com.mfo.turnosmedicos.domain.model.LoginRequest
import com.mfo.turnosmedicos.ui.historyAppointments.HistoryAppointmentsActivity
import com.mfo.turnosmedicos.ui.login.LoginActivity
import com.mfo.turnosmedicos.ui.myAppointments.MyAppointmentsActivity
import com.mfo.turnosmedicos.ui.scheduleAppointment.beneficiary.ScheduleAppointmentActivity
import com.mfo.turnosmedicos.utils.PreferencesHelper
import com.mfo.turnosmedicos.utils.PreferencesHelper.set
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        val preferences = PreferencesHelper.defaultPrefs(this)
        val token = preferences.getString("jwt", "").toString()
        mainViewModel.getUserInfo(token)
    }

    private fun initUI() {
        initUIState()
        initUIListeners()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when(it) {
                    MainState.Loading -> loadingState()
                    is MainState.Error -> errorState(it.error)
                    is MainState.Success -> successState(it)
                }
            }
        }
    }

    private fun initUIListeners() {
        binding.btnScheduleAppointment.setOnClickListener {
            val intent = Intent(this, ScheduleAppointmentActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnMyAppointments.setOnClickListener {
            val intent = Intent(this, MyAppointmentsActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnHistoryAppointments.setOnClickListener {
            val intent = Intent(this, HistoryAppointmentsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadingState() {
        binding.pbMain.isVisible = true
    }

    private fun errorState(error: String) {
        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        if(error == "invalid token") {
            clearSessionPreferences()
            goToLogin()
        }
    }

    private fun successState(state: MainState.Success) {
        println(state.user)
        binding.pbMain.isVisible = false
        binding.llMain.isVisible = true
    }

    private fun clearSessionPreferences() {
        val preferences = PreferencesHelper.defaultPrefs(this)
        preferences["jwt"] = ""
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}