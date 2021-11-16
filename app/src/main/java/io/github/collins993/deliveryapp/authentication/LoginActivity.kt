package io.github.collins993.deliveryapp.authentication

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import io.github.collins993.deliveryapp.dashboard.DashBoardActivity
import io.github.collins993.deliveryapp.databinding.ActivityLoginBinding
import io.github.collins993.deliveryapp.databinding.ActivitySignUpBinding
import io.github.collins993.deliveryapp.driversdashboard.DriversDashboardActivity
import io.github.collins993.deliveryapp.utils.Resource
import io.github.collins993.deliveryapp.viewmodel.MyViewModel
import io.github.collins993.deliveryapp.viewmodel.MyViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MyViewModel::class.java)

//        val auth = FirebaseAuth.getInstance()
//        if (auth.currentUser != null){
//            //startActivity(Intent(this, DriversDashboardActivity::class.java))
//            startActivity(Intent(this, DashBoardActivity::class.java))
//            finish()
//        }

        binding.riderBtn.setOnClickListener {
            startActivity(Intent(this, RiderLoginActivity::class.java))
            finish()
        }

        binding.registerTxt.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        binding.loginBtn.setOnClickListener {
            binding.loginBtn.isEnabled = false
            loginUser()

        }
    }

    private fun loginUser() {
        if (validateEmail() && validatePassword()){
            val email = binding.emailAddress.text.toString().trim()
            val password = binding.password.text.toString().trim()
            viewModel.signIn(email, password)
            observeLogin()
        }
        else{
            binding.loginBtn.isEnabled = true
            return
        }
    }

    private fun observeLogin() {
        viewModel.signInStatus.observe(this, { result ->
            result?.let {
                when(it){
                    is Resource.Success -> {
                        hideProgressBar()
                        if (it.data.equals("Login Successful")){
                            startActivity(Intent(this, DashBoardActivity::class.java))
                            finish()
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        val failedMessage =  it.message ?: "Unknown Error"
                        Toast.makeText(this,"Login failed with $failedMessage", Toast.LENGTH_LONG).show()
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