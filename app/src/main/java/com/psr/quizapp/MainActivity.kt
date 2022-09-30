package com.psr.quizapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_start= findViewById<Button>(R.id.btn_start)
        btn_start.setOnClickListener(){

                AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle)
                    .setTitle("Confirm Start?")
                    .setNegativeButton("NO") {dialog, which ->
                        Toast.makeText(this,"Cancelled!",
                            Toast.LENGTH_SHORT).show()}
                    .setPositiveButton("YES") { dialog, which ->
                        val intent= Intent(this@MainActivity, QuizActivity::class.java)
                        startActivity(intent)
                        finish()}.show()


        }

        val btn_sign_out= findViewById<Button>(R.id.btn_sign_out)
        btn_sign_out.setOnClickListener(){
            if(isConnected()){
                AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle)
                    .setTitle("Confirm SignOut?")
                    .setNegativeButton("NO") {dialog, which ->
                        Toast.makeText(this,"SignOut Cancelled!",
                            Toast.LENGTH_SHORT).show()}
                    .setPositiveButton("YES"){dialog,which->
                        FirebaseAuth.getInstance().signOut()
                        val intent= Intent(this, intro::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_NO_HISTORY)
                        startActivity(intent)
                        finishAffinity()}.show()
            } else
                showErrorSnackBar("NO INTERNET")
        }

    }
}