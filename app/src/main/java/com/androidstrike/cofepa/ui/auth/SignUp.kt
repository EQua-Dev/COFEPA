package com.androidstrike.cofepa.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.models.User
import com.androidstrike.cofepa.utils.toast
import com.androidstrike.cofepa.utils.visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUp : Fragment(), AdapterView.OnItemSelectedListener {


    //        R.array.departments
    val NEW_SPINNER_ID = 1
    val NEW_SPINNER2_ID = 2

    //    a firebase auth object is created to enable us create a user in the firebase console
//    we have the lateinit var called mAuth where we store the instance of the FirebaseAuth
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var table_user: DatabaseReference

    private var userId: String? = null
    private var emailAddress: String? = null
    lateinit var spinner_dept_text: String
    lateinit var spinner_level_text: String

    var stud_dept: String? = null
    var stud_level: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_sign_up, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val departmentArray = resources.getStringArray(R.array.departments)
        val levelArray = resources.getStringArray(R.array.levels)

        val deptAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, departmentArray)
        val levelAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, levelArray)

        auto_complete_tv_dept.setAdapter(deptAdapter)
        auto_complete_tv_level.setAdapter(levelAdapter)


//        here we initialize the instance of the Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        table_user = database.getReference().child("Users")

        button_sign_up.setOnClickListener {
            val email = et_new_email.text.toString().trim()
            val password = et_new_password.text.toString().trim()
            val confirm_password = et_new_confirm_password.text.toString().trim()
            val stud_name = et_new_user_name.text.toString().trim()
            val phone = et_new_phone_number.text.toString().trim()
            stud_dept = auto_complete_tv_dept.text.toString()
            stud_level = auto_complete_tv_level.text.toString()


//            if the email and password fields are empty we display error messages
            if (email.isEmpty()) {
                et_new_email.error = "Email Required"
                et_new_email.requestFocus()
                return@setOnClickListener
            }

//            if the email pattern/format does not does match that as defined, we display error messages
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                et_new_email.error = "Valid Email Required"
                et_new_email.requestFocus()
                return@setOnClickListener
            }

//            if the password contains less than 6 characters we display error message
            if (password.isEmpty() || password.length < 6) {
                et_new_password.error = "6 char password required"
                et_new_password.requestFocus()
                return@setOnClickListener
            }
            if (confirm_password != password) {
                et_new_confirm_password.error = "Must Match With Password"
            }
            if (stud_dept == null) {
                txt_input_layout_dept.error = "Please Select Department"
            }
            if (stud_level == null) {
                txt_input_layout_level.error = "Please Select Level"
            } else {
                registerUser(email, password)
            }

        }

        tv_login_instead.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_signIn)

        }
    }


    //    in this method we can create a user in firebase console
    private fun registerUser(email: String, password: String) {

        pb_sign_up.visible(true)
//    using the firebase object instance we create a user in the firebase console

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    //Registration success
                    //we call the login function from the helper class
                    //todo add the other details to be saved in the realtime database
                    val user = mAuth.currentUser
                    userId = user!!.uid
                    emailAddress = user.email

                    val newUser = User(
                        et_new_user_name.text.toString(),
                        emailAddress.toString(),
                        stud_level.toString(),
                        stud_dept.toString(),
                        et_new_phone_number.text.toString()
                    )
                    table_user.child(userId!!).setValue(newUser)
//                    Common.student_name = et_new_user_name.text.toString()
//                    Common.student_department = spinner_text.toString()
                    pb_sign_up.visible(false)
//                    activity?.login()
                    findNavController().navigate(R.id.action_signUp_to_signIn)

                } else {
//                we show the error message from the attempted registration
//                we set the exception message to be non nullable
                    it.exception?.message?.let {
                        pb_sign_up.visible(false)
//                    we call the toast function from the helper class
                        activity?.toast(it)
                    }
                }
            }

    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}
