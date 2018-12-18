package com.dscfuta.topnotch

import android.app.Application
import net.alexandroid.shpref.ShPref

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        ShPref.init(this, ShPref.APPLY)
    }
}