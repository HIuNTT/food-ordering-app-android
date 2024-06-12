package com.example.shopbtlkotlin.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.shopbtlkotlin.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

open class BaseActivity : AppCompatActivity() {

    protected lateinit var dialog: Dialog

    protected lateinit var database: FirebaseDatabase
    protected lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        database = Firebase.database

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
//        đổi màu các icon ở status bar thành đen, do nền màu trắng light
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        createLoadingDialog()

    }

    private fun createLoadingDialog() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_loading)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        dialog.setCancelable(false)
    }

    fun showLoading() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun hideLoading() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
}