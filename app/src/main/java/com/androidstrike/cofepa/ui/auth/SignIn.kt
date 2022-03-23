package com.androidstrike.cofepa.ui.auth

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.models.User
import com.androidstrike.cofepa.utils.Common
import com.androidstrike.cofepa.utils.login
import com.androidstrike.cofepa.utils.toast
import com.androidstrike.cofepa.utils.visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignIn : Fragment() {


    //    a firebase auth object is created to enable us create a user in the firebase console
//    we have the lateinit var called mAuth where we store the instance of the FirebaseAuth
    private var mAuth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


//        here we initialize the instance of the Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        firebaseUser = mAuth!!.currentUser

        button_sign_in.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()


//            if the email and password fields are empty we display error messages
            if (email.isEmpty()) {
                et_email.error = "Email Required"
                et_email.requestFocus()
                return@setOnClickListener
            }

//            if the email pattern/format does not does match that as defined, we display error messages
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                et_email.error = "Valid Email Required"
                et_email.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, password)

        }

        tv_new_account.setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_signUp)

        }

        tv_forgot_password.setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_passwordReset)

        }

    }

    private fun loginUser(email: String, password: String) {
        pb_sign_in.visible(true)
        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener {

                if (it.isSuccessful) {
                    //login success
                    Common.currentUser = User(firebaseUser?.uid!!, firebaseUser?.email)

                    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                    val usr_info = database.getReference("Users/${firebaseUser?.uid.toString()}")
                    var usrModel: User?
                    val usrInfoListener = object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            usrModel = snapshot.getValue(User::class.java)

                            Common.student_level = usrModel?.level.toString()
                            Common.student_department = usrModel?.department.toString()

                            Common.student_name = usrModel?.name.toString()
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    }
                    usr_info.addListenerForSingleValueEvent(usrInfoListener)
                    pb_sign_in.visible(false)
                    activity?.login()

                } else {
                    it.exception?.message?.let {
                        pb_sign_in.visible(false)
                        activity?.toast(it)
                    }
                }
            }
    }
}
