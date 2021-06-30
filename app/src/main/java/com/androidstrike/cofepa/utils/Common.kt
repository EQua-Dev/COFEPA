package com.androidstrike.cofepa.utils

import com.androidstrike.cofepa.models.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object Common {
    var paymentRefHash: String? = ""
    var currentUser: User? = null
    var student_department: String? = ""
    var student_name: String = "User"
    var student_level: String? = null
    var feeToPayHash: HashMap<String, Int> = HashMap<String, Int>()
    var paymentRef: String = "${student_name}${System.currentTimeMillis()}Ref"

    var database : FirebaseDatabase = FirebaseDatabase.getInstance() // todo when you have time, change all the private database references to use this one

}