package com.example.project1763.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.project1762.Helper.ChangeNumberItemsListener
import com.example.project1762.Helper.ManagementCart
import com.example.project1763.Model.ItemsModel
import com.example.project1763.databinding.ViewholderCartBinding


class CartAdapter (private  val listItemSelected:ArrayList<ItemsModel>, // Λίστα με τα επιλεγμένα προϊόντα
    context: Context,
    var changeNumberItemsListener: ChangeNumberItemsListener?=null // Listener για την αλλαγή του αριθμού των προϊόντων
    ):RecyclerView.Adapter<CartAdapter.ViewHolder>(){

    // ViewHolder για τη δέσμευση των στοιχείων του καλαθιού
    class ViewHolder(val binding: ViewholderCartBinding):RecyclerView.ViewHolder(binding.root)

    private val managementCart = ManagementCart(context)

    // Δημιουργία ViewHolder και δέσμευση του layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val binding=ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return  ViewHolder(binding)
    }

    // Σύνδεση δεδομένων με το ViewHolder
    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val item=listItemSelected[position]
        if (item.offer) { // εάν το προϊόν είναι σε έκπτωση, εμφανίζεται η νέα εκπτωτική τιμή
            val discountedPrice = item.price * 0.7 // Υπολογισμός της έκπτωσης 30%
            holder.binding.titleTxt.text = item.title
            holder.binding.feeEachItem.text = "$${item.price}"
            holder.binding.totalEachItem.text = "$${Math.round(item.numberInCart * discountedPrice)}"
            holder.binding.discMessage.visibility = View.VISIBLE
            holder.binding.numberItemTxt.text = item.numberInCart.toString()

            // Φόρτωση της εικόνας του προϊόντος
            Glide.with(holder.itemView.context)
                .load(item.picUrl[0])
                .apply(RequestOptions().transform(CenterCrop()))
                .into(holder.binding.pic)
        } else {
            holder.binding.titleTxt.text = item.title
            holder.binding.feeEachItem.text = "$${item.price}"
            holder.binding.totalEachItem.text = "$${Math.round(item.numberInCart * item.price)}"
            holder.binding.numberItemTxt.text = item.numberInCart.toString()

            Glide.with(holder.itemView.context)
                .load(item.picUrl[0])
                .apply(RequestOptions().transform(CenterCrop()))
                .into(holder.binding.pic)
        }

        // Διαχείριση αύξησης του αριθμού των προϊόντων
        holder.binding.plusCartBtn.setOnClickListener{
            managementCart.plusItem(listItemSelected,position,object :ChangeNumberItemsListener{
                override fun onChanged() {
                    notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                }
            })
        }
        // Διαχείριση μείωσης του αριθμού των προϊόντων
        holder.binding.minusCartBtn.setOnClickListener{
            managementCart.minusItem(listItemSelected,position,object :ChangeNumberItemsListener{
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }
    }
    // Επιστροφή του αριθμού των προϊόντων στη λίστα
    override fun getItemCount(): Int=listItemSelected.size
}