package com.archer.scheme.moduleb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.scheme.core.SchemeManager
import com.scheme.annotation.SchemeExtra
import com.scheme.annotation.SchemePath

@SchemePath("ModuleB/Activity")
class ModuleBActivity : AppCompatActivity() {

    @SchemeExtra
    var name: String? = null

    @SchemeExtra
    var age: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module)

        SchemeManager.inject(this)

        Log.e("scheme", "name$name")
        Log.e("scheme", "age$age")
    }
}
