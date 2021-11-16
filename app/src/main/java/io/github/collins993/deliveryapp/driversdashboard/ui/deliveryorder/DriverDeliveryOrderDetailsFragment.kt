package io.github.collins993.deliveryapp.driversdashboard.ui.deliveryorder

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.adapter.MyAdapter
import io.github.collins993.deliveryapp.dashboard.ui.orderhistory.OrderDetailsFragmentArgs
import io.github.collins993.deliveryapp.databinding.FragmentDriverDeliveryOrderDetailsBinding
import io.github.collins993.deliveryapp.databinding.FragmentDriverScheduledOrderBinding
import io.github.collins993.deliveryapp.viewmodel.MyViewModel
import io.github.collins993.deliveryapp.viewmodel.MyViewModelFactory


class DriverDeliveryOrderDetailsFragment : Fragment() {

    private var _binding: FragmentDriverDeliveryOrderDetailsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: MyViewModel
    val args: DriverDeliveryOrderDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDriverDeliveryOrderDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelProviderFactory
        ).get(MyViewModel::class.java)

        val orderDetails = args.orderDetails

        binding.pickUpAddress.text = orderDetails.pickUpAddress
        binding.pickUpDescription.text = orderDetails.pickUpDescription
        binding.pickUpFirstname.text = orderDetails.pickUpFirstName
        binding.pickUpLastname.text = orderDetails.pickUpLastName
        binding.pickUpEmailAddress.text = orderDetails.pickUpEmailAddress
        binding.pickUpPhoneNo.text = orderDetails.pickUpPhoneNumber
        binding.dropOffAddress.text = orderDetails.dropOffAddress
        binding.dropOffFirstname.text = orderDetails.dropOffFirstName
        binding.dropOffLastname.text = orderDetails.dropOffLastName
        binding.dropOffPhoneNo.text = orderDetails.dropOffPhoneNumber
        binding.dropOffEmailAddress.text = orderDetails.dropOffEmailAddress
        binding.dropOffScheduleOrder.text = orderDetails.scheduleOrder
        binding.orderStatus.text = orderDetails.deliveryStatus
        binding.date.text = orderDetails.dateAndTime
        binding.time.text = orderDetails.time

        binding.proceedBtn.setOnClickListener {

            val action = DriverDeliveryOrderDetailsFragmentDirections.actionDriverDeliveryOrderDetailsFragmentToDriverDeliveryMapsFragment(orderDetails)
            findNavController().navigate(action)
        }

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}