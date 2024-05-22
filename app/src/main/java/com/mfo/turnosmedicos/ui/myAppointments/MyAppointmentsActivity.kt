package com.mfo.turnosmedicos.ui.myAppointments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfo.turnosmedicos.databinding.ActivityMyAppointmentsBinding
import com.mfo.turnosmedicos.ui.login.LoginActivity
import com.mfo.turnosmedicos.ui.myAppointments.adapter.MyAppointmentAdapter
import com.mfo.turnosmedicos.utils.PreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyAppointmentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyAppointmentsBinding
    private lateinit var appointmentAdapter: MyAppointmentAdapter
    private val myAppointmentsViewModel: MyAppointmentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = PreferencesHelper.defaultPrefs(this)
        val token = preferences.getString("jwt", "").toString()

        println("mi token es: $token")

        myAppointmentsViewModel.getAllMyAppointment(token)
        initUI()
    }

    private fun initUI() {
        initList()
        initListeners()
        initUIState()
    }

    private fun initList() {
        appointmentAdapter = MyAppointmentAdapter(
            onItemSelected = {
                println("cancelar el turno: ${it.id}")
            }
        )
        binding.rvAppointment.apply {
            layoutManager = LinearLayoutManager(this@MyAppointmentsActivity)
            adapter = appointmentAdapter
        }
    }

    private fun initListeners() {

    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                myAppointmentsViewModel.state.collect {
                    when(it) {
                        MyAppointmentsState.Loading -> loadingState()
                        is MyAppointmentsState.Error -> errorState(it.error)
                        is MyAppointmentsState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.pbMyAppointment.isVisible = true
    }

    private fun errorState(error: String) {
        binding.pbMyAppointment.isVisible = false
        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        goToLogin()
    }

    private fun successState(state: MyAppointmentsState.Success) {
        binding.pbMyAppointment.isVisible = false
        binding.tvMyAppointments.isVisible = true
        if(state.myAppointment.isEmpty()) {
            binding.tvNotAppointments.isVisible = true
        } else {
            binding.rvAppointment.isVisible = true
            appointmentAdapter.updateList(state.myAppointment)
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}