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
import com.mfo.turnosmedicos.utils.ex.getToken
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
        initUI()
    }

    private fun initUI() {
        myAppointmentsViewModel.getAllMyAppointment(getToken())
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
        binding.apply {
            pbMyAppointment.isVisible = true
            tvMyAppointments.isVisible = false
            tvNotAppointments.isVisible = false
            rvAppointment.isVisible = false
        }
    }

    private fun errorState(error: String) {
        binding.pbMyAppointment.isVisible = false
        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        navigateToLogin()
    }

    private fun successState(state: MyAppointmentsState.Success) {
        binding.apply {
            pbMyAppointment.isVisible = false
            tvMyAppointments.isVisible = true
            if(state.myAppointment.isEmpty()) {
                tvNotAppointments.isVisible = true
            } else {
                rvAppointment.isVisible = true
                myAppointmentAdapter.updateList(state.myAppointment)
            }
        }
    }

    private fun cancelSuccess(cancelState: Boolean?) {
        if(cancelState == true) {
            binding.apply {
                pbMyAppointment.isVisible = false
                tvMyAppointments.isVisible = true
                rvAppointment.isVisible = true
            }
            Snackbar.make(binding.root, "Turno cancelado", Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(binding.root, "Failed to cancel appointment", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun cancelAppointment(id: Long, position: Int) {
        lifecycleScope.launch {
            val cancelState = myAppointmentsViewModel.cancelAppointment(getToken(), id)
            cancelSuccess(cancelState ?: false)
            if(cancelState) {
                myAppointmentAdapter.onDeleteItem(position)
                if(myAppointmentAdapter.itemCount < 1) {
                    binding.apply {
                        tvNotAppointments.isVisible = true
                        rvAppointment.isVisible = false
                    }
                }
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}