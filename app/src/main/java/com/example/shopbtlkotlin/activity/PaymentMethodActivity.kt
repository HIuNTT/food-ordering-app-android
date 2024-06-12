package com.example.shopbtlkotlin.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.adapter.PaymentMethodAdapter
import com.example.shopbtlkotlin.databinding.ActivityPaymentMethodBinding
import com.example.shopbtlkotlin.model.PaymentMethod
import com.example.shopbtlkotlin.util.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class PaymentMethodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentMethodBinding
    private lateinit var adapter: PaymentMethodAdapter

    private var paymentMethodSelectedId: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getDataIntent()

        binding.btBack.setOnClickListener {
            finish()
        }

        binding.paymentView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        val ref = FirebaseUtils.getPaymentRef()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listPaymentMethod = mutableListOf<PaymentMethod>()
                for (childSnapshot in snapshot.children) {
                    val paymentMethod = childSnapshot.getValue(PaymentMethod::class.java)
                    if (paymentMethod != null) {
                        listPaymentMethod.add(paymentMethod)
                    }
                }
                adapter = PaymentMethodAdapter(listPaymentMethod, object : PaymentMethodAdapter.ItemPaymentMethodListener {
                    override fun onClickItemPaymentMethod(paymentMethod: PaymentMethod) {
                        val intent = Intent()
                        intent.putExtra("paymentMethodId", paymentMethod.id.toString())
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                })
                adapter.setPaymentIdSelected(paymentMethodSelectedId!!.toInt())
                binding.paymentView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun getDataIntent() {
        val bundle: Bundle? = intent.extras
        if (bundle == null) {
            paymentMethodSelectedId = 0
        } else {
            paymentMethodSelectedId = bundle.getInt("payment_method_id", 0)
        }
    }
}