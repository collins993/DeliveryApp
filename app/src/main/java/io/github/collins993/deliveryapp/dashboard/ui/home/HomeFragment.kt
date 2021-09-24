package io.github.collins993.deliveryapp.dashboard.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.inputLayoutAddress.setOnClickListener {
            Toast.makeText(requireActivity(), "Open Map", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_nav_home_to_drop_off_fragment)
        }
        binding.proceedBtn.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_drop_off_fragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}