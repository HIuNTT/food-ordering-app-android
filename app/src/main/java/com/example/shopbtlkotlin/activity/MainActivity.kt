package com.example.shopbtlkotlin.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.adapter.ViewPagerAdapter
import com.example.shopbtlkotlin.databinding.ActivityMainBinding
import com.example.shopbtlkotlin.fragment.CartFragment
import com.example.shopbtlkotlin.fragment.HomeFragment
import com.example.shopbtlkotlin.fragment.MyselfFragment
import com.example.shopbtlkotlin.fragment.OrderFragment

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentList = arrayListOf<Fragment>(
            HomeFragment(),
            OrderFragment(),
            CartFragment(),
            MyselfFragment()
        )

        adapter = ViewPagerAdapter(fragmentList, supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mHome -> {
                    binding.viewPager.setCurrentItem(0, false)
                    return@setOnItemSelectedListener true
                }
                R.id.mOrder -> {
                    binding.viewPager.setCurrentItem(1, false)
                    return@setOnItemSelectedListener true
                }
                R.id.mCart -> {
                    binding.viewPager.setCurrentItem(2, false)
                    return@setOnItemSelectedListener true
                }
                R.id.mMyself -> {
                    binding.viewPager.setCurrentItem(3, false)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

}