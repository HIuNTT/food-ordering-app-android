package com.example.shopbtlkotlin.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.databinding.ActivityAddAddressBinding
import com.example.shopbtlkotlin.model.Address
import com.example.shopbtlkotlin.util.FirebaseUtils
import com.example.shopbtlkotlin.util.Validate

class AddAddressActivity : BaseActivity() {

    private lateinit var binding: ActivityAddAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAddressBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btSave.setOnClickListener {
            saveAddress()
        }

        binding.btBack.setOnClickListener {
            finish()
        }
    }

    private fun saveAddress() {
        showLoading()
        val name = binding.eName.text.toString()
        val phone = binding.ePhone.text.toString()
        val address = binding.eAddress.text.toString()

        if (!Validate.isValidUsername(name)) {
            hideLoading()
            binding.eName.error = "Tên người nhận không hợp lệ"
            return
        }

        if (phone.length > 10 || phone.length < 10 || phone.isEmpty()) {
            hideLoading()
            binding.ePhone.error = "Số điện thoại không hợp lệ"
            return
        }

        if (!Validate.isValidUsername(address)) {
            hideLoading()
            binding.eAddress.error = "Địa chỉ không được để trống"
            return
        }

        val userId = FirebaseUtils.getCurrentUser()?.uid
        val addressId= FirebaseUtils.getAddressRef().child(userId.toString()).push().key
        val ref = FirebaseUtils.getAddressRef()
        val a = Address(id = addressId.toString(), name = name, phone = phone, address = address)

        ref.child(userId.toString()).child(addressId.toString()).setValue(a).addOnSuccessListener {
            Toast.makeText(this, "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show()
            hideLoading()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Thêm địa chỉ thất bại", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}