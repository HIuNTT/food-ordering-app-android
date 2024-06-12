package com.example.shopbtlkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.databinding.ItemPaymentMethodBinding
import com.example.shopbtlkotlin.model.PaymentMethod

class PaymentMethodAdapter(
    private var listPaymentMethod: List<PaymentMethod>,
    private var itemPaymentMethodListener: ItemPaymentMethodListener
): RecyclerView.Adapter<PaymentMethodAdapter.PaymentViewHolder>() {

    private var paymentIdSelected: Int = 0

    interface ItemPaymentMethodListener {
        fun onClickItemPaymentMethod(paymentMethod: PaymentMethod)
    }

    fun setPaymentIdSelected(id: Int) {
        paymentIdSelected = id
    }

    inner class PaymentViewHolder(val binding: ItemPaymentMethodBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentMethodAdapter.PaymentViewHolder {
        val binding = ItemPaymentMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentMethodAdapter.PaymentViewHolder, position: Int) {
        val payment = listPaymentMethod[position]

        when (payment.id) {
            1 -> {
                holder.binding.img.setImageResource(R.drawable.cash)
                return
            }
            2 -> {
                holder.binding.img.setImageResource(R.drawable.card)
            }
        }

        holder.binding.tvName.text = payment.name


        if (payment.id == paymentIdSelected) {
            holder.binding.ivStatus.setImageResource(R.drawable.check_circle)

        } else {
            holder.binding.ivStatus.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            itemPaymentMethodListener.onClickItemPaymentMethod(payment)
        }
    }

    override fun getItemCount(): Int = listPaymentMethod.size
}