package com.psr.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPassword : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        backButton()
        val r_pass=findViewById<Button>(R.id.r_pass_fp)
        r_pass.setOnClickListener {
            resetpass()
        }
    }

    private fun resetpass() {
        showProgressDialog(this)
        val et_email=findViewById<EditText>(R.id.et_email_forgot_pass)
        val email: String = et_email.text.toString()
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    hideProgressDialog()
                    showErrorSnackBar("Password reset email to registered email address")
                    Handler(Looper.getMainLooper()).postDelayed({ finish()},5000)
                }
                else{
                    hideProgressDialog()
                    showErrorSnackBar("No account found with given email ID")
                }
            }
    }

    private fun backButton(){
        val toolbar= findViewById<Toolbar>(R.id.toolbar_forgot_pass)
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
}