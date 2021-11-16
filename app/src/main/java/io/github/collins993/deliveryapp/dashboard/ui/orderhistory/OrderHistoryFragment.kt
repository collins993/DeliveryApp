package io.github.collins993.deliveryapp.dashboard.ui.orderhistory

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.collins993.deliveryapp.R
import io.github.collins993.deliveryapp.adapter.MyAdapter
import io.github.collins993.deliveryapp.databinding.FragmentOrderHistoryBinding
import io.github.collins993.deliveryapp.viewmodel.MyViewModel
import io.github.collins993.deliveryapp.viewmodel.MyViewModelFactory

class OrderHistoryFragment : Fragment() {

    private var _binding: FragmentOrderHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var recAdapter: MyAdapter
    private lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModelProviderFactory = MyViewModelFactory(Application())
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelProviderFactory
        ).get(MyViewModel::class.java)

        viewModel.retrieveAllOrders()
        setUpRecyclerView()

        viewModel.getOrderStatus.observe(viewLifecycleOwner, Observer { documents ->
            recAdapter.differ.submitList(documents)
        })



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recAdapter.setOnItemClickListener {
            //Toast.makeText(requireActivity(), "Clicked", Toast.LENGTH_SHORT).show()
            val bundle = Bundle().apply {
                putSerializable("orderDetails", it)
            }
            findNavController().navigate(R.id.action_nav_order_history_to_orderDetailsFragment, bundle)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
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