package io.github.collins993.deliveryapp.dashboard.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.databinding.DropOffFragmentBinding

class DropOffFragment : Fragment() {

    private var _binding: DropOffFragmentBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DropOffFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.proceedBtn.setOnClickListener {
            Toast.makeText(requireActivity(), "Delivery On its way", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}