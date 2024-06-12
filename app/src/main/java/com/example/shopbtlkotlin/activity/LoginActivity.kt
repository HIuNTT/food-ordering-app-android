package com.example.shopbtlkotlin.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.databinding.ActivityLoginBinding
import com.example.shopbtlkotlin.util.Validate

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            finish()
        }

        binding.btSignIn.setOnClickListener {
            singIn()
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

    }

    private fun singIn() {
        showLoading()
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        if (!Validate.isValidEmail(email)) {
            hideLoading()
            binding.editEmail.error = "Vui lòng nhập email"
            return
        }

        if (!Validate.isValidPassword(password)) {
            hideLoading()
            binding.editPassword.error = "Vui lòng nhập mật khẩu"
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }
}