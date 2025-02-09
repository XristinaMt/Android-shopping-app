package com.example.project1763.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.project1763.Adapter.ProductAdapter
import com.example.project1763.Model.SliderModel
import com.example.project1763.Adapter.SliderAdapter
import com.example.project1763.Model.ItemsModel
import com.example.project1763.R
import com.example.project1763.ViewModel.MainViewModel
import com.example.project1763.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class MainActivity : BaseActivity() {
    private val viewModel = MainViewModel() // Δημιουργία ViewModel
    private lateinit var binding: ActivityMainBinding // Αρχικοποίηση binding για το αντίστοιχο layout
    private lateinit var adapter: ProductAdapter // Δημιουργία adapter για τα προϊόντα
    private lateinit var database: DatabaseReference // Αναφορά στη βάση δεδομένων Firebase

    // Δημιουργία RecyclerView, SearchView και Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var spinner: Spinner

    // Λίστες για τα προϊόντα και τα αγαπημένα
    private var itemsList = mutableListOf<ItemsModel>()
    private var allItemsList = mutableListOf<ItemsModel>()
    private var favouritesList = mutableListOf<ItemsModel>()

    // Μεταβλητές για την κατηγορία και το εύρος τιμών
    private var selectedCategoryId: Int? = null
    private var minPrice: Double? = null
    private var maxPrice: Double? = null

    // Αντικείμενο για τις διαθέσιμες γλώσσες
    companion object {
        private val languages = arrayOf("Select Language", "English", "Ελληνικά")
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Αρχικοποίηση spinner για την επιλογή γλώσσας
        spinner = findViewById(R.id.spinner)
        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLang = parent.getItemAtPosition(position).toString()
                when (selectedLang) {
                    "Ελληνικά" -> {
                        setLocal(this@MainActivity, "el")
                        finish()
                        startActivity(intent)
                    }
                    "English" -> {
                        setLocal(this@MainActivity, "en")
                        finish()
                        startActivity(intent)
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Αρχικοποίηση search view και database reference για τα προϊόντα
        searchView = findViewById(R.id.search)
        database = FirebaseDatabase.getInstance().getReference("Items")

        // Ορισμός click listener για το κουμπί "allProducts"
        binding.allProducts.setOnClickListener {
            displayProducts(allItemsList)  // Προβολή όλων των προϊόντων όταν πατηθεί το κουμπί
        }

        // Κλήση των μεθόδων αρχικοποίησης
        initBanner()
        initCategory()
        initProducts()
        initBottomMenu()
        fetchProducts()
        setupSearchView()
        setupFilters()
    }

    private fun fetchProducts() { // Λήψη προϊόντων από τη βάση δεδομένων
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<ItemsModel>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(ItemsModel::class.java)
                    if (product != null) {
                        products.add(product)
                    }
                }
                allItemsList = products.toMutableList() // Αποθήκευση όλων των προϊόντων στην λίστα
                displayProducts(products)
                binding.viewCart.layoutManager = GridLayoutManager(this@MainActivity, 2)
            }

            override fun onCancelled(error: DatabaseError) {
                // Διαχείριση σφαλμάτων
            }
        })
    }

    private fun displayProducts(products: MutableList<ItemsModel>){ // Προβολή προϊόντων στο RecyclerView
        itemsList = products
        recyclerView = findViewById(R.id.viewCart)
        recyclerView.layoutManager = LinearLayoutManager(this)
        binding.viewCart.layoutManager = GridLayoutManager(this@MainActivity, 2)
        // Δημιουργία adapter για τα προϊόντα, "πέρασμα" της λίστας με τα προϊόντα και της λίστας με τα αγαπημένα
        adapter = ProductAdapter(itemsList, favouritesList)
        recyclerView.adapter = adapter
    }

    // Ορισμός τοπικής γλώσσας
    private fun setLocal(activity: Activity, langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    // Αρχικοποίηση κάτω μενού
    private fun initBottomMenu() {
        // Όταν πατηθεί το κουμπί cartBtn, ο χρήστης ανακατευθύνεται στο καλάθι του
        binding.cartBtn.setOnClickListener { startActivity(Intent(this@MainActivity, CartActivity::class.java))}
        // Όταν πατηθεί το κουμπί favBtn, ο χρήστης ανακατευθύνεται στη λίστα με τα αγαπημένα του
        binding.favBtn.setOnClickListener {
            navigateToFavourites()
        }
    }

    // Αρχικοποίηση των banners
    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer { items ->
            banners(items)
            binding.progressBarBanner.visibility = View.GONE
        })
        viewModel.loadBanners()
    }

    // Προβολή των banners
    private fun banners(images: List<SliderModel>) {
        binding.viewpagerSlider.adapter = SliderAdapter(images, binding.viewpagerSlider)
        binding.viewpagerSlider.clipToPadding = false
        binding.viewpagerSlider.clipChildren = false
        binding.viewpagerSlider.offscreenPageLimit = 3
        binding.viewpagerSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewpagerSlider.setPageTransformer(compositePageTransformer)
        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewpagerSlider)
        }
    }

    // Αρχικοποίηση κατηγοριών
    private fun initCategory() {
        // Αρχικοποίηση TextViews κάθε κατηγορία
        val categoryShoes = findViewById<TextView>(R.id.categoryShoes)
        val categoryClothes = findViewById<TextView>(R.id.categoryClothes)
        val categoryGadgets = findViewById<TextView>(R.id.categoryGadgets)

        // Ορισμός click listeners για κάθε κατηγορία
        categoryShoes.setOnClickListener {
            filterProductsByCategory(0)
        }
        categoryClothes.setOnClickListener {
            filterProductsByCategory(1)
        }
        categoryGadgets.setOnClickListener {
            filterProductsByCategory(2)
        }
    }

    // Αρχικοποίηση προϊόντων
    private fun initProducts() {
        binding.progressBarProducts.visibility = View.VISIBLE

        viewModel.products.observe(this, Observer {
            allItemsList = it.toMutableList()
            // Δημιουργία adapter για τα προϊόντα, "πέρασμα" της λίστας με όλα τα προϊόντα και της λίστας με τα αγαπημένα
            adapter = ProductAdapter(allItemsList, favouritesList)
            binding.viewCart.layoutManager = GridLayoutManager(this@MainActivity, 2)
            binding.viewCart.adapter = adapter
            binding.progressBarProducts.visibility = View.GONE
        })
        viewModel.loadProducts()
    }

    // Αρχικοποίηση search view
    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    adapter.updateList(itemsList)
                } else {
                    val filteredList = itemsList.filter { item ->
                        item.title.contains(newText, ignoreCase = true) ||
                                item.description.contains(newText, ignoreCase = true)
                    }
                    adapter.updateList(filteredList.toMutableList())
                }
                return true
            }
        })
    }

    // Ανακατεύθυνση στα αγαπημένα
    private fun navigateToFavourites() {
        // Διαδικάσια προβολής λίστας των αγαπημένων
        val intent = Intent(this, FavouritesActivity::class.java)
        intent.putParcelableArrayListExtra("favouritesList", ArrayList(favouritesList))
        startActivity(intent)
    }

    // Αρχικοποίηση φίλτρων
    private fun setupFilters() {
        val priceSeekBar: SeekBar = findViewById(R.id.priceSeekBar)
        val minPriceText: TextView = findViewById(R.id.minPriceText) // TextView για την εμφάνιση της ελάχιστης τιμής
        val maxPriceText: TextView = findViewById(R.id.maxPriceText) // TextView για την εμφάνιση της μέγιστης τιμής

        // listener για το price seekbar
        priceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Υπολογισμός ελάχιστης και μέγιστης τιμής ανάλογα με την πρόοδο του seekbar
                minPrice = progress.toDouble()
                maxPrice = (progress + 30).toDouble() // το εύρος τιμών μεταβάλεται ανά 30 μονάδες

                // Εμφάνιση του επιλεγμένου εύρους τιμών
                minPriceText.text = "$minPrice" // Ενημέρωση του TextView με την ελάχιστη τιμή
                maxPriceText.text = "$maxPrice" // Ενημέρωση του TextView με την μέγιστη τιμή
                binding.const2.visibility = View.VISIBLE
                applyFilters(selectedCategoryId, minPrice, maxPrice)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    // Εφαρμογή φίλτρων βάσει κατηγορίας και εύρους τιμών
    private fun applyFilters(categoryId: Int?, minPrice: Double?, maxPrice: Double?) {
        var filteredProducts = allItemsList.toMutableList()

        // Φιλτράρισμα βάσει κατηγορίας
        if (categoryId != null) {
            filteredProducts = filteredProducts.filter { it.categoryId == categoryId }.toMutableList()
        }

        // Φιλτράρισμα βάσει εύρους τιμών
        if (minPrice != null && maxPrice != null) {
            filteredProducts = filteredProducts.filter { it.price in minPrice..maxPrice }.toMutableList()
        }

        // Ενημέρωση του RecyclerView με τα φιλτραρισμένα προϊόντα
        displayProducts(filteredProducts)
    }

    // Φιλτράρισμα προϊόντων βάσει κατηγορίας
    private fun filterProductsByCategory(categoryId: Int) {
        val filteredList = allItemsList.filter { it.categoryId == categoryId }.toMutableList()
        displayProducts(filteredList)
    }
}