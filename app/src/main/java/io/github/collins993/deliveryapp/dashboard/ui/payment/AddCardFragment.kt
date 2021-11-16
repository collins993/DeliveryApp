package io.github.collins993.deliveryapp.dashboard.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.databinding.FragmentAddCardBinding
import io.github.collins993.deliveryapp.databinding.FragmentPaymentBinding


class AddCardFragment : Fragment() {

    private var _binding: FragmentAddCardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

     override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}