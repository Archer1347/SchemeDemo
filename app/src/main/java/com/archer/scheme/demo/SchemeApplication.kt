package com.archer.scheme.demo

import android.app.Application
import com.archer.scheme.SchemeManager

/**
 * Desc:
 * <p>
 * Date: 2020/7/14
 * Copyright: Copyright (c) 2010-2020
 * Company: @微微科技有限公司
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * @author: linjiaqiang
 */
class SchemeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SchemeManager.initScheme("scheme", "Main/Activity")
    }
}