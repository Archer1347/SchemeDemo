package com.archer.scheme.demo

import android.app.Application
import com.archer.scheme.SchemeManager

/**
 * Created by ljq on 2020/7/14
 */
class SchemeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SchemeManager.initScheme("scheme", "Main/Activity")
    }
}