package com.mfo.turnosmedicos.ui.historyAppointments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfo.turnosmedicos.databinding.ActivityHistoryBinding
import com.mfo.turnosmedicos.ui.historyAppointments.adapter.HistoryAdapter
import com.mfo.turnosmedicos.ui.login.LoginActivity
import com.mfo.turnosmedicos.utils.ex.getToken
import com.mfo.turnosmedicos.utils.ex.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        historyViewModel.getHistory(getToken())
        initList()
        initUIState()
    }

    private fun initList() {
        historyAdapter = HistoryAdapter()
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = historyAdapter
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                historyViewModel.state.collect {
                    when(it) {
                        HistoryState.Loading -> loadingState()
                        is HistoryState.Error -> errorState(it.error)
                        is HistoryState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.pbHistory.isVisible = true
    }

    private fun errorState(error: String) {
        binding.pbHistory.isVisible = false
        showToast("Error: $error")
        navigateToLogin()
    }

    private fun successState(state: HistoryState.Success ) {
        binding.apply {
            pbHistory.isVisible = false
            tvHistory.isVisible = true
            if(state.appointment.isEmpty()) {
                tvNotHistory.isVisible = true
            } else {
                rvHistory.isVisible = true
                historyAdapter.updateList(state.appointment)
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}