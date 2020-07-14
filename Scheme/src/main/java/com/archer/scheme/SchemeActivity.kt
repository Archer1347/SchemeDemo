package com.archer.scheme

import android.app.Activity
import android.os.Bundle

/**
 * Scheme中转
 *
 * Created by ljq on 2020/7/14
 */
class SchemeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.data
        if (uri != null) {
            SchemeManager.handleScheme(this, uri)
        }
        finish()
    }
}