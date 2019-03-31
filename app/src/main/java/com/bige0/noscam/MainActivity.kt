package com.bige0.noscam

import android.*
import android.content.pm.*
import android.net.*
import android.os.*
import android.support.v4.app.*
import android.support.v7.app.*
import android.widget.*
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
			setSmsMessages("inbox", null)
		}

		findViewById<Button>(R.id.one_number_sms).setOnClickListener {
			Notify.warn(this, "标题", "内容")
		}


		all_sms.setOnClickListener {
			setSmsMessages("", null)
		}
		inbox_sms.setOnClickListener {
			setSmsMessages("inbox", null)
		}
		outbox_sms.setOnClickListener {
			setSmsMessages("outbox", null)
		}
		sent_sms.setOnClickListener {
			setSmsMessages("sent", null)
		}
		draft_sms.setOnClickListener {
			setSmsMessages("draft", null)
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
	{
		if (requestCode == requestReadSms)
		{
			setSmsMessages("inbox", null)
		}
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
}
