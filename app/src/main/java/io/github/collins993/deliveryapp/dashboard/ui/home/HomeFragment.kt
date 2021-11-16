package io.github.collins993.deliveryapp.dashboard.ui.home

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.UserManager
import io.github.collins993.deliveryapp.databinding.FragmentHomeBinding
import io.github.collins993.deliveryapp.models.PickUpModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var userManager: UserManager

    val args: HomeFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        userManager = UserManager(requireActivity())
        val root: View = binding.root

        return root
    }

    override fun onResume() {
        super.onResume()
        val myAddress = args.address
        binding.addressLocationTxt.text = myAddress.toString()

    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addressBtn.setOnClickListener {
            //Toast.makeText(requireActivity(), "Open Map", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_nav_home_to_mapsFragment)
        }



        binding.proceedBtn.setOnClickListener {
            if (validateFields()) {

                val address = binding.addressLocationTxt.text.toString()
                val desc = binding.description.text.toString()
                val firstName = binding.firstName.text.toString()
                val lastName = binding.lastName.text.toString()
                val phoneNo = binding.phoneNumber.text.toString()
                val emailAddress = binding.emailAddress.text.toString()

                val pickUpModel = PickUpModel(address,desc,firstName,lastName,phoneNo,emailAddress)


                val action = HomeFragmentDirections.actionNavHomeToDropOffFragment(null, pickUpModel)
                findNavController().navigate(action)
            }

        }

        getCurrentDateTime()


    }

    private fun validateFields(): Boolean {
        if (binding.addressLocationTxt.text.toString() == "") {
            Toast.makeText(activity, "Select Address", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.description.text.toString().trim().isEmpty()) {
            binding.descInputLayout.error = "Required Field!"
            binding.description.requestFocus()
            return false
        } else {
            binding.descInputLayout.isErrorEnabled = false
        }

        if (binding.firstName.text.toString().trim().isEmpty()) {
            binding.firstNameInputLayout.error = "Required Field!"
            binding.firstName.requestFocus()
            return false
        } else {
            binding.firstNameInputLayout.isErrorEnabled = false
        }

        if (binding.lastName.text.toString().trim().isEmpty()) {
            binding.lastNameInputLayout.error = "Required Field!"
            binding.lastName.requestFocus()
            return false
        } else {
            binding.lastNameInputLayout.isErrorEnabled = false
        }

        if (binding.emailAddress.text.toString().trim().isEmpty()) {
            binding.emailInputLayout.error = "Required Field!"
            binding.emailAddress.requestFocus()
            return false
        } else {
            binding.emailInputLayout.isErrorEnabled = false
        }

        if (binding.phoneNumber.text.toString().trim().isEmpty()) {
            binding.phoneNumberInputLayout.error = "Required Field!"
            binding.phoneNumber.requestFocus()
            return false
        } else {
            binding.phoneNumberInputLayout.isErrorEnabled = false
        }

        //


        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCurrentDateTime() {
        val currentTime = Calendar.getInstance().time
        val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime)

        Log.i("CurrentDate", formattedDate)
        Log.i("CurrentTime", currentTime.toString())
    }

    private fun hideProgressBar() {
        //binding.progressCircular.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        //binding.progressCircular.visibility = View.VISIBLE
    }


//


}