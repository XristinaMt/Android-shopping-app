package com.example.project1763.Adapter


import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.example.project1763.R
import com.example.project1763.databinding.ViewholderSizeBinding

class SizeAdapter(val items:MutableList<String>):
    RecyclerView.Adapter<SizeAdapter.Viewholder>(){

    private var selectedPosition = -1 // Τρέχουσα επιλεγμένη θέση
    private var lastSelectedPosition = -1 // Προηγούμενη επιλεγμένη θέση
    private lateinit var context: Context

    // Εσωτερική κλάση ViewHolder για τη δέσμευση των στοιχείων του layout
    inner class Viewholder(val binding: ViewholderSizeBinding):
        RecyclerView.ViewHolder(binding.root)

    // Δημιουργία ViewHolder και δέσμευση του layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeAdapter.Viewholder {
        context=parent.context
        val binding=ViewholderSizeBinding.inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }

    // Σύνδεση δεδομένων με το ViewHolder
    override fun onBindViewHolder(holder: SizeAdapter.Viewholder, position: Int) {
        holder.binding.sizeTxt.text=items[position]

        // Ρύθμιση του click listener για την επιλογή του μεγέθους
        holder.binding.root.setOnClickListener{
            lastSelectedPosition=selectedPosition // Αποθήκευση της προηγούμενης επιλεγμένης θέσης
            selectedPosition=position // Ορισμός της νέας επιλεγμένης θέσης
            notifyItemChanged(lastSelectedPosition) // Ενημέρωση της θέσης της προηγούμενης επιλογής
            notifyItemChanged(selectedPosition) // Ενημέρωση της θέσης της τρέχουσας επιλογής
        }

        // Ενημέρωση της εμφάνισης της επιλογής
        if (selectedPosition==position){
            // Εφαρμογή χρώματος και φόντου για την επιλεγμένη θέση
            holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg_selected)
            holder.binding.sizeTxt.setTextColor(context.resources.getColor(R.color.pink))
        }else{
            holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg)
            holder.binding.sizeTxt.setTextColor(context.resources.getColor(R.color.black))
        }
    }
    override fun getItemCount(): Int=items.size // Επιστρέφει τον αριθμό των στοιχείων στη λίστα
    }