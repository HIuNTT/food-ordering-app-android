package com.example.shopbtlkotlin.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.adapter.PopularAdapter
import com.example.shopbtlkotlin.databinding.ActivityListFoodsBinding
import com.example.shopbtlkotlin.model.Food
import com.example.shopbtlkotlin.util.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class ListFoodsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListFoodsBinding
    private var categoryId: Int = 0
    private var categoryName: String? = null
    private var isAll: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListFoodsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getIntentExtra()

        initList()

        if (isAll) {
            initListAll()
            binding.titleTxt.text = "Đồ Ăn Phổ Biến"
        }
    }

    private fun initListAll() {
        val ref = FirebaseUtils.getFoodRef().orderByChild("isPopular").equalTo(true)
        binding.loadingList.visibility = View.VISIBLE

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<Food>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(Food::class.java)
                    if (item != null) {
                        items.add(item)
                    }
                }
                if (items.isNotEmpty()) {
                    binding.foodListView.layoutManager = GridLayoutManager(this@ListFoodsActivity, 2)
                    binding.foodListView.adapter = PopularAdapter(items)
                }
                binding.loadingList.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun initList() {
        val ref = FirebaseUtils.getFoodRef()
        binding.loadingList.visibility = View.VISIBLE

        val query = ref.orderByChild("categoryId").equalTo(categoryId.toDouble())
        query.addValueEventListener(object : ValueEventListener{
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
                        binding.foodListView.layoutManager = GridLayoutManager(this@ListFoodsActivity, 2)
                        binding.foodListView.adapter = PopularAdapter(items)
                    }
                    binding.loadingList.visibility = View.GONE
                } else {
                    binding.loadingList.visibility = View.GONE
                    binding.notFound.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getIntentExtra() {
        categoryId = intent.getIntExtra("categoryId", 0)
        categoryName = intent.getStringExtra("category")
        isAll = intent.getBooleanExtra("isAll", false)
        binding.titleTxt.text = categoryName
        binding.btBack.setOnClickListener {
            finish()
        }
    }
}

