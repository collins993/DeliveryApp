package io.github.collins993.deliveryapp.dashboard.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addCardBtn.setOnClickListener {
            findNavController().navigate(R.id.action_nav_payment_to_addCardFragment)
        }
        binding.orderRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val checkedRadioButtonId = radioGroup.checkedRadioButtonId
            when (checkedRadioButtonId) {
                R.id.cash_on_pickup -> {
                    Toast.makeText(requireActivity(), "Cash on Pickup", Toast.LENGTH_SHORT)
                        .show()

                }
                R.id.cash_on_delivery -> {
                    Toast.makeText(
                        requireActivity(),
                        "Cash on Delivery",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}