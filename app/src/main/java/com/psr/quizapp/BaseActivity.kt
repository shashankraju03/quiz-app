package com.psr.quizapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlin.system.exitProcess
import android.net.ConnectivityManager
import androidx.core.graphics.drawable.toDrawable
import java.io.IOException
import java.lang.Exception
import java.net.InetAddress
import java.util.concurrent.TimeUnit


open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    private lateinit var isdialog:AlertDialog
    private lateinit var adialog:AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun verificationDialog(mActivity:Activity) {
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.verification_progress,null)
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        adialog = builder.create()
        adialog.show()
    }
    fun hideverificationDialog() {
        isdialog.dismiss()
    }


    fun showProgressDialog(mActivity:Activity) {
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_progress,null)
        dialogView.background=Color.parseColor("#FFB6C1").toDrawable()
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isdialog = builder.create()
        isdialog.show()
    }

    fun hideProgressDialog() {
        isdialog.dismiss()
    }

    fun getCurrentUserId(): String {
        val currentUser= FirebaseAuth.getInstance().currentUser
        var currentUserId:String=""
        if (currentUser!=null) {currentUserId=currentUser.uid}
        return currentUserId
    }

    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
    fun doubleBackToCancel() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_cancel),
            Toast.LENGTH_SHORT
        ).show()

        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
    fun deleteAccount(){
        val Firestore= FirebaseFirestore.getInstance()
        Firestore.document("/Users/${getCurrentUserId()}").delete()
        val user = Firebase.auth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                   Toast.makeText(this,"Last Signup cancelled",Toast.LENGTH_LONG).show()
                }
            }
        FirebaseAuth.getInstance().signOut()
        val intent= Intent(this, Splash::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        finishAffinity()
    }
    @Throws(InterruptedException::class, IOException::class)
    open fun isConnected(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }

    fun showErrorSnackBar(message: String) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@BaseActivity,
                R.color.primary_red
            )
        )
        snackBar.show()
    }
}
