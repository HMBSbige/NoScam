package com.bige0.noscam

import android.content.*
import android.os.*
import android.telephony.*


class SmsReceiver : BroadcastReceiver()
{
	override fun onReceive(context: Context, intent: Intent)
	{
		val extras = intent.extras

		if (extras != null)
		{
			val sms = extras.get("pdus") as Array<*>
			var phoneNumber = ""
			var messageText = ""
			for (i in sms.indices)
			{
				val format = extras.getString("format")
				val smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				{
					SmsMessage.createFromPdu(sms[i] as ByteArray, format)
				}
				else
				{
					SmsMessage.createFromPdu(sms[i] as ByteArray)
				}
				phoneNumber = smsMessage.originatingAddress
				messageText += smsMessage.messageBody.toString()
			}
			Notify.requestAndWarn(context, "https://spam.bige0.cn/api/spams", messageText, phoneNumber)
		}
	}
}