package com.mfo.turnosmedicos.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.mfo.turnosmedicos.databinding.ActivityMainBinding
import com.mfo.turnosmedicos.ui.historyAppointments.HistoryActivity
import com.mfo.turnosmedicos.ui.login.LoginActivity
import com.mfo.turnosmedicos.ui.myAppointments.MyAppointmentsActivity
import com.mfo.turnosmedicos.ui.scheduleAppointment.beneficiary.ScheduleAppointmentActivity
import com.mfo.turnosmedicos.utils.ex.clearSessionPreferences
import com.mfo.turnosmedicos.utils.ex.getToken
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
    }

    private fun initUI() {
        mainViewModel.getUserInfo(getToken())
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
        binding.apply {
            btnScheduleAppointment.setOnClickListener { navigateToScheduleAppointment() }
            btnMyAppointments.setOnClickListener { navigateToMyAppointments() }
            btnHistoryAppointments.setOnClickListener { navigateToHistory() }
        }
    }

    private fun loadingState() {
        binding.pbMain.isVisible = true
    }

    private fun errorState(error: String) {
        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        if(error == "invalid token") {
            clearSessionPreferences()
            navigateToLogin()
        }
    }

    private fun successState(state: MainState.Success) {
        binding.apply {
            pbMain.isVisible = false
            llMain.isVisible = true
        }
    }

    private fun navigateToScheduleAppointment() {
        val intent = Intent(this, ScheduleAppointmentActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMyAppointments() {
        val intent = Intent(this, MyAppointmentsActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToHistory() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}