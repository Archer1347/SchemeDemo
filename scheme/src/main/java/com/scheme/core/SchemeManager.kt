package com.scheme.core

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.scheme.annotation.IExtraProvider
import com.scheme.annotation.ISchemeProvider
import java.lang.Exception
import java.lang.IllegalArgumentException

/**
 * Created by ljq on 2020/7/14
 */
object SchemeManager {

    private const val PROVIDER_PACKAGE = "com.archer.scheme.provider"
    private var mainClass: Class<Any>? = null
    private var scheme: String? = null

    /**
     * Desc: 处理Uri
     * <p>
     * Author: linjiaqiang
     * Date: 2020/7/14
     *
     * @param uriPath uri字符串
     */
    fun handleScheme(context: Context, uriPath: String, schemeNotFoundException: (() -> Unit)? = null) {
        Log.d("scheme", "uriPath => $uriPath")
        val uri = if (uriPath.contains("://")) {
            Uri.parse(uriPath)
        } else {
            Uri.parse("$scheme://$uriPath")
        }
        handleScheme(context, uri, schemeNotFoundException)
    }

    /**
     * Desc: 处理Uri
     * <p>
     * Author: linjiaqiang
     * Date: 2020/7/14
     *
     * @param uri uri
     */
    fun handleScheme(context: Context, uri: Uri, schemeNotFoundException: (() -> Unit)? = null) {
        if (scheme == null || mainClass == null) {
            throw IllegalArgumentException("请先调用initScheme初始化")
        }
        val cls = findClass(uri)
        if (cls == null) {
            schemeNotFoundException?.invoke()
            return
        }
        val intent = Intent(context, cls)
        handleExtra(cls, uri, intent)
        if (context !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (mainClass == null || context !is Activity || context.javaClass.simpleName == mainClass!!.simpleName || isAppOpened(context) || SchemeActivity::class.java.name == intent.component?.className) {
            context.startActivity(intent)
        } else {
            val intents = arrayOfNulls<Intent>(2)
            intents[0] = Intent.makeRestartActivityTask(ComponentName(context, mainClass!!))
            intents[1] = intent
            context.startActivities(intents)
        }
    }

    /**
     * Desc: 参数处理
     * <p>
     * Author: linjiaqiang
     * Date: 2020/7/14
     */
    private fun handleExtra(cls: Class<Any>, uri: Uri, intent: Intent) {
        val extraPkg = PROVIDER_PACKAGE + "." + cls.simpleName + "$$" + "Extra"
        try {
            val extraProvider = Class.forName(extraPkg).newInstance() as IExtraProvider
            extraProvider.convertParameterType(uri, intent)
        } catch (e: Exception) {
            Log.d("scheme", "路径${uri}参数解析异常")
        }
    }

    /**
     * Desc: 获取路由路径的class
     * <p>
     * Author: linjiaqiang
     * Date: 2020/7/14
     */
    private fun findClass(uri: Uri): Class<Any>? {
        val pkg = PROVIDER_PACKAGE + "." + uri.host + "$$" + "Scheme"
        return try {
            val schemeProvider = Class.forName(pkg).newInstance() as ISchemeProvider
            schemeProvider.findClass(uri.host + uri.path)
        } catch (e: Exception) {
            Log.d("scheme", "路径${uri}未找到")
            null
        }
    }

    /**
     * Desc: activity参数注入
     * <p>
     * Author: linjiaqiang
     * Date: 2020/7/14
     */
    fun inject(activity: Activity) {
        val extraPkg = PROVIDER_PACKAGE + "." + activity::class.java.simpleName + "$$" + "Extra"
        val extraProvider = Class.forName(extraPkg).newInstance() as IExtraProvider
        extraProvider.inject(activity)
    }

    /**
     * Desc: 初始化
     * <p>
     * Author: linjiaqiang
     * Date: 2020/7/14
     *
     * @param scheme 设置scheme
     * @param mainPath 首页路径
     */
    fun initScheme(scheme: String, mainPath: String) {
        SchemeManager.scheme = scheme
        mainClass = findClass(Uri.parse("$scheme://$mainPath"))
    }

    private fun isAppOpened(activity: Activity): Boolean {
        val activityManager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val activityName = activity.javaClass.name
        val tasksInfo = activityManager.getRunningTasks(3)
        if (tasksInfo != null && tasksInfo.size > 0) {
            for (i in tasksInfo.indices) {
                val cn = tasksInfo[i].baseActivity
                val currentPackageName = cn!!.packageName
                if (!TextUtils.isEmpty(currentPackageName) && currentPackageName == getAppPackageName(activity)) {
                    val className = cn.className
                    if (activityName != className) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun getAppPackageName(context: Context): String? {
        return try {
            val packageInfo = context.applicationContext.packageManager.getPackageInfo(
                    context.applicationContext.packageName,
                    0
            )
            if (packageInfo == null) {
                ""
            } else {
                packageInfo.packageName
            }
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }
}