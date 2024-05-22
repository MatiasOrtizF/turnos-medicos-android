package com.mfo.turnosmedicos.ui.scheduleAppointment.searcher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.mfo.turnosmedicos.databinding.ActivitySearcherBinding
import com.mfo.turnosmedicos.ui.scheduleAppointment.appointment.AppointmentActivity
import com.mfo.turnosmedicos.ui.scheduleAppointment.beneficiary.ScheduleAppointmentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearcherActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearcherBinding

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
        val optionsSpeciality = arrayOf("CARDIOLOGÍA", "CLÍNICA MÉDICA", "DERMATOLOGÍA", "DIABETOLOGÍA", "ENVERMERIA",
            "FONOAUDIOLOGÍA", "HEPATOLOGÍA", "INFECTOLOGÍA", "MEDICINA GENERAL", "NEUMONOLOGÍA", "NEUROLOGÍA", "NUTRICIÓN",
            "OFTALMOLOGÍA", "ORTOPEDIA", "TRAUMATOLOGÍA"
        )
        binding.spSpeciality.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionsSpeciality)
    }

    private fun initListeners() {
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, AppointmentActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnPrevious.setOnClickListener {
            goToScheduleAppointment()
        }
        binding.btnGoToScheduleAppointment.setOnClickListener {
            goToScheduleAppointment()
        }
    }

    private fun goToScheduleAppointment() {
        val intent = Intent(this, ScheduleAppointmentActivity::class.java)
        startActivity(intent)
        finish()
    }
}