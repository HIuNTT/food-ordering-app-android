package com.example.shopbtlkotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.shopbtlkotlin.databinding.ItemCartBinding
import com.example.shopbtlkotlin.model.CartItem
import com.example.shopbtlkotlin.model.Food
import com.example.shopbtlkotlin.util.FirebaseUtils

class CartAdapter(
    private var listCartItem: List<CartItem>,
) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private lateinit var context: Context
    private var mCartItemListener: CartItemListener? = null
    private var addressSelectedId = ""


    fun setCartItemListener(cartItemListener: CartItemListener) {
        mCartItemListener = cartItemListener
    }

    fun setListCartItem(mList: List<CartItem>) {
        listCartItem = mList
        notifyDataSetChanged()
    }

    fun getCartItemList(): List<CartItem> {
        return listCartItem
    }

    constructor() : this(listCartItem = mutableListOf<CartItem>())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartAdapter.CartViewHolder {
        context = parent.context
        val binding = ItemCartBinding.inflate(LayoutInflater.from(context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        val ci = listCartItem[position]

        FirebaseUtils.getFoodById(ci.foodId, object : FirebaseUtils.FoodCallBack {
            override fun onFoodReceived(food: Food?) {
                holder.binding.titleTxt.text = food?.title ?: ""
                holder.binding.priceTxt.text = "đ${food?.price}"
                Glide.with(context)
                    .load(food?.picUrl)
                    .transform(CenterCrop(), RoundedCorners(30))
                    .into(holder.binding.img)
            }
        })


        holder.binding.btSub.setOnClickListener {
            if (mCartItemListener != null) {
//                var quantity = (holder.binding.numberTxt.text.toString()).toIntOrNull()

                var quantity = ci.quantity
                if (quantity == 1) {
                    mCartItemListener?.onItemDelete(context, ci)
                    notifyDataSetChanged()
                } else {
                    quantity--
                    ci.quantity = quantity
                    mCartItemListener?.onItemUpdate(ci)
                    notifyDataSetChanged()
                }

            }
        }

        holder.binding.numberTxt.text = ci.quantity.toString()

        holder.binding.btAdd.setOnClickListener {
            if (mCartItemListener != null) {
                var quantity = ci.quantity
                quantity++
                ci.quantity = quantity
                mCartItemListener?.onItemUpdate(ci)
                notifyDataSetChanged()
            }
        }

        holder.binding.btDelete.setOnClickListener {
            if (mCartItemListener != null) {
                mCartItemListener?.onItemDelete(context, ci)
                notifyDataSetChanged()
            }
        }

    }

    override fun getItemCount(): Int = listCartItem.size


    class CartViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    interface CartItemListener {
        fun onItemDelete(context: Context, cartItem: CartItem)
        fun onItemUpdate(cartItem: CartItem)
    }
}