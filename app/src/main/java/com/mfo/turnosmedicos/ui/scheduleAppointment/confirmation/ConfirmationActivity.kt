package com.mfo.turnosmedicos.ui.scheduleAppointment.confirmation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mfo.turnosmedicos.databinding.ActivityConfirmationBinding
import com.mfo.turnosmedicos.databinding.ModalConfirmationBinding
import com.mfo.turnosmedicos.domain.model.AppointmentRequest
import com.mfo.turnosmedicos.domain.model.LoginRequest
import com.mfo.turnosmedicos.ui.home.MainActivity
import com.mfo.turnosmedicos.ui.login.LoginActivity
import com.mfo.turnosmedicos.utils.PreferencesHelper
import com.mfo.turnosmedicos.utils.PreferencesHelper.set
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class ConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmationBinding
    private val confirmationViewModel: ConfirmationViewModel by viewModels()
    private var day: String? = null
    private var hour: String? = null
    private var doctorId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id: Long = intent.getLongExtra("doctorId", -1)
        doctorId = id
        day = intent.getStringExtra("day") ?: ""
        hour= intent.getStringExtra("hour") ?: ""

        val preferences = PreferencesHelper.defaultPrefs(this)
        val token = preferences.getString("jwt", "").toString()

        confirmationViewModel.getDoctorById(token, id)
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
        binding.apply {
            btnConfirmAppointment.setOnClickListener {
                sendAppointment()
            }
            binding.btnPrevious.setOnClickListener { onBackPressed() }
        }
    }

    private fun loadingState() {
        binding.pbConfirmation.isVisible = true
    }

    private fun errorState(error: String) {
        binding.pbConfirmation.isVisible = false
        println(error)
        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        if(error == "Unauthorized: invalid token") {
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

    private fun sendAppointment() {
        if(doctorId != null && day != null && hour!=null) {
            val doctor = AppointmentRequest.DoctorId(doctorId)
            val appointmentRequest = AppointmentRequest(day, hour, doctor)

            val preferences = PreferencesHelper.defaultPrefs(this)
            val token = preferences.getString("jwt", "").toString()

            confirmationViewModel.addAppointment(token, appointmentRequest)

            openModal()
        } else {
            Toast.makeText(this, "Error: You might not have selected a date or a doctor.", Toast.LENGTH_SHORT).show()
            goToLogin()
        }
    }

    private fun openModal() {
        val binding = ModalConfirmationBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(binding.root)

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        dialog.show()

        binding.btnAccept.setOnClickListener {
            goToHome()
            dialog.dismiss()
        }
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