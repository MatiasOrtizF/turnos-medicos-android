package com.mfo.turnosmedicos.ui.myAppointments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mfo.turnosmedicos.databinding.ActivityMyAppointmentsBinding
import com.mfo.turnosmedicos.ui.login.LoginActivity
import com.mfo.turnosmedicos.ui.myAppointments.adapter.MyAppointmentAdapter
import com.mfo.turnosmedicos.utils.PreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MyAppointmentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyAppointmentsBinding
    private lateinit var myAppointmentAdapter: MyAppointmentAdapter
    private val myAppointmentsViewModel: MyAppointmentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = PreferencesHelper.defaultPrefs(this)
        val token = preferences.getString("jwt", "").toString()

        myAppointmentsViewModel.getAllMyAppointment(token)
        initUI()
    }

    private fun initUI() {
        initList()
        initListeners()
        initUIState()
    }

    private fun initList() {
        myAppointmentAdapter = MyAppointmentAdapter(
            onItemSelected = { id, position ->
                deleteToAppointments(id, position)
            }
        )
        binding.rvAppointment.apply {
            layoutManager = LinearLayoutManager(this@MyAppointmentsActivity)
            adapter = myAppointmentAdapter
        }
    }

    private fun deleteToAppointments(id: Long, position: Int) {
        val context = binding.root.context
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Cancel appointment")
        builder.setMessage("Are you sure you want to cancel this appointment?")

        builder.setPositiveButton("Yes") { _, _ ->
            val preferences = PreferencesHelper.defaultPrefs(this)
            val token = preferences.getString("jwt", "").toString()
            lifecycleScope.launch {
                val isDelete = myAppointmentsViewModel.cancelAppointment(token, id)
                if (isDelete) {
                    withContext(Dispatchers.Main) {
                        myAppointmentAdapter.onDeleteItem(position)
                        //Toast.makeText(this@MyAppointmentsActivity, "Turno cancelado", Toast.LENGTH_SHORT).show()
                        Snackbar.make(binding.root, "Turno cancelado", Snackbar.LENGTH_SHORT).show()
                        if(myAppointmentAdapter.itemCount < 1) {
                            binding.tvNotAppointments.isVisible = true
                            binding.rvAppointment.isVisible = false
                            binding.pbMyAppointment.isVisible = false
                        }
                    }
                }
            }
        }

        builder.setNegativeButton("Cancel", null)

        val dialog = builder.create()
        dialog.show()
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
            myAppointmentAdapter.updateList(state.myAppointment)
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}