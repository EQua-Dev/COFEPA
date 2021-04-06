package com.androidstrike.cofepa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.utils.toast
import kotlinx.android.synthetic.main.custom_pre_payment.*

class CustomPrePaymentDialog: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_design)
        return inflater.inflate(R.layout.custom_pre_payment, container,false)

//        btn_pre_pay_confirm.setOnClickListener {
//            activity?.toast("Confirm Clicked!")
//        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}