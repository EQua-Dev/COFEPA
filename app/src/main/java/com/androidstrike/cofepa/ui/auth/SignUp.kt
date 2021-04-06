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
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.models.User
import com.androidstrike.cofepa.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_sign_up.*

/**
 * A simple [Fragment] subclass.
 * Use the [SignUp.newInstance] factory method to
 * create an instance of this fragment.
 */

class SignUp : Fragment(),AdapterView.OnItemSelectedListener {


    var departmentsArray = arrayOf("Accounting", "ComputerScience", "Economics", "MassCommunication")
    var levelsArray = arrayOf("100L", "200L", "300L", "400L")
//        R.array.departments
    val NEW_SPINNER_ID = 1
    val NEW_SPINNER2_ID = 2

    //    a firebase auth object is created to enable us create a user in the firebase console
//    we have the lateinit var called mAuth where we store the instance of the FirebaseAuth
    private lateinit var mAuth : FirebaseAuth
    private lateinit var database :  FirebaseDatabase
    private lateinit var table_user: DatabaseReference

    private var userId:String?=null
    private var emailAddress:String?=null
    lateinit var spinner_dept_text:String
    lateinit var spinner_level_text:String

    var stud_dept:String?=null
    var stud_level:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_sign_up, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val aa = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, departmentsArray)

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(spinner_department){
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@SignUp
            prompt = "Select Student Department"
            gravity = Gravity.CENTER
        }

        val spinner = Spinner(context)
        spinner.id = NEW_SPINNER_ID

        val ll = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        ll.setMargins(10, 40, 10, 10)
        layout_sign_up.addView(spinner)

        val da = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, levelsArray)
        with(spinner_level){
            adapter = da
            setSelection(0, true)
            onItemSelectedListener = this@SignUp
            prompt = "Select Student Level"
            gravity = Gravity.CENTER
        }

        val spinLev = Spinner(context)
        spinLev.id = NEW_SPINNER2_ID

        val ll2 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        ll2.setMargins(10, 40, 10, 10)
        layout_sign_up.addView(spinLev)

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


//            if the email and password fields are empty we display error messages
            if (email.isEmpty()){
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
            if (password.isEmpty() || password.length < 6){
                et_new_password.error = "6 char password required"
                et_new_password.requestFocus()
                return@setOnClickListener
            }
            if (confirm_password != password) {
                et_new_confirm_password.error = "Must Match With Password"
            }else{

                registerUser(email, password)

            }

        }

        tv_login_instead.setOnClickListener {
            val fragment = SignIn()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.auth_frame, fragment, fragment.javaClass.simpleName)
                ?.commit()
        }
        }


    //    in this method we can create a user in firebase console
    private fun registerUser(email: String, password: String) {
        pb_sign_up.visibility = View.VISIBLE

//    using the firebase object instance we create a user in the firebase console

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    //Registration success
                    //we call the login function from the helper class
                        //todo add the other details to be saved in the realtime database
                            val user = mAuth.currentUser
                    userId = user!!.uid
                    emailAddress = user.email

                    val newUser = User(et_new_user_name.text.toString(),emailAddress.toString(),stud_level.toString(), stud_dept.toString() ,et_new_phone_number.text.toString())
                    table_user.child(userId!!).setValue(newUser)
//                    Common.student_name = et_new_user_name.text.toString()
//                    Common.student_department = spinner_text.toString()
                    activity?.pb_sign_up?.visibility = View.GONE
//                    activity?.login()
                    val fragment = SignIn()
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.auth_frame, fragment, fragment.javaClass.simpleName)
                        ?.commit()
                }else{
//                we show the error message from the attempted registration
//                we set the exception message to be non nullable
                    it.exception?.message?.let {
                        activity?.pb_sign_up?.visibility = View.GONE

//                    we call the toast function from the helper class
                        activity?.toast(it)
                    }
                }
            }

            }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if (parent?.id == R.id.spinner_department){
            spinner_dept_text = parent.getItemAtPosition(position).toString()
            stud_dept = spinner_dept_text
        }else if (parent?.id == R.id.spinner_level){
            spinner_level_text = parent.getItemAtPosition(position).toString()
            stud_level = spinner_level_text
        }


//        activity?.toast(spinner_dept_text)
//        when(view?.id){
//            1 -> activity?.toast(departmentsArray[position])
//            2 -> activity?.toast(levelsArray[position])

        }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
