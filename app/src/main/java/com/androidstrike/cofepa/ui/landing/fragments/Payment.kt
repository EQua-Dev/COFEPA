package com.androidstrike.cofepa.ui.landing.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.adapters.FeesHashAdapter
import com.androidstrike.cofepa.models.Fees
import com.androidstrike.cofepa.models.User
import com.androidstrike.cofepa.ui.CustomPrePaymentDialog
import com.androidstrike.cofepa.utils.Common
import com.androidstrike.cofepa.utils.Common.feeToPayHash
import com.androidstrike.cofepa.utils.toast
import com.androidstrike.cofepa.viewholders.FeesViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.custom_pre_payment.*
import kotlinx.android.synthetic.main.fragment_payment.*
import java.lang.StringBuilder
import java.text.NumberFormat
import java.util.*

class Payment : Fragment(), View.OnClickListener {


    var adapter: FirebaseRecyclerAdapter<User, FeesViewHolder>? = null

    lateinit var database: FirebaseDatabase
    lateinit var query: DatabaseReference

    var feesModel: Fees? = null
    var checkedItemTotal = 0

     var acceptanceFee: Int? = null
    var accommodationFee: Int? = null
    var examFee: Int? = null
    var hazardFee: Int? = null
    var libEquipFee: Int? = null
    var medicFee: Int? = null
    var psaFee: Int? = null
    var portalFee: Int? = null
    var securityFee: Int? = null
    var sportsFee: Int? = null
    var tuitionFee: Int? = null
    var dueTotal: String? = null

    var cstmTitle: String? = null

    //    lateinit var iFirebaseLoadDone: IFirebaseLoadDone
    lateinit var selectedSemester: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        feeToPayHash.clear()
        showSemesterChooseAlert()

        tv_stud_name.text = Common.student_name
        tv_semester.text =  Common.student_department //"Economics"


    }

    private fun loadFeesCost() {
        database = FirebaseDatabase.getInstance()
//        query = database
//            .getReference("Fees/${Common.student_department}/$selectedSemester")

        query = database
            .getReference().child("Fees/${Common.student_department}/$selectedSemester")


        val feesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                feesModel = snapshot.value(Fees())
                feesModel = snapshot.getValue(Fees::class.java)

                pb_payment.visibility = View.GONE

                if (selectedSemester == "Second") {
                    layout_acceptance.visibility = View.GONE
                    layout_exam.visibility = View.GONE
                    layout_hazard.visibility = View.GONE
                    layout_medic.visibility = View.GONE
                    layout_security.visibility = View.GONE
                    layout_sports.visibility = View.GONE
                    tv_fee_accommodation.text = feesModel!!.accommodation
                    tv_fee_lib_equip.text = feesModel?.libraryEquipment
                    tv_fee_psa.text = feesModel?.psa
                    tv_fee_portal.text = feesModel?.portal
                    tv_due_total.text = feesModel?.total
                    tv_fee_tuition.text = feesModel?.tuition

                    dueTotal = tv_due_total.text.toString()

                    accommodationFee = tv_fee_accommodation.text.toString().toInt()
                    libEquipFee = tv_fee_lib_equip.text.toString().toInt()
                    psaFee = tv_fee_psa.text.toString().toInt()
                    portalFee = tv_fee_portal.text.toString().toInt()
                    tuitionFee = tv_fee_tuition.text.toString().toInt()


                } else {

                    tv_fee_acceptance.text = feesModel?.acceptance
                    tv_fee_accommodation.text = feesModel?.accommodation
                    tv_fee_exam.text = feesModel?.exams
                    tv_fee_hazard.text = feesModel?.hazard
                    tv_fee_lib_equip.text = feesModel?.libraryEquipment
                    tv_fee_medic.text = feesModel?.medicalTreatment
                    tv_fee_psa.text = feesModel?.psa
                    tv_fee_portal.text = feesModel?.portal
                    tv_fee_security.text = feesModel?.security
                    tv_fee_sports.text = feesModel?.sports
                    tv_due_total.text = feesModel?.total
                    tv_fee_tuition.text = feesModel?.tuition

                    dueTotal = tv_due_total.text.toString()

                    acceptanceFee = tv_fee_acceptance.text.toString().toInt()
                    accommodationFee = tv_fee_accommodation.text.toString().toInt()
                    examFee = tv_fee_exam.text.toString().toInt()
                    hazardFee = tv_fee_hazard.text.toString().toInt()
                    libEquipFee = tv_fee_lib_equip.text.toString().toInt()
                    medicFee = tv_fee_medic.text.toString().toInt()
                    psaFee = tv_fee_psa.text.toString().toInt()
                    portalFee = tv_fee_portal.text.toString().toInt()
                    securityFee = tv_fee_security.text.toString().toInt()
                    sportsFee = tv_fee_sports.text.toString().toInt()
                    tuitionFee = tv_fee_tuition.text.toString().toInt()


                }

            }

            override fun onCancelled(error: DatabaseError) {
                activity?.toast(error.message)
            }

        }
        query.addListenerForSingleValueEvent(feesListener)

//        showCheckedTotal()
        Log.d("EQUA", "showCheckedTotal: called")

        layout_acceptance.setOnClickListener(this)
        layout_accommodation.setOnClickListener(this)
        layout_exam.setOnClickListener(this)
        layout_hazard.setOnClickListener(this)
        layout_lib_equip.setOnClickListener(this)
        layout_medic.setOnClickListener(this)
        layout_psa.setOnClickListener(this)
        layout_portal.setOnClickListener(this)
        layout_security.setOnClickListener(this)
        layout_sports.setOnClickListener(this)
        layout_tuition.setOnClickListener(this)


    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onClick(v: View?) {
//        var acceptanceFee: Int? = tv_fee_acceptance.text.toString().toInt()
//        var accommodationFee: Int? = tv_fee_accommodation.text.toString().toInt()
//        var examFee: Int? = tv_fee_exam.text.toString().toInt()
//        var hazardFee: Int? = tv_fee_hazard.text.toString().toInt()
//        var libEquipFee: Int? = tv_fee_lib_equip.text.toString().toInt()
//        var medicFee: Int? = tv_fee_medic.text.toString().toInt()
//        var psaFee: Int? = tv_fee_psa.text.toString().toInt()
//        var portalFee: Int? = tv_fee_portal.text.toString().toInt()
//        var securityFee: Int? = tv_fee_security.text.toString().toInt()
//        var sportsFee: Int? = tv_fee_sports.text.toString().toInt()
//        var tuitionFee: Int? = tv_fee_tuition.text.toString().toInt()

        when (v) {
            layout_acceptance -> {
                if (!cb_acceptance.isChecked) {
                    cb_acceptance.isChecked = true
//                    Log.d("EQUA", "showCheckedTotal: ${cb_acceptance.text}")
                    if (acceptanceFee != null) {
                        checkedItemTotal += acceptanceFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash[txt_acceptance.text.toString()] = acceptanceFee!!
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                } else {
                    cb_acceptance.isChecked = false
                    if (acceptanceFee != null) {
                        checkedItemTotal -= acceptanceFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash.remove(txt_acceptance.text.toString(), acceptanceFee!!)
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                }
            }
            layout_accommodation -> {
                if (!cb_accommodation.isChecked) {
                    cb_accommodation.isChecked = true
                    Log.d("EQUA", "showCheckedTotal: ${cb_accommodation.text}")
                    if (accommodationFee != null) {
                        checkedItemTotal += accommodationFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash[txt_accommodation.text.toString()] = accommodationFee!!
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                } else {
                    cb_accommodation.isChecked = false
                    if (accommodationFee != null) {
                        checkedItemTotal -= accommodationFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash.remove(txt_accommodation.text.toString(), accommodationFee!!)
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                }
            }

            layout_exam -> {
                if (!cb_exams.isChecked) {
                    cb_exams.isChecked = true
                    Log.d("EQUA", "showCheckedTotal: ${cb_exams.text}")
                    if (examFee != null) {
                        checkedItemTotal += examFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash[txt_exams.text.toString()] = examFee!!
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                } else {
                    cb_exams.isChecked = false
                    if (examFee != null) {
                        checkedItemTotal -= examFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash.remove(txt_exams.text.toString(), examFee!!)
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                }
            }

            layout_hazard -> {
                if (!cb_hazard.isChecked) {
                    cb_hazard.isChecked = true
                    Log.d("EQUA", "showCheckedTotal: ${cb_hazard.text}")
                    if (hazardFee != null) {
                        checkedItemTotal += hazardFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash[txt_hazard.text.toString()] = hazardFee!!
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                } else {
                    cb_hazard.isChecked = false
                    if (hazardFee != null) {
                        checkedItemTotal -= hazardFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash.remove(txt_hazard.text.toString(), hazardFee!!)
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                }
            }

            layout_lib_equip -> {
                if (!cb_lib.isChecked) {
                    cb_lib.isChecked = true
                    Log.d("EQUA", "showCheckedTotal: ${cb_lib.text}")
                    if (libEquipFee != null) {
                        checkedItemTotal += libEquipFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash[txt_lib_and_equip.text.toString()] = libEquipFee!!
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                } else {
                    cb_lib.isChecked = false
                    if (libEquipFee != null) {
                        checkedItemTotal -= libEquipFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash.remove(txt_lib_and_equip.text.toString(), libEquipFee!!)
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                }
            }

            layout_medic -> {
                if (!cb_medic.isChecked) {
                    cb_medic.isChecked = true
                    Log.d("EQUA", "showCheckedTotal: ${cb_medic.text}")
                    if (medicFee != null) {
                        checkedItemTotal += medicFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash[txt_med_treat.text.toString()] = medicFee!!
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                } else {
                    cb_medic.isChecked = false
                    if (medicFee != null) {
                        checkedItemTotal -= medicFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash.remove(txt_med_treat.text.toString(), medicFee!!)
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                }
            }

            layout_psa -> {
                if (!cb_psa.isChecked) {
                    cb_psa.isChecked = true
                    Log.d("EQUA", "showCheckedTotal: ${cb_psa.text}")
                    if (psaFee != null) {
                        checkedItemTotal += psaFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash[txt_psa.text.toString()] = psaFee!!
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                } else {
                    cb_psa.isChecked = false
                    if (psaFee != null) {
                        checkedItemTotal -= psaFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash.remove(txt_psa.text.toString(), psaFee!!)
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                }
            }

            layout_portal -> {
                if (!cb_portal.isChecked) {
                    cb_portal.isChecked = true
                    Log.d("EQUA", "showCheckedTotal: ${cb_portal.text}")
                    if (portalFee != null) {
                        checkedItemTotal += portalFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash[txt_portal.text.toString()] = portalFee!!
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                } else {
                    cb_portal.isChecked = false
                    if (portalFee != null) {
                        checkedItemTotal -= portalFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash.remove(txt_portal.text.toString(), portalFee!!)
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                }
            }

            layout_security -> {
                if (!cb_security.isChecked) {
                    cb_security.isChecked = true
                    Log.d("EQUA", "showCheckedTotal: ${cb_security.text}")
                    if (securityFee != null) {
                        checkedItemTotal += securityFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash[txt_security.text.toString()] = securityFee!!
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                } else {
                    cb_security.isChecked = false
                    if (securityFee != null) {
                        checkedItemTotal -= securityFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash.remove(txt_security.text.toString(), securityFee!!)
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                }
            }

            layout_sports -> {
                if (!cb_sports.isChecked) {
                    cb_sports.isChecked = true
                    Log.d("EQUA", "showCheckedTotal: ${cb_sports.text}")
                    if (sportsFee != null) {
                        checkedItemTotal += sportsFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash[txt_sports.text.toString()] = sportsFee!!
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                } else {
                    cb_sports.isChecked = false
                    if (sportsFee != null) {
                        checkedItemTotal -= sportsFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash.remove(txt_sports.text.toString(), sportsFee!!)
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                }
            }

            layout_tuition -> {
                if (!cb_tuition.isChecked) {
                    cb_tuition.isChecked = true
                    Log.d("EQUA", "showCheckedTotal: ${cb_tuition.text}")
                    if (tuitionFee != null) {
                        checkedItemTotal += tuitionFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash[txt_tuition.text.toString()] = tuitionFee!!
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                } else {
                    cb_tuition.isChecked = false
                    if (tuitionFee != null) {
                        checkedItemTotal -= tuitionFee!!
                        tv_paying_fee.text = "$checkedItemTotal"
                        feeToPayHash.remove(txt_tuition.toString(), tuitionFee!!)
                    }
                    activity?.toast("${checkedItemTotal.toString()} Checked")
                }
            }

//            else -> {
//                activity?.toast("$checkedItemTotal")
//            }

        }

        btn_pay.setOnClickListener {

//            for (key in feeToPayHash.keys){
//                println("$key : ${feeToPayHash[key]}")
//            }
//            Log.d("EQUA", "onClickMain: $feeToPayHash  \n")

            showConfirmDialog()
//            builder.show()
        }
    }

//    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun showConfirmDialog() {

    val hashAdapter: FeesHashAdapter
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_design)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_pre_payment)
//        val title = dialog.findViewById(R.id.custom_text_title) as TextView
//        title.text = title
        val customListView = dialog.findViewById(R.id.rv_confirm_items) as RecyclerView
    val customTotalText = dialog.findViewById(R.id.tv_total_confirm) as TextView

    val locale: Locale = Locale("en", "NG")
    val fmt: NumberFormat = NumberFormat.getCurrencyInstance(locale)

    customTotalText.append("Total: " +fmt.format(checkedItemTotal).toString())
//    val txtFees = dialog.findViewById(R.id.txt_fee) as TextView

    hashAdapter = FeesHashAdapter()
    customListView.adapter = hashAdapter
    customListView.layoutManager = LinearLayoutManager(activity)



        val yesBtn = dialog.findViewById(R.id.btn_pre_pay_confirm) as Button
        val noBtn = dialog.findViewById(R.id.btn_pre_pay_edit) as Button

        yesBtn.setOnClickListener {
            val frag_rave = FlutterWave()
//
                val bundle = Bundle()
                bundle.putInt("totalFee", checkedItemTotal)
                bundle.putString("payingSemester", selectedSemester)
                bundle.putString("totalDue", dueTotal)
                frag_rave.arguments = bundle

                val manager = fragmentManager

                val frag_transaction = manager?.beginTransaction()

                frag_transaction?.replace(R.id.fragment_container, frag_rave)
                frag_transaction?.commit()
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    var checkedItem = -1

    private fun showSemesterChooseAlert() {
        val semesters = arrayOf("First", "Second")
//        for (semester in semesters) {
//            checkedItem = semesters.indexOf(semester)
//        }

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select Semester to Pay")
        builder.setSingleChoiceItems(
            semesters,
            checkedItem,
            DialogInterface.OnClickListener { dialog, which ->
                //user checked item
                checkedItem = which
                for (semester in semesters) {
//                activity?.toast(checkedItem.toString())
                    if (checkedItem == 0) selectedSemester = "First"
                    else if (checkedItem == 1) selectedSemester = "Second"
                    activity?.toast(selectedSemester.toString())
//                selectedSemester = semester
                }
//                selectedSemester = getString(checkedItem)//checkedItem.toString() //
//            activity?.toast(selectedSemester!!)
            })
        builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->

            pb_payment.visibility = View.VISIBLE
            loadFeesCost()

        })
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}