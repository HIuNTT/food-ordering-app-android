package com.example.shopbtlkotlin.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.databinding.ActivityDetailBinding
import com.example.shopbtlkotlin.model.CartItem
import com.example.shopbtlkotlin.model.Food
import com.example.shopbtlkotlin.util.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class DetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var food: Food
    private var num = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getIntentExtra()
        setVariable()

    }

    private fun setVariable() {
        binding.btBack.setOnClickListener {
            finish()
        }

        Glide.with(this)
            .load(food.picUrl)
            .into(binding.img)

        val price = if (food.price == food.price.toInt().toDouble()) {
            food.price.toInt().toString()
        } else {
            food.price.toString()
        }

        binding.priceTxt.text = "đ" + price
        binding.titleTxt.text = food.title
        binding.desTxt.text = food.description
        binding.ratingTxt.text = food.rating.toString()
        binding.ratingBar.rating = food.rating.toFloat()

//        lúc khởi tạo view, set btSub unused
        if (num == 1) binding.btSub.setImageResource(R.drawable.sub_icon_unused)

        binding.btAdd.setOnClickListener {
            if (num == 1) binding.btSub.setImageResource(R.drawable.sub_icon)
            num++
            binding.numberTxt.text = num.toString()
        }

        binding.btSub.setOnClickListener {
            if (num > 1) {
                num--
                if (num == 1) binding.btSub.setImageResource(R.drawable.sub_icon_unused)
                binding.numberTxt.text = num.toString()
            }
        }

        binding.btAddToCart.setOnClickListener {
            showLoading()
            val foodId = food.id
            val userId = FirebaseUtils.getCurrentUser()?.uid
            addToCart(foodId, userId, num)
        }
    }

    private fun addToCart(foodId: Int, userId: String?, num: Int) {
        val cartRef = FirebaseUtils.getCartItemRef()
        val userCartRef = cartRef.child(userId.toString())
//        Đọc cơ sở dữ liệu 1 lần duy nhất
        userCartRef.child(foodId.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var currentQuantity = snapshot.child("quantity").getValue<Int>()
                    currentQuantity = currentQuantity?.plus(num)
                    userCartRef.child(foodId.toString()).child("quantity").setValue(currentQuantity)
                    Toast.makeText(this@DetailActivity, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show()
                } else {
                    val cartItem = CartItem(foodId, userId.toString(), num)
                    userCartRef.child(foodId.toString()).setValue(cartItem).addOnSuccessListener {
                        Toast.makeText(this@DetailActivity, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show()
                    }
                        .addOnFailureListener {
                            Toast.makeText(this@DetailActivity, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        hideLoading()

    }

    private fun getIntentExtra() {
        food = intent.getSerializableExtra("food") as Food
    }
}