package com.androidstrike.cofepa.ui.verify

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.models.PaidFees
import com.androidstrike.cofepa.utils.Common
import com.androidstrike.cofepa.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_sub_verify.*

class SubVerify : Fragment() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var table_fees_for_verify: DatabaseReference
    lateinit var selectedSemester: String

    var paidFeesModel: PaidFees? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_verify, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showSemesterChooseAlert()

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


//        database.getReference().child("Users/$user/Fees/${Common.student_level}/${payingSemester} Semester ")
    }

    var checkedItem = -1

    private fun showSemesterChooseAlert() {
        val semesters = arrayOf("First", "Second")

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

            verifySemesterFees()

        })
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun verifySemesterFees() {

        val userUid = mAuth.currentUser?.uid
        table_fees_for_verify = database.getReference("Users/$userUid/Fees/${Common.student_level}/${selectedSemester} Semester ")

        val verifyListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                paidFeesModel = snapshot.getValue(PaidFees::class.java)

                if (paidFeesModel!!.owingTotal == "0")
                    verify_text.text = getString(R.string.verify_yes)
                else
                    verify_text.text = getString(R.string.verify_no)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        table_fees_for_verify.addListenerForSingleValueEvent(verifyListener)
    }
}