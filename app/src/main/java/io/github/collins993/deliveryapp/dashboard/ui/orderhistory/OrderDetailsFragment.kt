package io.github.collins993.deliveryapp.dashboard.ui.orderhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.databinding.FragmentOrderDetailsBinding
import io.github.collins993.deliveryapp.databinding.FragmentOrderHistoryBinding

class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val args: OrderDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
        binding.dropOffScheduleOrder.text = orderDetails.deliveryTime
        binding.orderStatus.text = orderDetails.deliveryStatus
        binding.date.text = orderDetails.dateAndTime
        binding.time.text = orderDetails.time

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}