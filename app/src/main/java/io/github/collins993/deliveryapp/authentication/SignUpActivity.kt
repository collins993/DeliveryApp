package io.github.collins993.deliveryapp.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.dashboard.DashBoardActivity
import io.github.collins993.deliveryapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this, DashBoardActivity::class.java))
        }

        binding.loginTxt.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}