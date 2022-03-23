package com.androidstrike.cofepa.ui.verify

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.adapters.VerifyHolder
import com.androidstrike.cofepa.models.PaidFees
import com.androidstrike.cofepa.utils.Common
import com.androidstrike.cofepa.utils.toast
import com.androidstrike.cofepa.utils.visible
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_sub_verify.*

class SubVerify : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    lateinit var selectedSemester: String
    lateinit var recyclerViewVerify: RecyclerView
    var adapter: FirebaseRecyclerAdapter<PaidFees, VerifyHolder>? = null
    var checkedItem = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_verify, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showSemesterChooseDialog()

        recyclerViewVerify = rv_verify
        val layoutManager = LinearLayoutManager(activity)
        recyclerViewVerify.layoutManager = layoutManager
        recyclerViewVerify.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                layoutManager.orientation
            )
        )
    }


    private fun showSemesterChooseDialog() {
        val semesters = arrayOf("First Semester", "Second Semester")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select Semester to Verify")
        builder.setSingleChoiceItems(
            semesters,
            checkedItem,
            DialogInterface.OnClickListener { dialog, which ->
                //user checked item
                checkedItem = which
                for (semester in semesters) {
                    if (checkedItem == 0) selectedSemester = "FirstSemester"
                    else if (checkedItem == 1) selectedSemester = "SecondSemester"
                }
            })
        builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->

            verifySemesterFees()

        })
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun verifySemesterFees() {

        val userUid = mAuth.currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val verifyTableFees =
            database.reference.child("Users/$userUid/Fees/${Common.student_level}/${selectedSemester}")

        val options = FirebaseRecyclerOptions.Builder<PaidFees>()
            .setQuery(verifyTableFees, PaidFees::class.java)
            .build()
        try {
            adapter = object : FirebaseRecyclerAdapter<PaidFees, VerifyHolder>(options) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): VerifyHolder {
                    val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.custom_verify_item, parent, false)
                    return VerifyHolder(itemView)
                }

                override fun onBindViewHolder(
                    holder: VerifyHolder,
                    position: Int,
                    model: PaidFees
                ) {
                    holder.txtSemester.append("${model.semester}, ${model.level}")
                    holder.txtTotalDue.append("${model.dueTotal}")
                    holder.txtTotalPaid.append("${model.paidTotal}")
                    holder.txtTotalOwing.append("${model.owingTotal}")

                    val verificationRef = model.paymentRef.toString()

                    if (model.isVerified == false) {
                        if (model.owingTotal == "N0") {
                            holder.btnVerify.setOnClickListener {
                                // edittext alert dialog
                                val etBuilder = MaterialAlertDialogBuilder(requireContext())

                                etBuilder.setTitle("Enter Verification Code ")

                                val constraintLayout = getEditTextLayout(requireContext())
                                etBuilder.setView(constraintLayout)

                                val textInputLayout =
                                    constraintLayout.findViewWithTag<TextInputLayout>("textInputLayoutTag")
                                val textInputEditText =
                                    constraintLayout.findViewWithTag<TextInputEditText>("textInputEditTextTag")

                                // alert dialog positive button
                                etBuilder.setPositiveButton("Submit") { dialog, which ->
                                    val userCode: String = textInputEditText.text.toString()
                                    if (userCode == verificationRef) {
                                        holder.txtCleared.text = "Cleared"
                                        holder.txtCleared.setCompoundDrawablesWithIntrinsicBounds(
                                            R.drawable.ic_cleared,
                                            0,
                                            0,
                                            0
                                        )
                                        database.reference.child("Users/$userUid/Fees/${Common.student_level}/${selectedSemester}").orderByChild("verified").equalTo(true)


                                        val sharedPref =
                                            requireActivity().getSharedPreferences(
                                                "VerificationStatus",
                                                Context.MODE_PRIVATE
                                            )
                                        val editor = sharedPref.edit()
                                        editor.putBoolean("isVerified", true)
                                        editor.apply()

                                    } else {
                                        activity?.toast("Code not valid. 2 more attempts!")
                                    }
                                }

                                // alert dialog other buttons
                                etBuilder.setNegativeButton("No", null)
                                etBuilder.setNeutralButton("Cancel", null)

                                // set dialog non cancelable
                                etBuilder.setCancelable(false)

                                // finally, create the alert dialog and show it
                                val dialog = etBuilder.create()

                                dialog.show()

                                // initially disable the positive button
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

                                // edit text text change listener
                                textInputEditText.addTextChangedListener(object : TextWatcher {
                                    override fun afterTextChanged(p0: Editable?) {
                                    }
                                    override fun beforeTextChanged(
                                        p0: CharSequence?, p1: Int,
                                        p2: Int, p3: Int
                                    )
                                    {}
                                    override fun onTextChanged(
                                        p0: CharSequence?, p1: Int,
                                        p2: Int, p3: Int
                                    ) {
                                        if (p0.isNullOrBlank()) {
                                            textInputLayout.error = "Code is required"
                                            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                                .isEnabled = false
                                        } else {
                                            textInputLayout.error = ""
                                            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                                .isEnabled = true
                                        }
                                    }
                                })
                            }
                        } else {
                            holder.btnVerify.visible(false)
                            activity?.toast("Complete fees to be cleared")
                        }
                    } else {
                        activity?.toast("Already Verified")
                    }
                }
            }
        } catch (e: Exception) {
            activity?.toast(e.message.toString())
        }
        adapter?.startListening()
        recyclerViewVerify.adapter = adapter

    }

    private fun getEditTextLayout(context: Context): ConstraintLayout {
        val constraintLayout = ConstraintLayout(context)
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        constraintLayout.layoutParams = layoutParams
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            constraintLayout.id = View.generateViewId()
        }

        val textInputLayout = TextInputLayout(context)
        textInputLayout.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
        layoutParams.setMargins(
            32.toDp(context),
            8.toDp(context),
            32.toDp(context),
            8.toDp(context)
        )
        textInputLayout.layoutParams = layoutParams
        textInputLayout.hint = "Input Verification Code"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textInputLayout.id = View.generateViewId()
        }
        textInputLayout.tag = "textInputLayoutTag"


        val textInputEditText = TextInputEditText(context)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textInputEditText.id = View.generateViewId()
        }
        textInputEditText.tag = "textInputEditTextTag"

        textInputLayout.addView(textInputEditText)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintLayout.addView(textInputLayout)
        return constraintLayout
    }

    // extension method to convert pixels to dp
    fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()

    //boolean shared pref to store whether user is using the app for the 1st time
    private fun isVerified(): Boolean {
        val sharedPref =
            requireActivity().getSharedPreferences("VerificationStatus", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isVerified", false)
    }

}