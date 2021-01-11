package com.androidstrike.cofepa.models

class User {

    lateinit var name:String
    lateinit var email: String
    lateinit var department: String
    lateinit var phone: String

    constructor(){

    }

    constructor(name: String, email: String, department: String, phone: String) {
        this.name = name
        this.email = email
        this.department = department
        this.phone = phone
    }

}