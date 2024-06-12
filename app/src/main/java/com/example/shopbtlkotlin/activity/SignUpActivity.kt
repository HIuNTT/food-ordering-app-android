package com.example.shopbtlkotlin.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.databinding.ActivitySignUpBinding
import com.example.shopbtlkotlin.model.User
import com.example.shopbtlkotlin.util.Validate

class SignUpActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txSignIn.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()
        }

        binding.btSignUp.setOnClickListener {
            registerUser()
        }

        binding.togglePass.setImageResource(R.drawable.visibility_off_ic)

        binding.togglePass.setOnClickListener {
            if (binding.editPassword.transformationMethod is PasswordTransformationMethod) {
                binding.editPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.togglePass.setImageResource(R.drawable.visibility_ic)
            } else {
                binding.editPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.togglePass.setImageResource(R.drawable.visibility_off_ic)
            }
            binding.editPassword.setSelection(binding.editPassword.text.length)
        }

        binding.toggleRepeatPass.setImageResource(R.drawable.visibility_off_ic)

        binding.toggleRepeatPass.setOnClickListener {
            if (binding.editRepeatPassword.transformationMethod is PasswordTransformationMethod) {
                binding.editRepeatPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.toggleRepeatPass.setImageResource(R.drawable.visibility_ic)
            } else {
                binding.editRepeatPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.toggleRepeatPass.setImageResource(R.drawable.visibility_off_ic)
            }
            binding.editRepeatPassword.setSelection(binding.editRepeatPassword.text.length)
        }

    }

    private fun registerUser() {

        showLoading()

        val username = binding.editUsername.text.toString().trim()
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()
        val repeatPassword = binding.editRepeatPassword.text.toString().trim()

        if (!Validate.isValidEmail(email)) {
            hideLoading()
            binding.editEmail.error = "Vui lòng nhập email"
            return
        }

        if (!Validate.isValidUsername(username)) {
            hideLoading()
            binding.editUsername.error = "Vui lòng nhập username"
            return
        }

        if (!Validate.isValidPassword(password)) {
            hideLoading()
            binding.editPassword.error = "Vui lòng nhập mật khẩu"
            return
        } else if (password.length < 6) {
            hideLoading()
            binding.editPassword.error = "Mật khẩu phải hơn 6 kí tự"
            return
        }

        if (!Validate.isValidPassword(repeatPassword)) {
            hideLoading()
            binding.editRepeatPassword.error = "Vui lòng nhập mật khẩu"
            return
        } else if (repeatPassword.length < 6) {
            hideLoading()
            binding.editRepeatPassword.error = "Mật khẩu phải hơn 6 kí tự"
            return
        }

        if (Validate.isValidPassword(password) && Validate.isValidPassword(repeatPassword) && !password.equals(repeatPassword)) {
            hideLoading()
            binding.editRepeatPassword.error = "Mật khẩu không khớp"
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val ref = database.reference
                val currentUser = auth.currentUser
                val userId = currentUser?.uid
                val user = User(username = username, email = email, password = password)
                ref.child("users").child(userId.toString()).setValue(user)
                hideLoading()
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
                hideLoading()
            }
        }
    }
}