package com.psr.quizapp

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.psr.quizapp.firebase.FireStore
import com.psr.quizapp.models.User


private val RC_SIGN_IN=100

class sign_in : BaseActivity() {

    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        backButton()
        val btn_sign_in=findViewById<Button>(R.id.btn_sign_in)
        btn_sign_in.setOnClickListener {
            signInUser()
        }
        val fp=findViewById<Button>(R.id.fp)
        fp.setOnClickListener{
            val intent= Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
        val google_sign_in=findViewById<Button>(R.id.google_sign_in_btn)
        google_sign_in.setOnClickListener{
            AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle)
                .setTitle("Note:")
                .setMessage("Using GoogleAuth will overwrite profile info if an account already exists for the selected email")
                .setNegativeButton("Cancel") {dialog, which ->
                    Toast.makeText(this,"Sign In Cancelled!",
                        Toast.LENGTH_SHORT).show()}
                .setPositiveButton("Continue"){dialog,which->
                    googleSignIn()
                }.show()
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    fun googleSignIn(){
        val signInIntent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        googleSignInClient!!.signOut()

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this@sign_in, task.exception!!.message, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    showProgressDialog(this@sign_in)
                    val acct = GoogleSignIn.getLastSignedInAccount(this)
                    val personPhoto: String = acct!!.photoUrl.toString()
                    val fireBaseuser: FirebaseUser = task.result!!.user!!
                    val remail = fireBaseuser.email!!
                    val name = fireBaseuser.displayName!!
                    val user = User(fireBaseuser.uid,name,remail,personPhoto)
                    FireStore().CreateGUser(this@sign_in,user)
                } else {
                    hideProgressDialog()
                    task.exception!!.message?.let { showErrorSnackBar(it) }
                }
            }
    }




    private fun backButton(){
        val toolbar= findViewById<Toolbar>(R.id.toolbar_sign_in)
        setSupportActionBar(toolbar)
        val actionbar= supportActionBar
        if(actionbar!=null)
        {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.back_arrow)
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    fun signInSucces(user: User){
        hideProgressDialog()
        if(FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
        val intent= Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        finishAffinity()
        }else{
            FirebaseAuth.getInstance().signOut()
            showErrorSnackBar("Please Verify your Email")
        }

    }
    private fun signInUser(){
        val et_email=findViewById<EditText>(R.id.et_email_sign_in)
        val et_password=findViewById<EditText>(R.id.et_password_sign_in)
        val email: String = et_email.text.toString().trim { it <= ' ' }
        val password: String = et_password.text.toString().trim { it <= ' ' }
        if (validateForm(email, password))
        {
            showProgressDialog(this@sign_in)
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FireStore().loadUser(this@sign_in)
                    } else {
                        hideProgressDialog()
                        task.exception!!.message?.let { showErrorSnackBar(it) }
                    }
                }
        }
    }
    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password.")
                false
            }
            else -> {
                true
            }
        }
    }



}