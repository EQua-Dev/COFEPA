package com.androidstrike.cofepa.ui.verify

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.adapters.HistoryHolder
import com.androidstrike.cofepa.models.PaidFees
import com.androidstrike.cofepa.utils.Common
import com.androidstrike.cofepa.utils.toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_verify_history.*
import kotlinx.coroutines.*

class VerifyHistory : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    var adapter: FirebaseRecyclerAdapter<PaidFees, HistoryHolder>? = null
    lateinit var recyclerViewHistory: RecyclerView
    private lateinit var selectedSemester: String
    var checkedItem = -1



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showSemesterChooseAlert()

        recyclerViewHistory = rv_history
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerViewHistory.layoutManager = layoutManager
        Log.d("EQUARV", "onActivityCreated: nna eeh")
        recyclerViewHistory.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                layoutManager.orientation
            )
        )
    }

    private fun showSemesterChooseAlert() {
        val semesters = arrayOf("First", "Second")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select Semester to View")
        builder.setSingleChoiceItems(
            semesters,
            checkedItem,
            DialogInterface.OnClickListener { dialog, which ->
                //user checked item
                checkedItem = which
                for (semester in semesters) {
//                activity?.toast(checkedItem.toString())
                    if (checkedItem == 0) selectedSemester = "FirstSemester"
                    else if (checkedItem == 1) selectedSemester = "SecondSemester"
                    activity?.toast(selectedSemester.toString())
//                selectedSemester = semester
                }
//                selectedSemester = getString(checkedItem)//checkedItem.toString() //
//            activity?.toast(selectedSemester!!)
            })
        builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
            viewSemesterFees()

        })
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun viewSemesterFees() {

        val userUid = mAuth.currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val verifyTableFees =
//            database.getReference().child("Users/a4LQ9n1zUdMWbbXAaEsiOLb8Byz1/Fees/300L/SecondSemester")
            database.reference.child("Users/$userUid/Fees/${Common.student_level}/$selectedSemester")
        Log.d("EQUA1122", "verifySemesterFees: ${userUid}\n${Common.student_level}")

        val options = FirebaseRecyclerOptions.Builder<PaidFees>()
            .setQuery(verifyTableFees, PaidFees::class.java)
            .build()

        Log.d("EQUA@@##!!", "verifySemesterFees: REached here2")

        try {
            adapter = object : FirebaseRecyclerAdapter<PaidFees, HistoryHolder>(options) {

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): HistoryHolder {
                    Log.d("EQUA@@##!!", "verifySemesterFees: REached here3")
                    val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.custom_history_item, parent, false)
                    return HistoryHolder(itemView)
                }

                override fun onBindViewHolder(
                    holder: HistoryHolder,
                    position: Int,
                    model: PaidFees
                ) {
                    holder.txtHistorySemester.append("${model.semester} Semester, ${model.level}")
                    holder.txtHistoryTotalDue.append("${model.dueTotal}")
                    holder.txtHistoryTotalPaid.append("${model.paidTotal}")

                    val refHash = model.paymentRef.toString()
                    holder.btnHistory.setOnClickListener {
                        showRefDialog(refHash)
                    }

//                    notifyDataSetChanged()
                }

            }
        } catch (e: Exception) {
            Log.d("EQUA", "verifySemesterFees: ${e.message}")
        }
        adapter?.startListening()
        recyclerViewHistory.adapter = adapter
    }


    private fun showRefDialog(referenceHash: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_design)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_reference_dialog)

        val customTV = dialog.findViewById(R.id.hash_txt_view) as TextView
        val customBtn = dialog.findViewById(R.id.copy_button) as Button
//        val customCTV = dialog.findViewById(R.id.message_textview) as TextView

        customTV.text = referenceHash


        customBtn.setOnClickListener {
            lifecycleScope.launch {
                copyToClipboard(referenceHash)
//                customCTV.animate().translationY(80f).duration = 200L
//                delay(2000L)
//                customCTV.animate().translationY(-80f).duration = 200L
                activity?.toast("Copied!")
//                applyAnimation()
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun copyToClipboard(referenceHash: String) {
        val clipboardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Payment Reference", referenceHash)
        clipboardManager.setPrimaryClip(clipData)
    }

//    override fun onStart() {
//        super.onStart()
//        Log.d("EQUAOnStart", "onStart: omo")
//        adapter?.startListening()
//    }

    override fun onStop() {
        super.onStop()
        if (adapter != null)
            adapter!!.stopListening()
    }

}