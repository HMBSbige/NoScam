package com.bige0.noscam

import android.content.*
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.row_layout.view.*

class ListAdapter(private val context: Context, private val list: ArrayList<SmsData>) : BaseAdapter()
{
	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View
	{
		val view = LayoutInflater.from(context)
			.inflate(R.layout.row_layout, parent, false)
		view.sms_sender.text = list[position].senderName
		view.sms_message.text = list[position].message
		view.sms_date.text = list[position].date
		return view
	}

	override fun getItem(position: Int): Any
	{
		return list[position]
	}

	override fun getItemId(position: Int): Long
	{
		return position.toLong()
	}

	override fun getCount(): Int
	{
		return list.size
	}
}