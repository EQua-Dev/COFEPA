package com.androidstrike.cofepa.models

class User {

    lateinit var name:String
    var email: String? = null
    lateinit var level: String
    lateinit var department: String
    lateinit var phone: String

    constructor(){

    }



    constructor(uid: String, email: String?)
    constructor(name: String, email: String?, level: String, department: String, phone: String) {
        this.name = name
        this.email = email
        this.level = level
        this.department = department
        this.phone = phone
    }

}