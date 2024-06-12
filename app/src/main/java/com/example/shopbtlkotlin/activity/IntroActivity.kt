package com.example.shopbtlkotlin.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.example.shopbtlkotlin.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setVariable()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        )

    }

    private fun setVariable() {
        binding.btStart.setOnClickListener {
            if (auth.currentUser != null) {
                startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}