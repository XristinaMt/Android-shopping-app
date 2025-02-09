package com.example.project1763.Adapter


import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project1763.R

import com.example.project1763.databinding.ViewholderPhotoBinding

class PhotoAdapter(val items:MutableList<String>,private val listener: OnItemClickListener):
    RecyclerView.Adapter<PhotoAdapter.Viewholder>(){

    // Αρχικοποιούμε τις μεταβλητές για την επιλογή της θέσης
    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    private lateinit var context: Context

    // Ορισμός του interface για το click listener
    interface OnItemClickListener {
        fun onItemClick(photoUrl: String)
    }

    // Εσωτερική κλάση ViewHolder για τη δέσμευση των στοιχείων του layout
    inner class Viewholder(val binding: ViewholderPhotoBinding):
        RecyclerView.ViewHolder(binding.root)

    // Δημιουργία ViewHolder και δέσμευση του layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoAdapter.Viewholder {
        context=parent.context
        val binding= ViewholderPhotoBinding.inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }

    // Σύνδεση δεδομένων με το ViewHolder
    override fun onBindViewHolder(
        holder: PhotoAdapter.Viewholder, position: Int) {

        // Φόρτωση της εικόνας
        Glide.with(holder.itemView.context)
            .load(items[position])
            .into(holder.binding.pic)

        // Διαχείριση του click listener για την επιλογή της εικόνας
        holder.binding.root.setOnClickListener{
            lastSelectedPosition=selectedPosition
            selectedPosition=position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
            listener.onItemClick(items[position])
        }

        // Διαχείριση του οπτικού στοιχείου
        if (selectedPosition==position){
            holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg_selected)
        }else{
            holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg)
        }
    }
    override fun getItemCount(): Int=items.size // Επιστρέφει τον αριθμό των στοιχείων στη λίστα
    }