package com.example.project1762.Helper

import android.content.Context
import android.widget.Toast
import com.example.project1763.Helper.TinyDB
import com.example.project1763.Model.ItemsModel

class ManagementCart(val context: Context) {

    // Χρήση της βιβλιοθήκης TinyDB για αποθήκευση δεδομένων
    private val tinyDB = TinyDB(context)

    fun insertItems(item: ItemsModel) { // Εισαγωγή ενός προϊόντος στο καλάθι
        var listItems = getListCart() // Λαμβάνεται την τρέχουσα λίστα από το καλάθι
        val existAlready = listItems.any { it.title == item.title } // Έλεγχος αν το προϊόν υπάρχει ήδη στη λίστα
        val index = listItems.indexOfFirst { it.title == item.title } // Εντοπισμός της θέσης του προϊόντος στη λίστα αν υπάρχει

        if (existAlready) { // Αν υπάρχει, ανανεώνεται ο αριθμός του προϊόντος
            listItems[index].numberInCart = item.numberInCart
        } else { // Αν δεν υπάρχει, προστίθεται το νέο προϊόν στη λίστα
            listItems.add(item)
        }

        // Αποθηκεύει τη λίστα στην CartList του TinyDB
        tinyDB.putListObject("CartList", listItems)

        // Εμφανίζεται μήνυμα ότι το προϊόν προστέθηκε στο καλάθι
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()
    }

    // Επιστροφή της λίστας των προϊόντων του καλαθιού
    fun getListCart(): ArrayList<ItemsModel> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    // Μείωση του αριθμού του προϊόντος στο καλάθι
    fun minusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (listItems[position].numberInCart == 1) {
            // Αν υπάχει ένα τεμάχιο του προϊόντος, αφαιρείται το προϊόν από τη λίστα
            listItems.removeAt(position)
        } else {
            // Αν ο αριθμός των τεμαχίων είναι μεγαλύτερος από 1, απλά μειώνεται ο αριθμός
            listItems[position].numberInCart--
        }
        // Αποθηκεύεται η νέα κατάσταση της λίστας στο TinyDB
        tinyDB.putListObject("CartList", listItems)
        // Ενημερώνεται ο listener για την αλλαγή
        listener.onChanged()
    }

    // Αύξηση του αριθμού του προϊόντος στο καλάθι
    fun plusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        listItems[position].numberInCart++ // Αυξάνεται ο αριθμός του προϊόντος
        tinyDB.putListObject("CartList", listItems) // Αποθηκεύεται η νέα κατάσταση της λίστας στο TinyDB
        listener.onChanged() // Ενημερώνεται ο listener για την αλλαγή
    }
}