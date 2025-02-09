package com.example.project1763.Model

import android.os.Parcel
import android.os.Parcelable

data class ItemsModel(
    var title: String = "",
    var description: String = "",
    var picUrl: ArrayList<String> = ArrayList(),
    var size: ArrayList<String> = ArrayList(),
    var price: Double = 0.0,
    var rating: Float = 0.0f,
    val offer: Boolean = false,
    var numberInCart: Int = 0,
    var categoryId: Int = 0
):Parcelable{ // Διασύνδεση η οποία επιτρέπει την αποθήκευση και ανάκτηση ενός προϊόντος
    constructor(parcel: Parcel):this( //Επαναφέρει τα πεδία ώστε να μπορούν να ξαναδημιουργηθούν τα αντικείμενα
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.readDouble(),
        parcel.readFloat(),
        parcel.readByte() != 0.toByte()
    )

    override fun describeContents(): Int { // Επιστρέφει 0 καθώς δεν χρησιμοποιείται ειδικό περιεχόμενο
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) { // Εγγραφή των πεδίων του αντικειμένου στο Parcel
        dest.writeString(title)
        dest.writeString(description)
        dest.writeStringList(picUrl)
        dest.writeStringList(size)
        dest.writeDouble(price)
        dest.writeFloat(rating)
        dest.writeByte(if (offer) 1 else 0)
    }

    // Μέθοδος για τη δημιουργία ενός πίνακα αντικειμένων ItemsModel
    companion object CREATOR : Parcelable.Creator<ItemsModel> { // Δημιουργεί ένα νέο αντικείμενο ItemsModel από ένα Parcel
        override fun createFromParcel(parcel: Parcel): ItemsModel { // Μέθοδος για τη δημιουργία ενός πίνακα αντικειμένων ItemsModel
            return ItemsModel(parcel)
        }
        override fun newArray(size: Int): Array<ItemsModel?> {
            return arrayOfNulls(size)
        }
    }
}
