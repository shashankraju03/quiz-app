package com.psr.quizapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.psr.quizapp.firebase.FireStore

class Splash : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        val icon = findViewById<ImageView>(R.id.icon)
        val fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        icon.startAnimation(fade_in)
        val AppSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs",0)
        val isNightModeOn: Boolean=AppSettingPrefs.getBoolean("NightMode",true)
        if (isNightModeOn)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            val currentUserId= FireStore().getCurrentUserId()
            if(currentUserId.isNotEmpty()){
                if(!FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
                    deleteAccount()
                    val intent= Intent(this@Splash, intro::class.java)
                    startActivity(intent)
                }else{
                    val intent= Intent(this@Splash, MainActivity::class.java)
                    startActivity(intent)}
            }else {
                val intent = Intent(this@Splash, intro::class.java)
                startActivity(intent)
            }
            startActivity(intent)
            finish()

        }, 1500)

    }
}