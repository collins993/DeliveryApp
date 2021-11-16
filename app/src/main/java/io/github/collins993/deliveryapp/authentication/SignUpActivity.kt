package io.github.collins993.deliveryapp.authentication

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.dashboard.DashBoardActivity
import io.github.collins993.deliveryapp.databinding.ActivitySignUpBinding
import io.github.collins993.deliveryapp.utils.Resource
import io.github.collins993.deliveryapp.viewmodel.MyViewModel
import io.github.collins993.deliveryapp.viewmodel.MyViewModelFactory

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var viewModel: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MyViewModel::class.java)

        binding.riderBtn.setOnClickListener {
            startActivity(Intent(this, RiderSignUpActivity::class.java))
            finish()
        }

        binding.registerBtn.setOnClickListener {
            binding.registerBtn.isEnabled = false
            registerUser()

        }

        binding.loginTxt.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    private fun registerUser() {
        if (validateEmail() && validatePassword()){
            val email = binding.emailAddress.text.toString().trim()
            val password = binding.password.text.toString().trim()
            viewModel.signUp(email, password)
            observeRegistration()
        }
        else{
            binding.registerBtn.isEnabled = true
            return
        }



    }

    private fun observeRegistration() {
        viewModel.registrationStatus.observe(this, { result ->
            result?.let {
                when(it){
                    is Resource.Success -> {
                        hideProgressBar()
                        if (it.data.equals("UserCreated")){
                            Toast.makeText(this,"User Successful created",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, DashBoardActivity::class.java))
                            finish()
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        val failedMessage =  it.message ?: "Unknown Error"
                        Toast.makeText(this,"Registration failed with $failedMessage", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }

        })
    }

    private fun validateEmail(): Boolean {
        if (binding.emailAddress.text.toString().trim().isEmpty()) {
            binding.txtInputLayoutEmail.error = "Required Field!"
            binding.emailAddress.requestFocus()
            return false
        }  else {
            binding.txtInputLayoutEmail.isErrorEnabled = false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.password.text.toString().trim().isEmpty()) {
            binding.txtInputLayoutPassword.error = "Required Field!"
            binding.password.requestFocus()
            return false
        } else if (binding.password.text.toString().length < 8) {
            binding.txtInputLayoutPassword.error = "password can't be less than 8"
            binding.password.requestFocus()
            return false
        } else {
            binding.txtInputLayoutPassword.isErrorEnabled = false
        }
        return true
    }

    private fun hideProgressBar() {
        binding.progressCircular.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressCircular.visibility = View.VISIBLE
    }
}