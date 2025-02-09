package com.example.project1763.Adapter

import android.view.ViewGroup
import android.view.View
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions
import com.example.project1763.Model.SliderModel
import com.example.project1763.R

class SliderAdapter(
    private var sliderItems:List<SliderModel>, // Λίστα με τα στοιχεία slider
    private val viewPager2: ViewPager2) // ViewPager2 για τη διαχείριση του slider
    :RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private lateinit var context:Context
    private val runnable = Runnable { // Runnable που ενημερώνει το dataset και ανανεώνει τον adapter
        sliderItems = sliderItems
        notifyDataSetChanged()
    }

    // Δημιουργία του ViewHolder για το στοιχείο του slider
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.slider_item_container, parent, false)
        return SliderViewHolder(view)
    }

    // Σύνδεση των δεδομένων με το ViewHolder
    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.setImage(sliderItems[position], context)

        // Αυτόματα προχωράμε στην επόμενη σελίδα όταν φτάσουμε στην τελευταία
        if(position == sliderItems.lastIndex - 1) {
            viewPager2.post(runnable)
        }
    }
    override fun getItemCount(): Int = sliderItems.size // Επιστρέφει τον αριθμό των στοιχείων στο adapter

    // Εσωτερική κλάση ViewHolder για τη δέσμευση του layout του slider
    class SliderViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        private val imageView: ImageView = itemView.findViewById(R.id.imageSlide)

        // Μέθοδος για τη ρύθμιση της εικόνας του slider
        fun setImage(sliderItems: SliderModel, context: Context) {
            val requestOptions = RequestOptions().transform(CenterInside())

            // Φόρτωση εικόνας
            Glide.with(context)
                .load(sliderItems.url)
                .apply(requestOptions)
                .into(imageView)
        }
    }
}