package com.example.project1763.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1762.Helper.ChangeNumberItemsListener
import com.example.project1762.Helper.ManagementCart
import com.example.project1763.Adapter.CartAdapter

import com.example.project1763.databinding.ActivityCartBinding

class CartActivity : BaseActivity() {
    private  lateinit var binding:ActivityCartBinding
    private lateinit var managementCart: ManagementCart // Μεταβλητή γισ τη διαχείριση του καλαθιού αγορών

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Αρχικοποίηση μεταβλητής
        managementCart= ManagementCart(this)

        // Κλήση των μεθόδων αρχικοποίησης
        setVariable()
        initCartList()
        calculateCart()
    }

    // Αρχικοποίηση της λίστας του καλαθιού
    private fun initCartList() {
        binding.viewCart.layoutManager=LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // Ορισμός του adapter για το RecyclerView με την λίστα των προϊόντων στο καλάθι
        binding.viewCart.adapter=CartAdapter(managementCart.getListCart(), this, object :ChangeNumberItemsListener{
            override fun onChanged() {
                // Υπολογισμός του καλαθιού όταν αλλάζει ο αριθμός των προϊόντων
                calculateCart()
            }
        })

        // Έλεγχος αν το καλάθι είναι άδειο και ενημέρωση του UI ανάλογα
        with(binding){
            emptyTxt.visibility=if(managementCart.getListCart().isEmpty()) View.VISIBLE else View.GONE
            scrollView2.visibility=if(managementCart.getListCart().isEmpty())View.GONE else View.VISIBLE
        }
    }

    // Υπολογισμός των συνολικών εξόδων του καλαθιού
    private fun calculateCart() {
        val delivery = 3.0 // Κόστος παράδοσης
        var totalFee = 0.0 // Συνολικό κόστος των προϊόντων

        // Υπολογισμός του συνολικού κόστους λαμβάνοντας υπόψη τις εκπτώσεις
        for (item in managementCart.getListCart()) {
            if (item.offer) {
                // Εφαρμογή έκπτωσης 30%
                totalFee += item.price * 0.7 * item.numberInCart
            } else {
                totalFee += item.price * item.numberInCart
            }
        }

        // Στρογγυλοποίηση του συνολικού κόστους στα δύο δεκαδικά ψηφία
        totalFee = Math.round(totalFee * 100) / 100.0

        // Υπολογισμός του συνολικού ποσού συμπεριλαμβανομένου του κόστους παράδοσης
        val total = Math.round((totalFee + delivery) * 100) / 100.0

        // Ενημέρωση του UI με τα ποσά που έχουν υπολογισθεί
        with(binding) {
            totalFeeTxt.text = "$$totalFee"
            deliveryTxt.text = "$$delivery"
            totalTxt.text = "$$total"
        }
    }

    private fun setVariable() {
        // Ορισμός του click listener για το κουμπί backBtn
        binding.backBtn.setOnClickListener {finish()}
    }
}