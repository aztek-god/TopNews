package dv.serg.topnews.app

import android.app.Application
import android.support.v4.app.Fragment
import com.facebook.stetho.Stetho
import dv.serg.topnews.BuildConfig
import dv.serg.topnews.R
import dv.serg.topnews.di.Injector
import dv.serg.topnews.di.component.AppComponent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AppContext : Application() {
    companion object {
        lateinit var appComponent: AppComponent
        lateinit var appContext: AppContext

        fun getStringById(resId: Int): String {
            return appContext.getString(resId)
        }

        fun getStringByName(stringDesc: String): String {
            return getStringById(appContext.resources.getIdentifier(stringDesc, "string", appContext.packageName))
        }

        fun getMonthName(index: Int): String {
            return AppContext.appContext.resources.getStringArray(R.array.months)[index]
        }

        fun datetimeToString(datetime: Long): String {
            val sdf = SimpleDateFormat("HH:mm, d __ yyyy", Locale.ENGLISH)
            val date = Date(datetime)
            val monthNumber = date.month
            return sdf.format(date).replace("__", getMonthName(monthNumber))
        }

        fun convertDatetimeFromString(datetimeString: String, pattern: String = Constants.Time.DEFAULT_DATETIME_PATTERN): Long {
            val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
            val date: Date = sdf.parse(datetimeString)
            return date.time
        }

        fun getStringDate(date: String): String {
            return datetimeToString(convertDatetimeFromString(date))
        }

        val fragments: MutableMap<Int, Fragment> = HashMap()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = Injector.getAppComponent(this)
        appContext = this

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}