package com.archer.scheme.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.archer.scheme.SchemeManager

class StartUpActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent.getStringExtra("data")
        Log.e("scheme","StartUpActivity data $data")
        if (data != null) {
            SchemeManager.handleScheme(this, data)
        } else if (isTaskRoot) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}