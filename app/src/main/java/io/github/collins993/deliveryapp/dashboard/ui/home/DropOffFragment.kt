package io.github.collins993.deliveryapp.dashboard.ui.home

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.UserManager
import io.github.collins993.deliveryapp.databinding.DropOffFragmentBinding
import io.github.collins993.deliveryapp.models.DeliveryModel
import io.github.collins993.deliveryapp.models.PickUpModel
import io.github.collins993.deliveryapp.utils.Resource
import io.github.collins993.deliveryapp.viewmodel.MyViewModel
import io.github.collins993.deliveryapp.viewmodel.MyViewModelFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DropOffFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var _binding: DropOffFragmentBinding? = null
    var day = 0
    var month = 0
    var hour = 0
    var minute = 0
    var year = 0

    var savedDay = 0
    var savedMonth = 0
    var savedHour = 0
    var savedMinute = 0
    var savedYear = 0

    var currTime: String = ""
    var scheduledTime: String = ""

    var address: String = ""
    var description: String = ""
    var firstname: String = ""
    var lastname: String = ""
    var phoneNumber: String = ""
    var email: String = ""
    private var pickUpModel: PickUpModel? = null

    private val binding get() = _binding!!
    private val args: DropOffFragmentArgs by navArgs<DropOffFragmentArgs>()
    private lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DropOffFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelProviderFactory
        ).get(MyViewModel::class.java)

        pickUpModel = args.pickUpDetails

        address = pickUpModel?.address.toString()
        Log.i("PickUpAddress", address)
        description = pickUpModel?.description.toString()
        firstname = pickUpModel?.firstName.toString()
        lastname = pickUpModel?.lastName.toString()
        phoneNumber = pickUpModel?.phoneNumber.toString()
        email = pickUpModel?.emailAddress.toString()



        binding.proceedBtn.setOnClickListener {

            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
            val stf = SimpleDateFormat("h:mm a")

            if (validateFields()) {

                val deliveryModel = DeliveryModel(
                    address,
                    description,
                    firstname,
                    lastname,
                    phoneNumber,
                    email,
                    binding.addressLocationTxt.text.toString(),
                    binding.firstName.text.toString(),
                    binding.lastName.text.toString(),
                    binding.phoneNumber.text.toString(),
                    binding.emailAddress.text.toString(),
                    getScheduleOrder(),
                    getDeliveryTime(),
                    currTime,
                    stf.format(date),
                    Firebase.auth.currentUser?.uid.toString()

                )

                viewModel.addOrder(deliveryModel)
                observeAddOrder()
            }

        }
        return root
    }

    override fun onResume() {
        super.onResume()
        val myAddress = args.myAddress
        Log.i("DropOffAddress", myAddress.toString())
        binding.addressLocationTxt.text = myAddress.toString()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentDateTime()


        binding.addressBtn.setOnClickListener {
            val action =
                DropOffFragmentDirections.actionDropOffFragmentToDropOffMapsFragment(args.pickUpDetails)
            findNavController().navigate(action)
        }

        binding.orderRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val checkedRadioButtonId = radioGroup.checkedRadioButtonId
            when (checkedRadioButtonId) {
                R.id.immediately_radio_button -> {
                    Toast.makeText(requireActivity(), "Delivery immediately", Toast.LENGTH_SHORT)
                        .show()
                    binding.dateAndTimeBtn.visibility = View.GONE
                    binding.dateAndTime.visibility = View.GONE
                }
                R.id.schedule_radio_button -> {
                    Toast.makeText(
                        requireActivity(),
                        "Delivery later by my time",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.dateAndTimeBtn.visibility = View.VISIBLE
                    binding.dateAndTime.visibility = View.VISIBLE

                    binding.dateAndTimeBtn.setOnClickListener {
                        setLaterDateAndTime()
                    }
                }
            }
        }
    }

    private fun observeAddOrder() {
        viewModel.addOrderStatus.observe(viewLifecycleOwner, { result ->
            result?.let {
                when (it) {
                    is Resource.Success -> {
                        hideProgressBar()
                        if (it.data.equals("Order Added")) {
                            Toast.makeText(activity, "Order has be placed", Toast.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.action_drop_off_fragment_to_nav_home)

                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        val failedMessage = it.message ?: "Unknown Error"
                        Toast.makeText(
                            activity,
                            "Order failed with $failedMessage",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }

        })
    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
        TimePickerDialog(requireActivity(), this, hour, minute, true).show()
    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute

        binding.dateAndTime.text =
            "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute: $savedMinute"
        scheduledTime = binding.dateAndTime.text as String
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLaterDateAndTime() {
        getDateTimeCalendar()
        DatePickerDialog(requireActivity(), this, year, month, day).show()
    }

    private fun getCurrentDateTime() {
//        val currentTime = Calendar.getInstance().time
//        val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime)
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
        val sdf = SimpleDateFormat("MM-dd-yyyy")
        val stf = SimpleDateFormat("h:mm a")
        val formatedDate = sdf.format(date)

        currTime = formatedDate
        Log.i("CurrentTime", stf.format(date))
//        Log.i("CurrentTime", currentTime.toString())
    }

    private fun validateFields(): Boolean {
        if (binding.addressLocationTxt.text.toString() == "") {
            Toast.makeText(activity, "Select Address", Toast.LENGTH_SHORT).show()
            return false
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

        return true
    }

    private fun getScheduleOrder(): String {
        val checkedRadioButtonId = binding.orderRadioGroup.checkedRadioButtonId

        if (checkedRadioButtonId == R.id.schedule_radio_button) {
            return "Scheduled"
        }
        return "Immediately"
    }

    private fun getDeliveryTime(): String {
        val checkedRadioButtonId = binding.orderRadioGroup.checkedRadioButtonId

        if (checkedRadioButtonId == R.id.schedule_radio_button) {
            return scheduledTime
        }
        return "Immediately"
    }

    private fun hideProgressBar() {
        binding.progressCircular.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressCircular.visibility = View.VISIBLE
    }

}