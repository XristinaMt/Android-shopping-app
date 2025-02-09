@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.project1763.activity

import android.os.Bundle
import android.view.View
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1763.Model.ItemsModel
import androidx.recyclerview.widget.GridLayoutManager
import com.example.project1763.Adapter.ProductAdapter
import com.example.project1763.databinding.ActivityFavouritesBinding

class FavouritesActivity : BaseActivity(){
    private lateinit var binding: ActivityFavouritesBinding
    private lateinit var favouritesAdapter: ProductAdapter // Adapter για την εμφάνιση των αγαπημένων προϊόντων
    private var favouritesList = mutableListOf<ItemsModel>() // Λίστα με τα αγαπημένα προϊόντα

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ανάκτηση της λίστας των αγαπημένων προϊόντων από το intent
        favouritesList = intent.getParcelableArrayListExtra("favouritesList") ?: mutableListOf()

        // Κλήση μεθόδων αρχικοποίησης
        setupRecyclerView()
        setVariable()
    }

    // Αρχικοποίηση του RecyclerView για την εμφάνιση των αγαπημένων προϊόντων
    private fun setupRecyclerView() {
        // Δημιουργία και ρύθμιση του adapter για τα αγαπημένα προϊόντα
        favouritesAdapter = ProductAdapter(favouritesList, favouritesList)
        binding.favouritesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.favouritesRecyclerView.adapter = favouritesAdapter
        binding.favouritesRecyclerView.layoutManager = GridLayoutManager(this@FavouritesActivity, 2)
        updateEmptyViewVisibility() // Ενημέρωση της ορατότητας του κειμένου που εμφανίζεται όταν η λίστα είναι κενή
    }

    private fun setVariable() {
        // Ορισμός click listener για το κουμπί backBtn
        binding.backBtn.setOnClickListener { finish() }
    }

    // Ενημέρωση της ορατότητας του κειμένου που εμφανίζεται όταν η λίστα είναι κενή
    private fun updateEmptyViewVisibility() {
        binding.emptyTxt.visibility = if (favouritesList.isEmpty()) View.VISIBLE else View.GONE
    }
}
