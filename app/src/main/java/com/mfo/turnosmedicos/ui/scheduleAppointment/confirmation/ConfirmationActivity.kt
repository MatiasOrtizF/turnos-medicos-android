package com.mfo.turnosmedicos.ui.scheduleAppointment.confirmation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.mfo.turnosmedicos.databinding.ActivityConfirmationBinding
import com.mfo.turnosmedicos.domain.model.AppointmentRequest
import com.mfo.turnosmedicos.domain.model.Doctor
import com.mfo.turnosmedicos.ui.home.MainActivity
import com.mfo.turnosmedicos.ui.login.LoginActivity
import com.mfo.turnosmedicos.utils.PreferencesHelper
import com.mfo.turnosmedicos.utils.PreferencesHelper.set
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmationBinding
    private val confirmationViewModel: ConfirmationViewModel by viewModels()
    private var day: String? = null
    private var hour: String? = null
    private var doctor: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val doctorId: Long = intent.getLongExtra("doctorId", -1)
        doctor = doctorId
        day = intent.getStringExtra("day") ?: ""
        hour= intent.getStringExtra("hour") ?: ""

        val preferences = PreferencesHelper.defaultPrefs(this)
        val token = preferences.getString("jwt", "").toString()

        confirmationViewModel.getDoctorById(token, doctorId)
        initUI()
    }

    private fun initUI() {
        initUIState()
        initUIListeners()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            confirmationViewModel.state.collect {
                when(it) {
                    ConfirmationState.Loading -> loadingState()
                    is ConfirmationState.Error -> errorState(it.error)
                    is ConfirmationState.Success -> successState(it)
                }
            }
        }
    }

    private fun initUIListeners() {
        binding.btnConfirmAppointment.setOnClickListener {
            if(doctor != null && day != null && hour!=null) {
                val doctor = AppointmentRequest.DoctorId(id = doctor)
                val appointmentRequest = AppointmentRequest(day = day, hour = hour, doctor = doctor)

                val preferences = PreferencesHelper.defaultPrefs(this)
                val token = preferences.getString("jwt", "").toString()

                confirmationViewModel.addAppointment(token, appointmentRequest)

                goToHome()
            } else {
                Toast.makeText(this, "Error: You might not have selected a date or a doctor.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadingState() {
        binding.pbConfirmation.isVisible = true
    }

    private fun errorState(error: String) {
        binding.pbConfirmation.isVisible = false
        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        if(error == "invalid token") {
            clearSessionPreferences()
            goToLogin()
        }
    }

    private fun successState(state: ConfirmationState.Success) {
        println(state.doctor)
        binding.pbConfirmation.isVisible = false
        binding.llConfirmation.isVisible = true

        binding.tvSpeciality.text = state.doctor.speciality
        binding.tvDoctor.text = state.doctor.lastName + " " + state.doctor.name
        binding.tvDay.text = day
        binding.tvHour.text = hour
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

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}