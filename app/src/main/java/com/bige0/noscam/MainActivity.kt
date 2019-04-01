package com.bige0.noscam

import android.*
import android.content.*
import android.content.pm.*
import android.icu.text.*
import android.net.*
import android.os.*
import android.support.design.widget.*
import android.support.v4.app.*
import android.support.v4.view.*
import android.support.v7.app.*
import android.support.v7.app.ActionBarDrawerToggle
import android.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
	private val requestReadSms: Int = 2

	private val requestReceiveSms: Int = 4

	private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
		drawer_layout.addDrawerListener(toggle)
		toggle.syncState()

		nav_view.setNavigationItemSelectedListener(this)


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

	override fun onBackPressed()
	{
		if (drawer_layout.isDrawerOpen(GravityCompat.START))
		{
			drawer_layout.closeDrawer(GravityCompat.START)
		}
		else
		{
			super.onBackPressed()
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return when (item.itemId)
		{
			R.id.action_settings -> true
			else -> super.onOptionsItemSelected(item)
		}
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean
	{
		// Handle navigation view item clicks here.
		when (item.itemId)
		{
			R.id.nav_manage ->
			{

			}
			R.id.nav_send ->
			{

			}
		}

		drawer_layout.closeDrawer(GravityCompat.START)
		return true
	}

	private fun setSmsMessages(uriString: String, selection: String?)
	{
		val smsList = ArrayList<SmsData>()
		sms_list_view.adapter = null

		val cursor = contentResolver.query(Uri.parse("content://sms/$uriString"), null, selection, null, null)

		if (cursor!!.moveToFirst())
		{
			val nameID = cursor.getColumnIndex("address")
			val messageID = cursor.getColumnIndex("body")
			val dateID = cursor.getColumnIndex("date")

			do
			{
				val dateString = cursor.getString(dateID)
				smsList.add(SmsData(cursor.getString(nameID), dateFormat.format(dateString.toLong()), cursor.getString(messageID)))
			} while (cursor.moveToNext())

			cursor.close()
			val adapter = ListAdapter(this, smsList)
			sms_list_view.adapter = adapter
		}
	}

	private fun refreshList()
	{
		setSmsMessages("inbox", null)
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
