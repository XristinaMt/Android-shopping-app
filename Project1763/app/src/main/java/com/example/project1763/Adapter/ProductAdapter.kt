package com.example.project1763.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.project1763.Model.ItemsModel

import com.example.project1763.activity.DetailActivity
import com.example.project1763.databinding.ViewholderProductsBinding

class ProductAdapter(var items:MutableList<ItemsModel>,
    private val favouritesList: MutableList<ItemsModel>): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private var context: Context? = null

    // Εσωτερική κλάση ViewHolder για τη δέσμευση των στοιχείων του layout
    class ViewHolder(val binding: ViewholderProductsBinding) : RecyclerView.ViewHolder(binding.root)

    // Δημιουργία ViewHolder και δέσμευση του layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        context=parent.context
        val binding=ViewholderProductsBinding.inflate(LayoutInflater.from(context),parent, false)
        return ViewHolder(binding)
    }

    // Σύνδεση δεδομένων με το ViewHolder
    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {

        val product = items[position]

        holder.binding.titleTxt.text = items[position].title

        if (product.offer) { // Eάν το προϊόν είναι σε έκπτωση, yπολογισμός και εμφάνιση τιμής με έκπτωση
            val discountedPrice = product.price * 0.7 // 30% discount
            holder.binding.priceTxt.text = "$${product.price}"
            holder.binding.productDiscountedPrice.text = "$${String.format("%.2f", discountedPrice)}"
            holder.binding.productDiscountedPrice.visibility = View.VISIBLE
            holder.binding.OnDiscount.visibility = View.VISIBLE

            // Eμφάνιση της εικόνας του προϊόντος
            val requestOptions = RequestOptions().transform(CenterCrop())
            Glide.with(holder.itemView.context)
                .load(items[position].picUrl[0])
                .apply(requestOptions)
                .into(holder.binding.pic)

            // Ορισμός του click listener για την εμφάνιση λεπτομερειών του προϊόντος
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.putExtra("object", items[position])
                holder.itemView.context.startActivity(intent)
            }
        } else { // Εμφάνιση του προϊόντος χωρίς έκπτωση
            holder.binding.titleTxt.text = items[position].title
            holder.binding.priceTxt.text = "$" + items[position].price.toString()
            holder.binding.ratingTxt.text = items[position].rating.toString()
            holder.binding.productDiscountedPrice.visibility = View.GONE
            holder.binding.OnDiscount.visibility = View.GONE

            val requestOptions = RequestOptions().transform(CenterCrop())
            Glide.with(holder.itemView.context)
                .load(items[position].picUrl[0])
                .apply(requestOptions)
                .into(holder.binding.pic)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.putExtra("object", items[position])
                holder.itemView.context.startActivity(intent)
            }
        }

        // Ρύθμιση του checkbox για τα αγαπημένα προϊόντα
        holder.binding.cbHeart.setOnCheckedChangeListener(null)
        holder.binding.cbHeart.isChecked = favouritesList.contains(product)
        holder.binding.cbHeart.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) { // Προσθήκη προϊόντος στη λίστα αγαπημένων
                favouritesList.add(product)
            } else { // Αφαίρεση προϊόντος από τη λίστα αγαπημένων
                favouritesList.remove(product)
            }
        }
    }

    override fun getItemCount(): Int = items.size // Επιστρέφει τον αριθμό των στοιχείων στη λίστα

    // Ενημέρωση της λίστας προϊόντων
    fun updateList(newList: MutableList<ItemsModel>) {
        items = newList
        notifyDataSetChanged()
    }
}