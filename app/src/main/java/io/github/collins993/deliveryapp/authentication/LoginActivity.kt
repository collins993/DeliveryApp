package io.github.collins993.deliveryapp.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.collins993.deliveryapp.dashboard.DashBoardActivity
import io.github.collins993.deliveryapp.databinding.ActivityLoginBinding
import io.github.collins993.deliveryapp.databinding.ActivitySignUpBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.registerTxt.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, DashBoardActivity::class.java))
        }
    }
}