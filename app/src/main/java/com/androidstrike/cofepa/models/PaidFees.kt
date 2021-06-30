package com.androidstrike.cofepa.models

class PaidFees {

    var dueTotal: String? = null
    var paidTotal: String? = null
    var owingTotal: String? = null
    var paymentRef: String? = ""
    var level: String? = ""
    var semester: String= ""
    var isVerified: Boolean? = null

    constructor()
    constructor(
        dueTotal: String?,
        paidTotal: String?,
        owingTotal: String?,
        paymentRef: String?,
        level: String?,
        semester: String,
        isVerified: Boolean?
    ) {
        this.dueTotal = dueTotal
        this.paidTotal = paidTotal
        this.owingTotal = owingTotal
        this.paymentRef = paymentRef
        this.level = level
        this.semester = semester
        this.isVerified = isVerified
    }


}