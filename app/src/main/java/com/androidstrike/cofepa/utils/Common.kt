package com.androidstrike.cofepa.utils

import com.androidstrike.cofepa.models.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object Common {
    lateinit var currentUser: User
    lateinit var student_department: String
    var student_name: String? = null
    var student_level: String? = null
    var feeToPayHash: HashMap<String, Int>? = null

    var database : FirebaseDatabase = FirebaseDatabase.getInstance() // todo when you have time, change all the private database references to use this one

}