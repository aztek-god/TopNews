package dv.serg.topnews.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import dv.serg.lib.utils.logd
import dv.serg.topnews.R
import java.text.SimpleDateFormat
import java.util.*


fun openBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val activities = context.packageManager.queryIntentActivities(intent, 0)
    val isIntentSafe = activities.size > 0
    context.logd("isIntentSafe = ${activities.size}")
    if (isIntentSafe) {
        val chooser = Intent.createChooser(intent, context.getString(R.string.intent_chooser))
        context.startActivity(chooser)
    }
}

fun Date.isYesterday(): Boolean {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    val yesterday = Calendar.getInstance().apply {
        set(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1)
    }.time

    return this.before(today) && this.after(yesterday)
}

fun Date.isToday(): Boolean {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    return this.after(today)
}

fun Number.asForwardZeroString(): String {
    when {
        this is Long -> {
            if (this in 0..9) {
                return "0${this}"
            }
        }
        this is Int -> {
            if (this in 0..9) {
                return "0${this}"
            }
        }
        this is Short -> {
            if (this in 0..9) {
                return "0${this}"
            }
        }
        this is Byte -> {
            if (this in 0..9) {
                return "0${this}"
            }
        }
    }

    return this.toString()
}

fun Date.getCustDay(): Int {
    val instance = Calendar.getInstance()
    instance.time = this

    return instance.get(Calendar.DAY_OF_MONTH)
}

fun Date.getHour(): Int {
    val instance = Calendar.getInstance()
    instance.time = this

    return instance.get(Calendar.HOUR_OF_DAY)
}

fun Date.getMinute(): Int {
    val instance = Calendar.getInstance()
    instance.time = this

    return instance.get(Calendar.MINUTE)
}

fun Date.getCustYear(): Int {
    val instance = Calendar.getInstance()
    instance.time = this

    return instance.get(Calendar.YEAR)
}

fun Date.getMonthIndex(): Int {
    return Calendar.getInstance().apply {
        time = this.time
    }.get(Calendar.MONTH)
}


fun getStringDateTime(context: Context, stringDate: String, pattern: String): String {
    val date: Date = SimpleDateFormat(pattern, Locale.getDefault()).parse(stringDate)

    when {
        date.isToday() -> {
            return "${date.getHour()}:${date.getMinute().asForwardZeroString()} ${context.getString(R.string.today)}"
        }
        date.isYesterday() -> {
            return "${date.getHour()}:${date.getMinute().asForwardZeroString()} ${context.getString(R.string.yesterday)}"
        }
    }

    val month: String = context.resources.getStringArray(R.array.months)[date.getMonthIndex()]
    return "${date.getCustDay().asForwardZeroString()}-$month-${date.getCustYear()} ${date.getHour()}:${date.getMinute()}"
}