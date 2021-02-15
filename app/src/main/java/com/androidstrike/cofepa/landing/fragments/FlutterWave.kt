package com.androidstrike.cofepa.landing.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.models.PaidFees
import com.androidstrike.cofepa.utils.Common

import com.androidstrike.cofepa.utils.toast
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_flutter_wave.*

class FlutterWave : Fragment() {

    /**
     * Test Mastercard details for mock
    Card number: 5531 8866 5214 2950
    cvv: 564
    Expiry: 09/32
    Pin: 3310
    OTP: 12345
     **/

    var feesAmount: Int? = null
    var payingSemester: String? = null
    var totalDue: String? = null
    var owingTotal: Double? = null
    val publicKey = "FLWPUBK-2d6ccddd437ac5c89668bb3116f33ed5-X"
    val encrypyKey = "884f53bff48591fe29facb22"

    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var table_fees: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flutter_wave, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments?.getInt("totalFee") != null) {
            feesAmount = arguments?.getInt("totalFee")!!
            payingSemester = arguments?.getString("payingSemester")
            totalDue = arguments?.getString("totalDue")
        }

        owingTotal = totalDue!!.toDouble() - feesAmount!!.toDouble()

//        tvtotal.text = feesAmount.toString()


//        here we initialize the instance of the Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val user = mAuth.currentUser?.uid
        table_fees = database.getReference().child("Users/$user/Fees/${Common.student_level}/${payingSemester} Semester ")

        makePayment()

    }

    private fun makePayment() {
        RaveUiManager(this)
            .setAmount(feesAmount?.toDouble()!!)
            .setEmail(Common.currentUser.email)
            .setCountry("NG")
            .setCurrency("NGN").setfName(Common.student_name).setlName(Common.student_name)
            .setNarration("Fees Payment")
            .setPublicKey(publicKey)
            .setEncryptionKey(encrypyKey)
            .setTxRef("${System.currentTimeMillis()} Ref ")
            .acceptAccountPayments(true)
            .acceptCardPayments(true)
            //.isPreAuth(true) this is used if you want to charge the customer but not withdraw it immediately, eg uber
            //.setSubAccounts() takes in list as parameter, it is used if you want to pay the charged fee into 2 accounts
            //.setPaymentPlan() used for subscription basis
            .onStagingEnv(true)
            .shouldDisplayFee(true)
            .showStagingLabel(true)
            .initialize()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === RaveConstants.RAVE_REQUEST_CODE && data != null) {
            val message: String = data.getStringExtra("response").toString() //returns the entire raw data of the transaction
            Log.d("EQUA", "onActivityResult: $message")
            if (resultCode === RavePayActivity.RESULT_SUCCESS) {
                activity?.toast("SUCCESS $message")

                val newFees = PaidFees("N${totalDue.toString()}", "N${feesAmount.toString()}", "N${owingTotal.toString()}")
                table_fees.setValue(newFees)

                val frag_rave = Verify()

                val manager = fragmentManager

                val frag_transaction = manager?.beginTransaction()

                frag_transaction?.replace(R.id.fragment_container, frag_rave)
                frag_transaction?.commit()

            } else if (resultCode === RavePayActivity.RESULT_ERROR) {
                activity?.toast("ERROR $message")

            } else if (resultCode === RavePayActivity.RESULT_CANCELLED) {
                activity?.toast("CANCELLED $message")

            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
}