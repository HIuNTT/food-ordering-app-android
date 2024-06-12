package com.example.shopbtlkotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.shopbtlkotlin.databinding.ItemOrderBinding
import com.example.shopbtlkotlin.model.Food
import com.example.shopbtlkotlin.model.MyOrder
import com.example.shopbtlkotlin.util.FirebaseUtils

class OrderAdapter(private var listOrder: List<MyOrder>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private lateinit var context: Context

    class OrderViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapter.OrderViewHolder {
        context = parent.context
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(context), parent, false)
        return OrderViewHolder(binding)
    }


    override fun getItemCount(): Int = listOrder.size

    override fun onBindViewHolder(holder: OrderAdapter.OrderViewHolder, position: Int) {
        val order = listOrder[position]

        FirebaseUtils.getFoodById(order.cartItems!![0].foodId, object : FirebaseUtils.FoodCallBack {
            override fun onFoodReceived(food: Food?) {
                holder.binding.titleTxt.text = food?.title ?: ""
                holder.binding.priceTxt.text = "đ${food?.price}"
                Glide.with(context)
                    .load(food?.picUrl)
                    .transform(CenterCrop(), RoundedCorners(30))
                    .into(holder.binding.img)
                holder.binding.numberTxt.text = order.cartItems[0].quantity.toString()
            }
        })

        if (order.cartItems.size > 1) {
            holder.binding.viewAdd.visibility = View.VISIBLE
            holder.binding.viewAddBottom.visibility = View.VISIBLE
        } else {
            holder.binding.viewAdd.visibility = View.GONE
            holder.binding.viewAddBottom.visibility = View.GONE
        }

        holder.binding.totalNumber.text = "${order.cartItems.size} sản phẩm"
        holder.binding.totalPrice.text = "đ${order.total}"
        holder.binding.statusTxt.text = order.status
    }
}