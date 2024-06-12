package com.example.shopbtlkotlin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.shopbtlkotlin.adapter.OrderAdapter
import com.example.shopbtlkotlin.databinding.FragmentOrderBinding
import com.example.shopbtlkotlin.model.MyOrder
import com.example.shopbtlkotlin.util.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firestore.v1.StructuredQuery.Order

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private val userId = FirebaseUtils.getCurrentUser()?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }

    private fun setData() {
        val ref = FirebaseUtils.getOrderRef().child(userId.toString())
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val items = mutableListOf<MyOrder>()
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(MyOrder::class.java)
                        if (item != null) {
                            items.add(item)
                        }
                    }
                    if (items.isNotEmpty()) {
                        binding.orderView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.orderView.adapter = OrderAdapter(items)
                    }
                } else {
                    binding.scrollView.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}