package com.psr.quizapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.psr.quizapp.firebase.FireStore
import com.psr.quizapp.models.User


class sign_up : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        backButton()
        val btn_sign_up=findViewById<Button>(R.id.btn_sign_up)
        btn_sign_up.setOnClickListener {
            registerUser()
        }
        val google_sign_in=findViewById<Button>(R.id.google_sign_in_btn)
        google_sign_in.setOnClickListener{
            val intent= Intent(this@sign_up, sign_in::class.java)
            startActivity(intent)
        }
    }
    private fun backButton(){
        val toolbar= findViewById<Toolbar>(R.id.toolbar_sign_up)
        setSupportActionBar(toolbar)
        val actionbar= supportActionBar
        if(actionbar!=null)
        {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.back_arrow)
        }
        toolbar.setNavigationOnClickListener(){
            onBackPressed()
        }
    }
    fun registrationSucces(){
        hideProgressDialog()
        Toast.makeText(this, "Sign Up Successful,Please Verify email to login",Toast.LENGTH_LONG).show()
        verificationDialog(this)
        Handler(Looper.getMainLooper()).postDelayed({hideverificationDialog()
            finish()},7000)

    }



    private fun registerUser() {
        val et_name=findViewById<EditText>(R.id.et_name_sign_up)
        val et_email=findViewById<EditText>(R.id.et_email_sign_up)
        val et_password=findViewById<EditText>(R.id.et_password_sign_up)
        val et_Cpassword=findViewById<EditText>(R.id.et_c_password)
        val name: String = et_name.text.toString().trim { it <= ' ' }
        val email: String = et_email.text.toString().trim { it <= ' ' }
        val password: String = et_password.text.toString().trim { it <= ' ' }
        val Cpassword:String= et_Cpassword.text.toString().trim{it<=' '}
        if (validateForm(name, email, password,Cpassword))
        {
            showProgressDialog(this@sign_up)
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FirebaseAuth.getInstance().currentUser!!.sendEmailVerification().addOnCompleteListener { vtask ->
                            if (vtask.isSuccessful){
                                val fireBaseuser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
                                val remail = fireBaseuser.email!!
                                val user = User(fireBaseuser.uid,name,remail)
                                FireStore().registerUser(this@sign_up,user)


                            }else{
                                hideProgressDialog()
                                vtask.exception!!.message?.let { showErrorSnackBar(it) }
                            }
                        }
                        }

                     else {
                        hideProgressDialog()
                        task.exception!!.message?.let { showErrorSnackBar(it) }
                    }
                }

        }
    }



    private fun validateForm(name: String, email: String, password: String, Cpassword:String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter name.")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password.")
                false
            }
            TextUtils.isEmpty(Cpassword) -> {
                showErrorSnackBar("Please enter confirm password.")
                false
            }
             password!=Cpassword-> {
                showErrorSnackBar("Passwords doesn't match")
                false
            }

            else -> {
                true
            }
        }
    }

}