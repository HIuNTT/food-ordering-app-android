package com.example.shopbtlkotlin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.activity.IntroActivity
import com.example.shopbtlkotlin.databinding.FragmentCartBinding
import com.example.shopbtlkotlin.databinding.FragmentMyselfBinding
import com.example.shopbtlkotlin.util.FirebaseUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database

class MyselfFragment : Fragment() {

    private var _binding: FragmentMyselfBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyselfBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (FirebaseUtils.getCurrentUser() != null) {
            binding.btLogout.visibility = View.VISIBLE
        }
        binding.btLogout.setOnClickListener {
            val ref = Firebase.auth
            if (FirebaseUtils.getCurrentUser() != null) {
                ref.signOut()
                startActivity(Intent(context, IntroActivity::class.java))
            }
        }
    }
}