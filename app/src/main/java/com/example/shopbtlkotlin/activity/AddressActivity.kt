package com.example.shopbtlkotlin.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.adapter.AddressAdapter
import com.example.shopbtlkotlin.databinding.ActivityAddressBinding
import com.example.shopbtlkotlin.fragment.CartFragment
import com.example.shopbtlkotlin.model.Address
import com.example.shopbtlkotlin.util.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding

//    private var listAddress: MutableList<Address> = mutableListOf()
    private lateinit var adapter: AddressAdapter
    private var addressSelectecId: String? = "0"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getDataIntent()

        binding.addressView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val userId = FirebaseUtils.getCurrentUser()?.uid
        val ref = FirebaseUtils.getAddressRef().child(userId.toString())
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listAddress = mutableListOf<Address>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(Address::class.java)
                    if (item != null) {
                        listAddress.add(item)
                    }
                }
                adapter = AddressAdapter(listAddress, object : AddressAdapter.ItemAddressListener {
                    override fun onClickItemAddress(address: Address) {
                        val resultIntent = Intent()
                        resultIntent.putExtra("addressId", address.id.toString())
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }

                    override fun onClickEditAddress(address: Address) {
                        val intent = Intent(this@AddressActivity, UpdateAddressActivity::class.java)
                        intent.putExtra("address", address)
                        startActivity(intent)
                        finish()
                    }

                })
                adapter.setAddressSelectedId(addressSelectecId.toString())
                binding.addressView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        binding.btAddAddress.setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }
        binding.btBack.setOnClickListener {
            finish()
        }

        listenDataChanged()
    }

    private fun listenDataChanged() {
        val userId = FirebaseUtils.getCurrentUser()?.uid
        val ref = FirebaseUtils.getAddressRef().child(userId.toString())
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<Address>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(Address::class.java)
                    if (item != null) {
                        items.add(item)
                    }
                }
                adapter.setListAddress(items)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getDataIntent() {
        val bundle = intent.extras
        if (bundle == null){
            addressSelectecId = "0"
        } else {
            addressSelectecId = bundle.getString("address_id", "0")
            Log.e(">>> ", addressSelectecId.toString())
        }

    }
}