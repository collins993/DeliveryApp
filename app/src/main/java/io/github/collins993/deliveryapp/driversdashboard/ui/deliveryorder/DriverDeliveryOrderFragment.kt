package io.github.collins993.deliveryapp.driversdashboard.ui

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
import io.github.collins993.deliveryapp.databinding.FragmentOrderHistoryBinding
import io.github.collins993.deliveryapp.databinding.FragmentSecondHomeBinding
import io.github.collins993.deliveryapp.viewmodel.MyViewModel
import io.github.collins993.deliveryapp.viewmodel.MyViewModelFactory

class SecondHomeFragment : Fragment() {

    private var _binding: FragmentSecondHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var recAdapter: MyAdapter
    private lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelProviderFactory
        ).get(MyViewModel::class.java)

        setUpRecyclerView()

        viewModel.retrieveAllDriverDeliveryOrders()

        viewModel.getDriverDeliveryOrderStatus.observe(viewLifecycleOwner, Observer { documents ->
            recAdapter.differ.submitList(documents)
        })

        recAdapter.setOnItemClickListener {
            //Toast.makeText(requireActivity(), "Clicked", Toast.LENGTH_SHORT).show()
            val bundle = Bundle().apply {
                putSerializable("orderDetails", it)
            }
            findNavController().navigate(R.id.action_nav_home1_to_driverDeliveryOrderDetailsFragment, bundle)
        }


        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpRecyclerView() {
        recAdapter = MyAdapter()
        binding.orderListRecyclerview.apply {
            adapter = recAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}