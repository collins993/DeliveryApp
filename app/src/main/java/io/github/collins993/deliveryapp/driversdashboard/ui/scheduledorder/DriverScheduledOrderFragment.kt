package io.github.collins993.deliveryapp.driversdashboard.ui.scheduledorder

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.adapter.MyAdapter
import io.github.collins993.deliveryapp.databinding.FragmentDriverScheduledOrderBinding
import io.github.collins993.deliveryapp.viewmodel.MyViewModel
import io.github.collins993.deliveryapp.viewmodel.MyViewModelFactory

class DriverScheduledOrderFragment : Fragment() {


    private var _binding: FragmentDriverScheduledOrderBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var recAdapter: MyAdapter
    private lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDriverScheduledOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelProviderFactory
        ).get(MyViewModel::class.java)

        setUpRecyclerView()

        viewModel.retrieveAllDriverScheduledDelivery()

        viewModel.getDriverScheduleOrderStatus.observe(viewLifecycleOwner, Observer { documents ->
            recAdapter.differ.submitList(documents)
        })

        recAdapter.setOnItemClickListener {
            //Toast.makeText(requireActivity(), "Clicked", Toast.LENGTH_SHORT).show()
            val bundle = Bundle().apply {
                putSerializable("orderDetails", it)
            }
            findNavController().navigate(R.id.action_driverScheduledOrderFragment_to_driverDeliveryOrderDetailsFragment, bundle)
        }

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpRecyclerView() {
        recAdapter = MyAdapter()
        binding.scheduleListRecyclerview.apply {
            adapter = recAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}