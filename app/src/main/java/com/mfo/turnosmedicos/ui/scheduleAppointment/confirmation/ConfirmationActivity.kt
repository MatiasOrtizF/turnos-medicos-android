package com.mfo.turnosmedicos.ui.scheduleAppointment.confirmation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mfo.turnosmedicos.databinding.ActivityConfirmationBinding
import com.mfo.turnosmedicos.databinding.ModalConfirmationBinding
import com.mfo.turnosmedicos.domain.model.AppointmentRequest
import com.mfo.turnosmedicos.ui.home.MainActivity
import com.mfo.turnosmedicos.ui.login.LoginActivity
import com.mfo.turnosmedicos.utils.ex.clearSessionPreferences
import com.mfo.turnosmedicos.utils.ex.getToken
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
        confirmationViewModel.getDoctorById(getToken(), id)
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
            navigateToLogin()
        }
    }

    private fun successState(state: ConfirmationState.Success) {
        binding.apply {
            pbConfirmation.isVisible = false
            llConfirmation.isVisible = true

            tvSpeciality.text = state.doctor.speciality
            tvDoctor.text = state.doctor.lastName + " " + state.doctor.name
            tvDay.text = day
            tvHour.text = hour
        }
    }

    private fun sendAppointment() {
        if(doctorId != null && day != null && hour!=null) {
            val doctor = AppointmentRequest.DoctorId(doctorId)
            val appointmentRequest = AppointmentRequest(day, hour, doctor)

            confirmationViewModel.addAppointment(getToken(), appointmentRequest)

            openModal()
        } else {
            Toast.makeText(this, "Error: You might not have selected a date or a doctor.", Toast.LENGTH_SHORT).show()
            navigateToLogin()
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
            navigateToHome()
            dialog.dismiss()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}