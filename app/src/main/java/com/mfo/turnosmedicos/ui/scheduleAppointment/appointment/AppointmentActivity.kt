package com.mfo.turnosmedicos.ui.scheduleAppointment.appointment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mfo.turnosmedicos.databinding.ActivityAppointmentBinding
import com.mfo.turnosmedicos.ui.scheduleAppointment.beneficiary.ScheduleAppointmentActivity
import com.mfo.turnosmedicos.ui.scheduleAppointment.searcher.SearcherActivity

class AppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppointmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        initListeners()
        initUIState()
    }

    private fun initListeners() {
        binding.btnOne.setOnClickListener {
            val intent = Intent(this, ScheduleAppointmentActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnTwo.setOnClickListener {
            goToSearcherActivity()
        }
        binding.btnPrevious.setOnClickListener {
            goToSearcherActivity()
        }
        binding.btnNextDay.setOnClickListener {
            println("next day")
        }
        binding.btnPreviousDay.setOnClickListener {
            println("previous day")
        }
    }

    private fun goToSearcherActivity() {
        val intent = Intent(this, SearcherActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun initUIState() {

    }
}