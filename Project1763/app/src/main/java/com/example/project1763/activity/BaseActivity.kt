package com.example.project1763.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

open class BaseActivity : AppCompatActivity() {

    // Όταν δημιουργείται η δραστηριότητα, η onCreate εκτελείται
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Αρχικοποίηση FirebaseDatabase
        FirebaseDatabase.getInstance()

        // Θέτω τα παρακάτω flags για διαχείριση περιορισμών της διάταξης
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}