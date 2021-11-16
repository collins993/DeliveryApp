package io.github.collins993.deliveryapp.adapter

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.collins993.deliveryapp.databinding.OrderHistoryLayoutBinding
import io.github.collins993.deliveryapp.models.DeliveryModel
import java.text.NumberFormat

class MyAdapter(): RecyclerView.Adapter<MyAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: OrderHistoryLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<DeliveryModel>() {

        override fun areItemsTheSame(oldItem: DeliveryModel, newItem: DeliveryModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: DeliveryModel, newItem: DeliveryModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            OrderHistoryLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val delivery = differ.currentList[position]
         holder.binding.orderDesc.text = delivery.pickUpDescription
        holder.binding.orderDate.text = delivery.dateAndTime
        holder.binding.orderStatus.text = delivery.deliveryStatus
        holder.binding.orderTime.text = delivery.time
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(delivery) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((DeliveryModel) -> Unit)? = null


    fun setOnItemClickListener(listener: (DeliveryModel) -> Unit){
        onItemClickListener = listener
    }
}