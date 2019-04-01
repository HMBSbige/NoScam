package com.bige0.noscam

import android.*
import android.content.*
import android.content.pm.*
import android.net.*
import android.os.*
import android.support.v4.app.*
import android.support.v7.app.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity()
{
	private val requestReadSms: Int = 2

	private val requestReceiveSms: Int = 4

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		Notify.createChannel(this)

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS), requestReceiveSms)
		}

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS), requestReadSms)
		}
		else
		{
			refreshList()
		}

		test_button.setOnClickListener {
			Notify.warn(this, "标题", "内容")
		}

		inbox_sms.setOnClickListener {
			setSmsMessages("inbox", null)
		}

		registerReceiver(receiveSms, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))

		pullToRefresh.setOnRefreshListener {
			refreshList()
			pullToRefresh.isRefreshing = false
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
	{
		if (requestCode == requestReadSms)
		{
			refreshList()
		}
	}

	private fun refreshList()
	{
		setSmsMessages("inbox", null)
	}

	private fun setSmsMessages(uriString: String, selection: String?)
	{
		val smsList = ArrayList<SmsData>()
		sms_list_view.adapter = null

		val cursor = contentResolver.query(Uri.parse("content://sms/$uriString"), null, selection, null, null)

		if (cursor.moveToFirst())
		{
			val nameID = cursor.getColumnIndex("address")
			val messageID = cursor.getColumnIndex("body")
			val dateID = cursor.getColumnIndex("date")

			do
			{
				val dateString = cursor.getString(dateID)
				smsList.add(SmsData(cursor.getString(nameID), Date(dateString.toLong()).toString(), cursor.getString(messageID)))
			} while (cursor.moveToNext())

			cursor.close()
			val adapter = ListAdapter(this, smsList)
			sms_list_view.adapter = adapter
		}
	}

	private var receiveSms: BroadcastReceiver = object : BroadcastReceiver()
	{
		override fun onReceive(context: Context, intent: Intent)
		{
			refreshList()
		}
	}

	override fun onDestroy()
	{
		super.onDestroy()
		unregisterReceiver(receiveSms)
	}

}
