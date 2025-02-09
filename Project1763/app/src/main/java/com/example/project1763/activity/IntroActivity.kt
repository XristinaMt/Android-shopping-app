package com.example.project1763.activity

import android.content.Intent
import android.os.Bundle
import com.example.project1763.databinding.ActivityIntroBinding

// Η κλάση IntroActivity επεκτείνει την BaseActivity
class IntroActivity : BaseActivity() {

    // Δημιουργώ μια μεταβλητή binding για να "κρατηθεί" η αναφορά στο layout
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Αρχικοποιώ την binding με το layoutInflater
        binding = ActivityIntroBinding.inflate(layoutInflater)

        // Ορίζω το περιεχόμενο της δραστηριότητας με βάση το αρχικό στοιχείο του binding
        setContentView(binding.root)

        // Ορίζω ένα click listener για το κουμπί startBtn
        binding.startBtn.setOnClickListener{
            // Όταν πατηθεί το κουμπί, ξεκινάει η MainActivity
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
        }
    }
}