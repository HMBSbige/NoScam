package com.bige0.noscam

import java.util.concurrent.atomic.*

object NotificationID
{
	private val c = AtomicInteger(0)
	val id: Int get() = c.incrementAndGet()
}