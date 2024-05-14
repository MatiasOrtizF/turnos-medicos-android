package com.mfo.turnosmedicos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mfo.turnosmedicos.databinding.ActivityLoginBinding
import com.mfo.turnosmedicos.databinding.ActivityMainBinding
import com.mfo.turnosmedicos.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        //initUIState()
        initUIListeners()
    }

    private fun initUIState() {

    }

    private fun initUIListeners() {
        binding.btnGoToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}