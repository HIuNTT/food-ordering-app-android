package com.example.shopbtlkotlin.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.adapter.PopularAdapter
import com.example.shopbtlkotlin.databinding.ActivitySearchFoodBinding
import com.example.shopbtlkotlin.model.Food
import com.example.shopbtlkotlin.util.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SearchFoodActivity : BaseActivity() {
    private lateinit var binding: ActivitySearchFoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFoodBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setEvent()
        searchFoodByKey()
    }

    private fun searchFoodByKey() {
        binding.search.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handleSearch(newText)
                return false
            }
        })
    }

    private fun handleSearch(newText: String?) {
        val ref = FirebaseUtils.getFoodRef()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<Food>()
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(Food::class.java)
                        if (item!!.title.lowercase().contains(newText.toString().lowercase())) {
                            items.add(item)
                        }
                    }
                    if (items.isNotEmpty()) {
                        binding.foodListView.layoutManager = GridLayoutManager(this@SearchFoodActivity, 2)
                        binding.foodListView.adapter = PopularAdapter(items)
                    } else {
                        binding.foodListView.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setEvent() {
        binding.btBack.setOnClickListener {
            finish()
        }
    }
}