package com.bige0.noscam

import android.app.*
import android.content.*
import android.graphics.*
import android.os.*
import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.*
import java.nio.charset.*

object Notify
{
	fun createChannel(context: Context)
	{
		val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
		val mChannel = NotificationChannel("0", "警告" as CharSequence, NotificationManager.IMPORTANCE_HIGH)
		mChannel.description = "警告风险短信"
		mChannel.enableLights(true)
		mChannel.lightColor = Color.RED
		mChannel.enableVibration(true)
		notificationManager!!.createNotificationChannel(mChannel)
	}

	fun warn(context: Context, title: String, text: String)
	{
		val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			val notification = Notification.Builder(context, "0")
				.setSmallIcon(android.R.drawable.stat_sys_warning)
				.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.logo))
				.setContentTitle(title)
				.setContentText(text)
				.setStyle(Notification.BigTextStyle().bigText(text))
				.build()
			notificationManager!!.notify(NotificationID.id, notification)
		}
		else
		{
			TODO("VERSION.SDK_INT < O")
		}
	}

	fun toast(context: Context, string: String)
	{
		android.widget.Toast.makeText(context, "手机号：（隐藏）\n短信：$string", android.widget.Toast.LENGTH_SHORT)
			.show()
	}

	fun requestAndWarn(context: Context, url: String, json: String, phoneNumber: String)
	{
		Fuel.post(url)
			.header(Headers.CONTENT_TYPE, "application/json")
			.body("\"$json\"")
			.also { println(it) }
			.response { result ->
				val res = result.get()
					.toString(Charset.defaultCharset())
				if (res == "-1")
				{
					Notify.warn(context, "你有一条来自 $phoneNumber 的风险短信", json)

				}
			}
	}

}