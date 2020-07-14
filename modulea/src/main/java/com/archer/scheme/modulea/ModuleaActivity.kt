package com.archer.scheme.modulea

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.archer.scheme.SchemeManager
import com.archer.scheme.annotation.SchemeExtra
import com.archer.scheme.annotation.SchemePath
import com.archer.scheme.modulea.R
import kotlinx.android.synthetic.main.activity_module1.*

@SchemePath("ModuleA/Activity")
class ModuleaActivity : AppCompatActivity() {

    @SchemeExtra
    var data: Int = 2

    @SchemeExtra
    var time: String? = null

    @SchemeExtra
    var hasData: Boolean = false

    @SchemeExtra
    var weight: Double = 0.1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module1)

        SchemeManager.inject(this)

        Log.e("scheme", "data$data")
        Log.e("scheme", "time$time")
        Log.e("scheme", "hasData$hasData")
        Log.e("scheme", "weight$weight")

        btn1.setOnClickListener {
            SchemeManager.handleScheme(
                this,
                "vvlife://ModuleA/Activity2?weight=0.1"
            )
        }
        btn2.setOnClickListener {
            SchemeManager.handleScheme(
                this,
                "vvlife://ModuleB/Activity?name=archer&age=18"
            )
        }
    }
}
