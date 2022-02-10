package com.archer.scheme.modulea

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.scheme.core.SchemeManager
import com.scheme.annotation.SchemeExtra
import com.scheme.annotation.SchemePath
import kotlinx.android.synthetic.main.activity_module2.*

@SchemePath("ModuleA/Activity2")
class ModuleaActivity2 : AppCompatActivity() {

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
        setContentView(R.layout.activity_module2)

        SchemeManager.inject(this)

        Log.e("scheme", "data$data")
        Log.e("scheme", "time$time")
        Log.e("scheme", "hasData$hasData")
        Log.e("scheme", "weight$weight")

        btn.setOnClickListener {
            SchemeManager.handleScheme(
                this,
                "vvlife://ModuleB/Activity?name=archer&age=18"
            )
        }
    }
}
