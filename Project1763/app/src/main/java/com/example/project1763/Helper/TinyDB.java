package com.example.project1763.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.example.project1763.Model.ItemsModel;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;

public class TinyDB {
    private SharedPreferences preferences;

    public TinyDB(Context appContext) {
        // Αρχικοποίηση SharedPreferences για αποθήκευση και ανάκτηση των δεδομένων
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }
    /**
     * Αποθηκεύει μια λίστα από String στο SharedPreferences με το δοθέν 'key'
     * @param key Κλειδί στο SharedPreferences
     */

    public ArrayList<ItemsModel> getListObject(String key) {
        Gson gson = new Gson();

        // Λαμβάνει τη λίστα των αντικειμένων ως String
        ArrayList<String> objStrings = getListString(key);
        ArrayList<ItemsModel> playerList = new ArrayList<ItemsModel>();

        for (String jObjString : objStrings) { // Μετατρέπει κάθε String σε ItemsModel και το προσθέτει στη λίστα
            ItemsModel player = gson.fromJson(jObjString, ItemsModel.class);
            playerList.add(player);
        }
        return playerList;
    }
    /**
     * Επιστρέφει μια λίστα από ItemsModel από το SharedPreferences με το δοθέν 'key'
     * @param key Κλειδί στο SharedPreferences
     * @param stringList Λίστα από String προς αποθήκευση
     */

    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }
    /**
     * Αποθηκεύει μια λίστα από String στο SharedPreferences με το δοθέν 'key'
     * @param key Κλειδί στο SharedPreferences
     */

    public void putListObject(String key, ArrayList<ItemsModel> playerList) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for (ItemsModel player : playerList) {
            objStrings.add(gson.toJson(player));
        }
        putListString(key, objStrings);
    }
    /**
     * Επιστρέφει μια λίστα από ItemsModel από το SharedPreferences με το δοθέν 'key'
     * @param key Κλειδί στο SharedPreferences
     */

    private void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
    }
    /**
     * Ελέγχει αν το κλειδί είναι null και προκαλεί NullPointerException αν είναι
     * @param key Το κλειδί για έλεγχο
     */
}