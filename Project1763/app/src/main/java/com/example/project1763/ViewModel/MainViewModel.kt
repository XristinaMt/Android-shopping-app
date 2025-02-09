package com.example.project1763.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project1763.Model.ItemsModel
import com.example.project1763.Model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel() : ViewModel() {

    // Αναφορά στη βάση δεδομένων Firebase
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _banner = MutableLiveData<List<SliderModel>>() // Αποθήκευση της λίστας με τα SliderModel
    private val _products = MutableLiveData<MutableList<ItemsModel>>() // Αποθήκευση της λίστας με τα ItemsModel

    // Μεταβλητή που παρέχει πρόσβαση στη λίστα των SliderModel που αποθηκεύεται στο _banner
    val banners: LiveData<List<SliderModel>> = _banner

    // Μεταβλητή που παρέχει πρόσβαση στη λίστα των ItemsModel που αποθηκεύεται στο _products
    val products: LiveData<MutableList<ItemsModel>> = _products

    fun loadBanners(){ // Φόρτωση δεδομένων από το Firebase στον κόμβο "Banner" και ενημέρωση του _banner
        val ref=firebaseDatabase.getReference("Banner")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) { // Όταν τα δεδομένα αλλάζουν, ενημερώνει το banners με τα νέα δεδομένα
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _banner.value = lists
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun loadProducts(){ // Φόρτωση δεδομένων από το Firebase στον κόμβο "Items" και ενημέρωση του _products
        val ref=firebaseDatabase.getReference("Items")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) { // Όταν τα δεδομένα αλλάζουν, ενημερώνει το products με τα νέα δεδομένα
                val lists = mutableListOf<ItemsModel>()
                for(childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if(list != null) {
                        lists.add(list)
                    }
                }
                _products.value=lists
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}