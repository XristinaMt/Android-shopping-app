@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.project1763.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1762.Helper.ManagementCart
import com.example.project1763.Adapter.PhotoAdapter
import com.example.project1763.Adapter.SizeAdapter
import com.example.project1763.Adapter.SliderAdapter
import com.example.project1763.Model.ItemsModel
import com.example.project1763.Model.SliderModel
import com.example.project1763.databinding.ActivityDetailBinding
import android.app.AlertDialog

class DetailActivity : BaseActivity(), PhotoAdapter.OnItemClickListener {
    private lateinit var binding:ActivityDetailBinding
    private lateinit var item:ItemsModel // Μεταβλητή για τη διαχείριση του προϊόντος
    private var numberOrder=1
    private lateinit var managementCart: ManagementCart // Μεταβλητή γισ τη διαχείριση του καλαθιού αγορών

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart=ManagementCart(this)

        // Κλήση των μεθόδων αρχικοποίησης
        getBundle()
        banners()
        initLists()

        // Ορισμός click listener για το κουμπί διαθεσιμότητας
        binding.availability.setOnClickListener{
            AlertDialog.Builder( this)
                .setMessage("It is in stock!")
                .create()
                .show()
        }
    }

    // Αρχικοποίηση των λιστών για τα μεγέθη και τις φωτογραφίες
    private fun initLists() {
        // Δημιουργία του adapter για τη λίστα με τα μεγέθη
        val sizeList=ArrayList<String>()
        for (size in item.size) {
            sizeList.add(size.toString())
        }
        binding.sizeList.adapter=SizeAdapter(sizeList)
        binding.sizeList.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)

        // Δημιουργία του adapter για τη λίστα με τις φωτογραφίες
        val photoList = ArrayList<String>()
        for (imageUrl in item.picUrl) {
            photoList.add(imageUrl)
        }
        binding.photoList.adapter =PhotoAdapter(photoList, this)
        binding.photoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    // Αρχικοποίηση του slider με τις φωτογραφίες του προϊόντος
    private fun banners() {
        val sliderItems=ArrayList<SliderModel>()
        for (imageUrl in item.picUrl) {
            sliderItems.add(SliderModel(imageUrl))
        }
        binding.slider.adapter=SliderAdapter(sliderItems,binding.slider)
        binding.slider.clipToPadding = true
        binding.slider.clipChildren = true
        binding.slider.offscreenPageLimit = 1

        // Εμφάνιση των dot indicators αν υπάρχουν παραπάνω από μία εικόνες
        if(sliderItems.size>1) {
            binding.dotIndicator.visibility=View.VISIBLE
            binding.dotIndicator.attachTo(binding.slider)
        }
    }

    // Λήψη των δεδομένων του προϊόντος μέσω intent και ενημέρωση του UI
    private fun getBundle() {
        item = intent.getParcelableExtra("object")!!
        binding.titleTxt.text = item.title
        binding.descriptionTxt.text = item.description
        binding.priceTxt.text = "$" + item.price
        if (item.offer) { // Εάν το προϊόν βρίσκεται σε έκπτωση, εμφάνισε τη νέα εκπτωτική τιμή
            val discountedPrice = item.price * 0.7
            binding.productDiscountedPrice.text = "$" + String.format("%.2f", discountedPrice)
            binding.productDiscountedPrice.visibility = View.VISIBLE
            binding.OnDiscount.visibility = View.VISIBLE
        } else {
            binding.productDiscountedPrice.visibility = View.GONE
            binding.OnDiscount.visibility = View.GONE
        }
        binding.ratingTxt.text = "${item.rating} Rating"

        // Ορισμός click listener για το κουμπί addToCartBtn
        binding.addToCartBtn.setOnClickListener {
            item.numberInCart = numberOrder
            managementCart.insertItems(item)
        }

        // Ορισμός click listener για το κουμπί backBtn
        binding.backBtn.setOnClickListener { finish() }

        // Ορισμός click listener για το κουμπί cartBtn, και ανακατεύθυνση του χρήστη στο καλάθι του
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this@DetailActivity, CartActivity::class.java))
        }
    }

    // Όταν πατηθεί μια φωτογραφία, επιλέγεται η εικόνα αυτή στο slider
    override fun onItemClick(photoUrl: String) {
        val position = item.picUrl.indexOf(photoUrl)
        if (position != -1) {
            binding.slider.currentItem = position
        }
    }
}