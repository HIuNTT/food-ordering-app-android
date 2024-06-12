package com.example.shopbtlkotlin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopbtlkotlin.activity.ListFoodsActivity
import com.example.shopbtlkotlin.databinding.ItemCategoryBinding
import com.example.shopbtlkotlin.model.Category

class CategoryAdapter(private val list: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CategoryViewHolder {
        context = parent.context
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryViewHolder, position: Int) {
        val item = list[position]
        holder.binding.titleCat.text = item.title

        Glide.with(holder.itemView.context)
            .load(item.picUrl)
            .into(holder.binding.imgCat)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ListFoodsActivity::class.java)
            intent.putExtra("categoryId", item.id)
            intent.putExtra("category", item.title)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = list.size

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}