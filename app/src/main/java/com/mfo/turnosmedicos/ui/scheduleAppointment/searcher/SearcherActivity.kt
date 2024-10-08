package com.mfo.turnosmedicos.ui.scheduleAppointment.searcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mfo.turnosmedicos.data.network.response.DoctorResponse
import com.mfo.turnosmedicos.databinding.ActivitySearcherBinding
import com.mfo.turnosmedicos.ui.login.LoginActivity
import com.mfo.turnosmedicos.ui.scheduleAppointment.appointment.AppointmentActivity
import com.mfo.turnosmedicos.ui.scheduleAppointment.beneficiary.ScheduleAppointmentActivity
import com.mfo.turnosmedicos.utils.PreferencesHelper
import com.mfo.turnosmedicos.utils.ex.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearcherActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearcherBinding
    private val searcherViewModel: SearcherViewModel by viewModels()
    private var selectedDoctorId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearcherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
        initUIState()
    }

    private fun initUIState() {
        val optionsSpeciality = arrayOf("Cardiology", "Internal Medicine", "Dermatology", "Diabetology", "Nursing",
            "Speech Therapy", "Hepatology", "Infectology", "General Medicine", "Pulmonology", "Neurology", "Nutrition",
            "Ophthalmology", "Orthopedics", "Traumatology"
        )

        binding.spSpeciality.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionsSpeciality)

        binding.spSpeciality.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val preferences = PreferencesHelper.defaultPrefs(this@SearcherActivity)
                val token = preferences.getString("jwt", "").toString()

                val selectedSpeciality = optionsSpeciality[position].lowercase()
                searcherViewModel.getDoctorBySpeciality(token, selectedSpeciality)

                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        searcherViewModel.state.collect {
                            when(it) {
                                SearcherState.Loading -> loadingState()
                                is SearcherState.Error -> errorState(it.error)
                                is SearcherState.Success -> successState(it)
                            }
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun loadingState() {
        binding.pbSearcher.isVisible = true
    }

    private fun errorState(error: String) {
        binding.pbSearcher.isVisible = false
        showToast("Error: $error")
        navigateToLogin()
    }

    private fun successState(state: SearcherState.Success) {
        binding.apply {
            pbSearcher.isVisible = false
            llSearcher.isVisible = true
            navSearcher.isVisible = true
        }
        initSpinnerDoctor(state)
    }

    private fun initSpinnerDoctor(state: SearcherState.Success) {
        val doctors = state.doctors.map { DoctorResponse(it.id, it.name, it.lastName, it.speciality) }

        val adapter = object: ArrayAdapter<DoctorResponse>(this, android.R.layout.simple_list_item_1, doctors) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).text =
                    getItem(position)?.name + " " + getItem(position)?.lastName
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).text =  getItem(position)?.name + " " + getItem(position)?.lastName
                return view
            }
        }

        binding.spDoctor.adapter = adapter

        binding.pbSearcher.isVisible = false
        binding.llSearcher.isVisible = true
        binding.navSearcher.isVisible = true

        binding.spDoctor.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                println(doctors[position].id)
                selectedDoctorId = doctors[position].id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun initListeners() {
        binding.btnNext.setOnClickListener {
            if(selectedDoctorId != null) {
                val intent = Intent(this, AppointmentActivity::class.java)
                intent.putExtra("doctorId", selectedDoctorId)
                startActivity(intent)
                finish()
            } else {
                showToast("Please select ad doctor.")
            }
        }
        binding.btnPrevious.setOnClickListener { navigateToScheduleAppointment() }
        binding.btnGoToScheduleAppointment.setOnClickListener { navigateToScheduleAppointment() }
    }

    private fun navigateToScheduleAppointment() {
        val intent = Intent(this, ScheduleAppointmentActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}