package com.bige0.noscam

import android.content.*
import android.os.*
import android.telephony.*
import android.widget.*

class SmsReceiver : BroadcastReceiver()
{
	override fun onReceive(context: Context, intent: Intent)
	{
		val extras = intent.extras

		if (extras != null)
		{
			val sms = extras.get("pdus") as Array<Any>
			for (i in sms.indices)
			{
				val format = extras.getString("format")
				var smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				{
					SmsMessage.createFromPdu(sms[i] as ByteArray, format)
				}
				else
				{
					SmsMessage.createFromPdu(sms[i] as ByteArray)
				}
				val phoneNumber = smsMessage.originatingAddress
				val messageText = smsMessage.messageBody.toString()

				Toast.makeText(context, "手机号：（隐藏）\n短信：$messageText", Toast.LENGTH_SHORT).show()
			}

		}
	}
}