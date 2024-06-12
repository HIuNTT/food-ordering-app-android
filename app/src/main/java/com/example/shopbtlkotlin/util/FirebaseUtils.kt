package com.example.shopbtlkotlin.util

import com.example.shopbtlkotlin.model.CartItem
import com.example.shopbtlkotlin.model.Food
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class FirebaseUtils {

    companion object {
        fun getBannerRef(): DatabaseReference = Firebase.database.reference.child("banners")

        fun getCategoryRef(): DatabaseReference = Firebase.database.reference.child("categories")

        fun getFoodRef(): DatabaseReference = Firebase.database.reference.child("foods")

        fun getUserRef(): DatabaseReference = Firebase.database.reference.child("users")

        fun getCurrentUser(): FirebaseUser? = Firebase.auth.currentUser

        fun getCartItemRef(): DatabaseReference = Firebase.database.reference.child("cart_items")

        fun getOrderRef(): DatabaseReference = Firebase.database.reference.child("orders")

        fun getAddressRef(): DatabaseReference = Firebase.database.reference.child("addresses")

        fun getPaymentRef(): DatabaseReference = Firebase.database.reference.child("payment_methods")

        fun getFoodById(foodId: Int, callBack: FoodCallBack) {
            val ref = getFoodRef().child(foodId.toString())
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val food = snapshot.getValue(Food::class.java)
                        food?.let { callBack.onFoodReceived(it) }
                    } else {
                        callBack.onFoodReceived(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callBack.onFoodReceived(null)
                }

            })
        }

        fun onUpdate(cartItem: CartItem) {
            val userId = getCurrentUser()?.uid
            val ref = getCartItemRef()
            ref.child(userId.toString()).child(cartItem.foodId.toString()).setValue(cartItem)
        }

        fun onDelete(cartItem: CartItem) {
            val userId = getCurrentUser()?.uid
            val ref = getCartItemRef()
            ref.child(userId.toString()).child(cartItem.foodId.toString()).removeValue()
        }

    }

    interface FoodCallBack {
        fun onFoodReceived(food: Food?)
    }
}