package com.example.shopbtlkotlin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.shopbtlkotlin.activity.ListFoodsActivity
import com.example.shopbtlkotlin.activity.SearchFoodActivity
import com.example.shopbtlkotlin.adapter.CategoryAdapter
import com.example.shopbtlkotlin.adapter.PopularAdapter
import com.example.shopbtlkotlin.adapter.SliderAdapter
import com.example.shopbtlkotlin.databinding.FragmentHomeBinding
import com.example.shopbtlkotlin.model.Category
import com.example.shopbtlkotlin.model.Food
import com.example.shopbtlkotlin.model.Slider
import com.example.shopbtlkotlin.util.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBanner()
        initCategory()
        initPopular()
        initWelcome()
        setEvent()
    }

    private fun setEvent() {
        binding.tvSeeAll.setOnClickListener {
            val intent = Intent(context, ListFoodsActivity::class.java)
            intent.putExtra("isAll", true)
            startActivity(intent)
        }

        binding.btSearch.setOnClickListener {
            startActivity(Intent(context, SearchFoodActivity::class.java))
        }
    }

    private fun initWelcome() {
        val id = FirebaseUtils.getCurrentUser()?.uid
        val ref = FirebaseUtils.getUserRef()
        ref.child(id.toString()).child("username").get().addOnSuccessListener {
            binding.usernameTxt.text = it.value.toString()
        }.addOnFailureListener {
            binding.usernameTxt.text = "Vô danh"
        }
    }

    private fun initPopular() {
        val ref = FirebaseUtils.getFoodRef().orderByChild("isPopular").equalTo(true).limitToFirst(4)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<Food>()
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(Food::class.java)
                        if (item != null) {
                            items.add(item)
                        }
                    }
                    if (items.isNotEmpty()) {
                        binding.viewPopular.layoutManager = GridLayoutManager(context, 2)
                        binding.viewPopular.adapter = PopularAdapter(items)
                    }
                    binding.loadingPopular.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun initCategory() {
        val ref = FirebaseUtils.getCategoryRef()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<Category>()
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(Category::class.java)
                        if (item != null) {
                            items.add(item)
                        }
                    }
                    if (items.isNotEmpty()) {
                        binding.viewCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        binding.viewCategory.adapter = CategoryAdapter(items)
                    }
                    binding.loadingCategory.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun initBanner() {
        val ref = FirebaseUtils.getBannerRef()
        binding.loadingBanner.visibility = View.VISIBLE

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<Slider>()
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(Slider::class.java)
                        if (item != null) {
                            items.add(item)
                        }
                    }
                    banners(items)
                    binding.loadingBanner.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun banners(items: List<Slider>) {
        binding.viewPager.adapter = SliderAdapter(items, binding.viewPager)
        binding.viewPager.clipChildren = false
        binding.viewPager.clipToPadding = false
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewPager.setPageTransformer(compositePageTransformer)
        if (items.isNotEmpty()) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewPager)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}