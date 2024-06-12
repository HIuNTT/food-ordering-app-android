package com.example.shopbtlkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.databinding.ItemAddressBinding
import com.example.shopbtlkotlin.model.Address
import com.example.shopbtlkotlin.model.CartItem

class AddressAdapter(
    private var listAddress: List<Address>,
    private var itemAddressListener: ItemAddressListener
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    private lateinit var addressSelectedId: String

    inner class AddressViewHolder(val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setAddressSelectedId(id: String) {
        addressSelectedId = id
    }

    fun setListAddress(mList: List<Address>) {
        listAddress = mList
        notifyDataSetChanged()
    }


    interface ItemAddressListener {
        fun onClickItemAddress(address: Address)
        fun onClickEditAddress(address: Address)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddressAdapter.AddressViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressAdapter.AddressViewHolder, position: Int) {
        val address = listAddress[position]

        holder.binding.tvName.text = address.name
        holder.binding.tvPhone.text = address.phone
        holder.binding.tvAddress.text = address.address
        if (address.id == addressSelectedId) {
            holder.binding.ivStatus.setImageResource(R.drawable.radio_button_checked)
        } else {
            holder.binding.ivStatus.setImageResource(R.drawable.radio_button_unchecked)
        }
        holder.binding.tvEdit.setOnClickListener {
            itemAddressListener.onClickEditAddress(address)
        }
        holder.itemView.setOnClickListener {
            itemAddressListener.onClickItemAddress(address)
        }
    }

    override fun getItemCount(): Int {
        return listAddress.size
    }
}