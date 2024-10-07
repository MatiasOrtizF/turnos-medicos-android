package com.mfo.turnosmedicos.ui.scheduleAppointment.appointment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.mfo.turnosmedicos.databinding.ActivityAppointmentBinding
import com.mfo.turnosmedicos.ui.login.LoginActivity
import com.mfo.turnosmedicos.ui.scheduleAppointment.appointment.adapter.AppointmentAdapter
import com.mfo.turnosmedicos.ui.scheduleAppointment.beneficiary.ScheduleAppointmentActivity
import com.mfo.turnosmedicos.ui.scheduleAppointment.confirmation.ConfirmationActivity
import com.mfo.turnosmedicos.ui.scheduleAppointment.searcher.SearcherActivity
import com.mfo.turnosmedicos.utils.ex.getToken
import com.mfo.turnosmedicos.utils.ex.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class AppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppointmentBinding
    private lateinit var appointmentAdapter: AppointmentAdapter
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private var selectedDoctorId: Long? = null
    private var day: String? = null
    private var hour: String? = null
    private var dayNumber: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val doctorId: Long = intent.getLongExtra("doctorId", -1)
        selectedDoctorId = doctorId

        dayNumber = 0
        appointmentViewModel.getAppointmentAvailable(getToken(), doctorId, dayNumber!!)
        initUI()
    }

    private fun initUI() {
        initList()
        initListeners()
        initUIState()
    }

    private fun initList() {
        appointmentAdapter = AppointmentAdapter(
            onItemSelected = {
                if(!binding.btnNext.isVisible) {
                    binding.btnNext.isVisible = true
                }
                hour = it.substring(0,5)
            },
        )
        binding.rvAppointment.apply {
            layoutManager = GridLayoutManager(this@AppointmentActivity, 4)
            adapter = appointmentAdapter
        }
    }

    private fun initListeners() {
        binding.apply {
            btnOne.setOnClickListener { navigateToScheduleAppointment() }
            btnTwo.setOnClickListener { navigateToSearcherActivity() }
            btnPrevious.setOnClickListener { navigateToSearcherActivity() }
            btnNext.setOnClickListener { navigateToConfirmation() }
            btnNextDay.setOnClickListener { nextDay() }
            btnPreviousDay.setOnClickListener {
                println("previous day")
            }
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                appointmentViewModel.state.collect {
                    when(it) {
                        AppointmentState.Loading -> loadingState()
                        is AppointmentState.Error -> errorState(it.error)
                        is AppointmentState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.pbAppointment.isVisible = true
    }

    private fun errorState(error: String) {
        binding.pbAppointment.isVisible = false
        showToast("Error: $error")
        navigateToLogin()
    }

    private fun successState(state: AppointmentState.Success) {
        binding.apply {
            pbAppointment.isVisible = false
            llAppointment.isVisible = true
            navAppointment.isVisible = true
            dayNumber = state.appointmentAvailable.dayNumber

            appointmentAdapter.updateList(state.appointmentAvailable.hour)

            day = state.appointmentAvailable.day
            val formattedDateTime = formatDate(state.appointmentAvailable.day)
            tvDay.text = formattedDateTime
        }
    }

    private fun nextDay() {
        lifecycleScope.launch {
            appointmentViewModel.getAppointmentAvailable(getToken(), selectedDoctorId!!, dayNumber!!)
        }
    }

    private fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE dd 'de' MMMM", Locale("es", "ES"))

        val date = inputFormat.parse(inputDate)

        return outputFormat.format(date).replaceFirstChar { it.uppercase() }
    }

    private fun navigateToScheduleAppointment() {
        val intent = Intent(this, ScheduleAppointmentActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToSearcherActivity() {
        val intent = Intent(this, SearcherActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToConfirmation() {
        val intent = Intent(this, ConfirmationActivity::class.java)
        intent.putExtra("doctorId", selectedDoctorId)
        intent.putExtra("day", day)
        intent.putExtra("hour", hour)
        startActivity(intent)
        finish()
    }
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}