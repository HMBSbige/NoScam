package com.bige0.noscam

import android.content.res.*
import android.os.*
import android.preference.*
import android.support.annotation.*
import android.support.v7.app.*
import android.support.v7.widget.*
import android.view.*

/**
 * A [android.preference.PreferenceActivity] which implements and proxies the necessary calls
 * to be used with AppCompat.
 */
abstract class AppCompatPreferenceActivity : PreferenceActivity()
{

	override fun onCreate(savedInstanceState: Bundle?)
	{
		delegate.installViewFactory()
		delegate.onCreate(savedInstanceState)
		super.onCreate(savedInstanceState)
	}

	override fun onPostCreate(savedInstanceState: Bundle?)
	{
		super.onPostCreate(savedInstanceState)
		delegate.onPostCreate(savedInstanceState)
	}

	val supportActionBar: ActionBar?
		get() = delegate.supportActionBar

	fun setSupportActionBar(toolbar: Toolbar?)
	{
		delegate.setSupportActionBar(toolbar)
	}

	override fun getMenuInflater(): MenuInflater
	{
		return delegate.menuInflater
	}

	override fun setContentView(@LayoutRes layoutResID: Int)
	{
		delegate.setContentView(layoutResID)
	}

	override fun setContentView(view: View)
	{
		delegate.setContentView(view)
	}

	override fun setContentView(view: View, params: ViewGroup.LayoutParams)
	{
		delegate.setContentView(view, params)
	}

	override fun addContentView(view: View, params: ViewGroup.LayoutParams)
	{
		delegate.addContentView(view, params)
	}

	override fun onPostResume()
	{
		super.onPostResume()
		delegate.onPostResume()
	}

	override fun onTitleChanged(title: CharSequence, color: Int)
	{
		super.onTitleChanged(title, color)
		delegate.setTitle(title)
	}

	override fun onConfigurationChanged(newConfig: Configuration)
	{
		super.onConfigurationChanged(newConfig)
		delegate.onConfigurationChanged(newConfig)
	}

	override fun onStop()
	{
		super.onStop()
		delegate.onStop()
	}

	override fun onDestroy()
	{
		super.onDestroy()
		delegate.onDestroy()
	}

	override fun invalidateOptionsMenu()
	{
		delegate.invalidateOptionsMenu()
	}

	private val delegate: AppCompatDelegate by lazy {
		AppCompatDelegate.create(this, null)
	}
}
