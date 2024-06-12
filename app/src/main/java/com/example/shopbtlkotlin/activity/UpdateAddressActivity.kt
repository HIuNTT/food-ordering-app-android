package com.example.shopbtlkotlin.activity

import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.databinding.ActivityUpdateAddressBinding
import com.example.shopbtlkotlin.databinding.CustomDialogBinding
import com.example.shopbtlkotlin.model.Address
import com.example.shopbtlkotlin.util.FirebaseUtils
import com.example.shopbtlkotlin.util.Validate

class UpdateAddressActivity : BaseActivity() {

    private lateinit var binding: ActivityUpdateAddressBinding
    private var a: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateAddressBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getDataIntent()
        setData()

        binding.btSave.setOnClickListener {
            updateAddress()
        }

        binding.btBack.setOnClickListener {
            finish()
        }

        binding.btDelete.setOnClickListener {
            val dialogBinding: CustomDialogBinding
            dialogBinding = CustomDialogBinding.inflate(LayoutInflater.from(this))

            val dialog = Dialog(this)
            dialog.setContentView(dialogBinding.root)
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.window!!.setGravity(Gravity.CENTER)
            }
            dialog.setCancelable(true)
            dialog.show()

            dialogBinding.btCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialogBinding.btDelete.setOnClickListener {
                deleteAddress()
                dialog.dismiss()
            }

        }
    }

    private fun deleteAddress() {
        val userId = FirebaseUtils.getCurrentUser()?.uid
        val ref = FirebaseUtils.getAddressRef()
        ref.child(userId.toString()).child(a?.id.toString()).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show()
            hideLoading()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show()
            hideLoading()
            finish()
        }
    }

    private fun updateAddress() {
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
        val ref = FirebaseUtils.getAddressRef()
        val addressObject =
            Address(id = a?.id.toString(), name = name, phone = phone, address = address)

        ref.child(userId.toString()).child(a?.id.toString()).setValue(addressObject)
            .addOnSuccessListener {
                Toast.makeText(this, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show()
                hideLoading()
                finish()
            }.addOnFailureListener {
            Toast.makeText(this, "Chỉnh sửa thất bại", Toast.LENGTH_SHORT).show()
            hideLoading()
            finish()
        }
    }

    private fun setData() {
        binding.eName.setText(a?.name)
        binding.ePhone.setText(a?.phone)
        binding.eAddress.setText(a?.address)
    }

    private fun getDataIntent() {
        val intent = intent
        a = intent.getSerializableExtra("address") as Address
    }
}