package com.dscfuta.topnotch

import android.app.Application
import android.content.Intent
import com.dscfuta.topnotch.ui.LoginActivity
import com.google.firebase.auth.FirebaseUser
import net.alexandroid.shpref.ShPref

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        ShPref.init(this, ShPref.APPLY)
    }
    /*fun updateUI(user: FirebaseUser?){
        if(user != null){
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }else{
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
    }*/
}