package com.androidstrike.cofepa.models

class PaidFees {

    var dueTotal: String? = null
    var paidTotal: String? = null
    var owingTotal: String? = null

    constructor()
    constructor(dueTotal: String?, paidTotal: String?, owingTotal: String?) {
        this.dueTotal = dueTotal
        this.paidTotal = paidTotal
        this.owingTotal = owingTotal
    }


}