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
import com.mfo.turnosmedicos.databinding.DialogCustomBinding
import com.mfo.turnosmedicos.ui.login.LoginActivity
import com.mfo.turnosmedicos.ui.myAppointments.adapter.MyAppointmentAdapter
import com.mfo.turnosmedicos.utils.PreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        val dialogCustomBinding = DialogCustomBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogCustomBinding.root)

        val dialog = builder.create()

        dialogCustomBinding.btnBack.setOnClickListener {
            dialog.dismiss()
        }

        dialogCustomBinding.btnAccept.setOnClickListener {
            cancelAppointment(id, position)
            dialog.dismiss()
        }

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
                        is MyAppointmentsState.CancelSuccess -> cancelSuccess(it.success)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.pbMyAppointment.isVisible = true
        binding.tvMyAppointments.isVisible = false
        binding.tvNotAppointments.isVisible = false
        binding.rvAppointment.isVisible = false
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

    private fun cancelSuccess(cancelState: Boolean?) {
        if(cancelState == true) {
            binding.pbMyAppointment.isVisible = false
            binding.tvMyAppointments.isVisible = true
            binding.rvAppointment.isVisible = true
            Snackbar.make(binding.root, "Turno cancelado", Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(binding.root, "Failed to cancel appointment", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun cancelAppointment(id: Long, position: Int) {
        val preferences = PreferencesHelper.defaultPrefs(this)
        val token = preferences.getString("jwt", "").toString()

        lifecycleScope.launch {
            val cancelState = myAppointmentsViewModel.cancelAppointment(token, id)
            cancelSuccess(cancelState ?: false)
            if(cancelState) {
                myAppointmentAdapter.onDeleteItem(position)
                if(myAppointmentAdapter.itemCount < 1) {
                    binding.tvNotAppointments.isVisible = true
                    binding.rvAppointment.isVisible = false
                }
            }
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}