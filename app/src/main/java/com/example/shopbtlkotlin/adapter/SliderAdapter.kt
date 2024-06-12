package com.example.shopbtlkotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.model.Slider

class SliderAdapter(private var list: List<Slider>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private lateinit var context: Context
    private val runnable = Runnable {
        list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SliderAdapter.SliderViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slider_item, parent, false)
        return SliderViewHolder(view)
    }

//    Gán dữ liệu vào từng item
    override fun onBindViewHolder(holder: SliderAdapter.SliderViewHolder, position: Int) {
        holder.setImage(list[position], context)
        if (position == list.size - 2) {
            viewPager2.post(runnable)
        }

    }

    override fun getItemCount(): Int = list.size

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageSlide)

        fun setImage(sliderItem: Slider, context: Context) {
            val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(60))

            Glide.with(context)
                .load(sliderItem.url)
                .apply(requestOptions)
                .into(imageView)
        }
    }

}