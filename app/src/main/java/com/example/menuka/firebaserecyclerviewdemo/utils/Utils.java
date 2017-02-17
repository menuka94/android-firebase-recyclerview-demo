package com.example.menuka.firebaserecyclerviewdemo.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by menuka on 2/17/17.
 */

public class Utils {
    public static String getUID(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.POSTS).push();
        String[] urlArray = databaseReference.toString().split("/");
        return urlArray[urlArray.length - 1];
    }
}
