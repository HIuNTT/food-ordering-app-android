package com.example.shopbtlkotlin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopbtlkotlin.R
import com.example.shopbtlkotlin.activity.AddressActivity
import com.example.shopbtlkotlin.activity.MainActivity
import com.example.shopbtlkotlin.activity.PaymentMethodActivity
import com.example.shopbtlkotlin.adapter.CartAdapter
import com.example.shopbtlkotlin.databinding.CustomDialogBinding
import com.example.shopbtlkotlin.databinding.FragmentCartBinding
import com.example.shopbtlkotlin.model.Address
import com.example.shopbtlkotlin.model.CartItem
import com.example.shopbtlkotlin.model.Food
import com.example.shopbtlkotlin.model.MyOrder
import com.example.shopbtlkotlin.model.PaymentMethod
import com.example.shopbtlkotlin.util.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment(), CartAdapter.CartItemListener {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val userId = FirebaseUtils.getCurrentUser()?.uid
    private lateinit var adapter: CartAdapter

    private var paymentMethodSelected: PaymentMethod? = null
    private var addressSelected: Address? = null

    private lateinit var getAction: ActivityResultLauncher<Intent>

    @Suppress("UNREACHABLE_CODE")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        binding.cartView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = CartAdapter()
        binding.cartView.adapter = adapter
        adapter.setCartItemListener(this)
        listenerDataChanged()
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    private fun listenerDataChanged() {
        val ref = FirebaseUtils.getCartItemRef().child(userId.toString())
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val items = mutableListOf<CartItem>()
                    var count = 0
                    var numTotal = 0
                    var priceTotal = 0.0
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(CartItem::class.java)
                        if (item != null) {
                            items.add(item)
                        }
                    }
                    for (item in items) {
                        FirebaseUtils.getFoodById(item.foodId, object : FirebaseUtils.FoodCallBack {
                            override fun onFoodReceived(food: Food?) {
                                priceTotal += food?.price!! * item.quantity
                                count++
                                if (count == items.size) {
                                    binding.totalPrice.text = " đ${priceTotal}"
                                }
                            }
                        })
                    }
                    numTotal = items.size

                    adapter.setListCartItem(items)
                    binding.buttonCheckOut.text = "Thanh toán (${numTotal})"


                } else {
                    binding.emptyLayout.visibility = View.VISIBLE
                    binding.continueOrder.setOnClickListener {
                        val viewPager = (requireActivity() as MainActivity).binding.viewPager
                        viewPager.setCurrentItem(0, false)
                        (requireActivity() as MainActivity).binding.bottomNav.selectedItemId =
                            R.id.mHome
                    }
                    binding.scrollView.visibility = View.GONE
                    binding.bottom.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        setEvent()

        getAction = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        val intent = result.data

                        val addressId = intent?.getStringExtra("addressId")
                        val ref = FirebaseUtils.getAddressRef().child(userId.toString())
                            .child(addressId.toString())
                        ref.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val address = snapshot.getValue(Address::class.java)
                                    if (address != null) {
                                        addressSelected = address
                                    } else {
                                        addressSelected = null
                                    }
                                }

                                if (addressSelected != null) {
                                    binding.tvAddress.text =
                                        "${addressSelected?.name} | ${addressSelected?.phone}\n${addressSelected?.address}"
                                } else {
                                    binding.tvAddress.text = "Chọn địa chỉ nhận hàng"
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })

                        val paymentMethodId = intent?.getStringExtra("paymentMethodId")
                        val ref1 = FirebaseUtils.getPaymentRef().child(paymentMethodId.toString())
                        ref1.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val paymentMethod = snapshot.getValue(PaymentMethod::class.java)
                                    if (paymentMethod != null) {
                                        paymentMethodSelected = paymentMethod
                                    } else {
                                        paymentMethodSelected = null
                                    }
                                }

                                if (paymentMethodSelected != null) {
                                    binding.tvPaymentMethod.text = "${paymentMethodSelected?.name}"
                                } else {
                                    binding.tvPaymentMethod.text = "Chọn phương thức thanh toán"
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })

                    }
                }
            })

    }

    private fun setEvent() {
        binding.buttonCheckOut.setOnClickListener {
            order()
        }

        binding.rlAddress.setOnClickListener {
            val bundle = Bundle()
            if (addressSelected != null) {
                bundle.putString("address_id", addressSelected?.id)
            } else {
                bundle.putString("address_id", "0")
            }
            val intent = Intent(context, AddressActivity::class.java)
            intent.putExtras(bundle)
            getAction.launch(intent)
        }

        binding.rlPaymentMethod.setOnClickListener {
            val bundle = Bundle()
            if (paymentMethodSelected != null) {
                bundle.putInt("payment_method_id", paymentMethodSelected!!.id)
            } else {
                bundle.putInt("payment_method_id", 0)
            }
            val intent = Intent(context, PaymentMethodActivity::class.java)
            intent.putExtras(bundle)
            getAction.launch(intent)
        }
    }

    private fun order() {
        if (adapter.itemCount < 1) {
            Toast.makeText(context, "Vui lòng thêm món ăn vào giỏ hàng", Toast.LENGTH_SHORT).show()
            return
        }
        if (paymentMethodSelected == null) {
            Toast.makeText(context, "Hãy chọn phương thức thanh toán", Toast.LENGTH_SHORT).show()
            return
        }
        if (addressSelected == null) {
            Toast.makeText(context, "Hãy chọn địa chỉ nhận hàng", Toast.LENGTH_SHORT).show()
            return
        }

        val ref = FirebaseUtils.getOrderRef()
        val orderId = ref.child(userId.toString()).push().key
        val order = MyOrder(
            id = orderId.toString(),
            status = "Đang xác nhận",
            address = addressSelected!!,
            cartItems = adapter.getCartItemList(),
            paymentMethod = paymentMethodSelected!!,
            total = binding.totalPrice.text.toString().replace("đ", "").toDouble()
        )

        ref.child(userId.toString()).child(orderId.toString()).setValue(order)
            .addOnSuccessListener {
                Toast.makeText(context, "Đặt hàng thành công", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show()
            }

        FirebaseUtils.getCartItemRef().child(userId.toString()).removeValue()
    }

    private fun setData() {

        binding.emptyLayout.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
        binding.bottom.visibility = View.VISIBLE

        val ref = FirebaseUtils.getCartItemRef().child(userId.toString())
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val items = mutableListOf<CartItem>()
                    var count = 0
                    var numTotal = 0
                    var priceTotal = 0.0
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(CartItem::class.java)
                        if (item != null) {
                            items.add(item)
                        }
                    }
                    for (item in items) {
                        FirebaseUtils.getFoodById(item.foodId, object : FirebaseUtils.FoodCallBack {
                            override fun onFoodReceived(food: Food?) {
                                priceTotal += food?.price!! * item.quantity
                                count++
                                if (count == items.size) {
                                    binding.totalPrice.text = " đ${priceTotal}"
                                }
                            }
                        })
                    }
                    numTotal = items.size

                    adapter.setListCartItem(items)
                    binding.buttonCheckOut.text = "Thanh toán (${numTotal})"


                } else {
                    binding.emptyLayout.visibility = View.VISIBLE
                    binding.continueOrder.setOnClickListener {
                        val viewPager = (requireActivity() as MainActivity).binding.viewPager
                        viewPager.setCurrentItem(0, false)
                        (requireActivity() as MainActivity).binding.bottomNav.selectedItemId =
                            R.id.mHome
                    }
                    binding.scrollView.visibility = View.GONE
                    binding.bottom.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onItemDelete(context: Context, cartItem: CartItem) {
        val dialogBinding: CustomDialogBinding
        dialogBinding = CustomDialogBinding.inflate(LayoutInflater.from(context))

        val dialog = Dialog(context)
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
            FirebaseUtils.getCartItemRef().child(userId.toString())
                .child(cartItem.foodId.toString()).removeValue()
            dialog.dismiss()
        }
    }

    override fun onItemUpdate(cartItem: CartItem) {
        FirebaseUtils.getCartItemRef().child(userId.toString()).child(cartItem.foodId.toString())
            .setValue(cartItem)
    }

    override fun onResume() {
        super.onResume()
        setData()
    }
}