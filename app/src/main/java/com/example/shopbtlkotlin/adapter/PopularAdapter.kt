package com.example.shopbtlkotlin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.shopbtlkotlin.activity.DetailActivity
import com.example.shopbtlkotlin.databinding.ItemPopularBinding
import com.example.shopbtlkotlin.model.Food

class PopularAdapter(private val items: List<Food>) :
    RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    private lateinit var context: Context

    inner class PopularViewHolder(val binding: ItemPopularBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularAdapter.PopularViewHolder {
        context = parent.context
        val binding = ItemPopularBinding.inflate(LayoutInflater.from(context), parent, false)
        return PopularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularAdapter.PopularViewHolder, position: Int) {
        holder.binding.titleTxt.text = items[position].title

        val price = if (items[position].price == items[position].price.toInt().toDouble()) {
            items[position].price.toInt().toString()
        } else {
            items[position].price.toString()
        }

        holder.binding.priceTxt.text = "đ" + price

        val rating = if (items[position].rating == items[position].rating.toInt().toDouble()) {
            items[position].rating.toInt().toString()
        } else {
            items[position].rating.toString()
        }

        holder.binding.ratingTxt.text = rating

        val requestOption = RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(items[position].picUrl)
            .apply(requestOption)
            .into(holder.binding.img)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("food", items[position])
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = items.size
}