package com.mfo.turnosmedicos.ui.scheduleAppointment.beneficiary

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.mfo.turnosmedicos.databinding.ActivityScheduleAppointmentBinding
import com.mfo.turnosmedicos.ui.home.MainState
import com.mfo.turnosmedicos.ui.home.MainViewModel
import com.mfo.turnosmedicos.ui.login.LoginActivity
import com.mfo.turnosmedicos.ui.scheduleAppointment.searcher.SearcherActivity
import com.mfo.turnosmedicos.utils.PreferencesHelper
import com.mfo.turnosmedicos.utils.PreferencesHelper.set
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleAppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleAppointmentBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        val preferences = PreferencesHelper.defaultPrefs(this)
        val token = preferences.getString("jwt", "").toString()
        mainViewModel.getUserInfo(token)
    }

    private fun initUI() {
        initListeners()
        initUIState()
    }

    private fun initListeners() {
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, SearcherActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when(it) {
                    MainState.Loading -> loadingState()
                    is MainState.Error -> errorState(it.error)
                    is MainState.Success -> successState(it)
                }
            }
        }
    }

    private fun loadingState() {
        binding.pbScheduleAppointment.isVisible = true
    }

    private fun errorState(error: String) {
        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        if(error == "invalid token") {
            clearSessionPreferences()
            goToLogin()
        }
    }

    private fun successState(state: MainState.Success) {
        binding.pbScheduleAppointment.isVisible = false
        binding.llScheduleAppointment.isVisible = true
        binding.tvBeneficiary.text = "${state.user.lastName} ${state.user.name}"

        updateTextWithBoldPrefix(binding.tvPhone, state.user.phone.toString())
        updateTextWithBoldPrefix(binding.tvCellphone, state.user.cellphone.toString())
        updateTextWithBoldPrefix(binding.tvEmail, state.user.email)
    }

    private fun updateTextWithBoldPrefix(textView: TextView, suffix: String) {
        val prefix = textView.text.toString()
        val combinedText = "$prefix $suffix"
        val spannableString = SpannableString(combinedText)

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            prefix.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannableString
    }

    private fun clearSessionPreferences() {
        val preferences = PreferencesHelper.defaultPrefs(this)
        preferences["jwt"] = ""
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}