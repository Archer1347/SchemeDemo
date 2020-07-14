package com.archer.scheme.demo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.archer.scheme.SchemeManager
import com.archer.scheme.annotation.SchemePath
import kotlinx.android.synthetic.main.activity_main.*

@SchemePath("Main/Activity")
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            SchemeManager.handleScheme(
                this,
                "scheme://ModuleA/Activity?data=1&time=23123111&hasData=true"
            )
        }

        btn2.setOnClickListener {
            startNotification()
            finish()
        }

        btn3.setOnClickListener {
            finish()

            Handler().postDelayed({
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("scheme://ModuleB/Activity?name=hhhh")
                )
                startActivity(intent)
                finish()
            }, 500)
        }
    }

    /**
     * Desc: 模拟发送一个通知
     * <p>
     * Author: linjiaqiang
     * Date: 2020/7/14
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var channel: NotificationChannel? = notificationManager.getNotificationChannel("test")
        if (channel == null) {
            // 定位低优先级，其他默认处理优先级为高优先级(状态栏, 横幅, 锁屏)
            channel = NotificationChannel("test", "test", NotificationManager.IMPORTANCE_HIGH)
        } else {
            channel.name = "test"
        }
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(applicationContext, StartUpActivity::class.java)
        intent.putExtra("data", "scheme://ModuleA/Activity")
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(applicationContext, "test")
        builder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("标题")
            .setContentText("辣鸡")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        notificationManager.notify(111, builder.build())
    }
}