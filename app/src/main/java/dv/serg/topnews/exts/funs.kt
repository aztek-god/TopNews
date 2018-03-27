package dv.serg.topnews.exts

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.support.annotation.ColorInt
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import dv.serg.topnews.R
import dv.serg.topnews.ui.activity.SearchActivity
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*


fun openBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val activities = context.packageManager.queryIntentActivities(intent, 0)
    val isIntentSafe = activities.size > 0
    if (isIntentSafe) {
        val chooser = Intent.createChooser(intent, context.getString(R.string.intent_chooser))
        context.startActivity(chooser)
    }
}

fun <E> MutableList<E>.update(data: List<E>) {
    clear()
    addAll(data)
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
    return "${date.getCustDay().asForwardZeroString()}-$month-${date.getCustYear()} ${date.getHour()}:${if (date.getMinute() < 10) {
        date.getMinute().asForwardZeroString()
    } else {
        date.getMinute().toString()
    }}"
}


fun View.showAnimation() {
    animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(200)
            .setInterpolator(LinearOutSlowInInterpolator())
            .setListener(
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator?) {
                            super.onAnimationStart(animation)
                            visibility = View.GONE
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            visibility = View.VISIBLE
                        }
                    }
            )

}

fun View.hideAnimation() {
    animate().scaleX(0f)
            .scaleY(0f)
            .alpha(0f)
            .setDuration(300)
            .setInterpolator(FastOutLinearInInterpolator())
            .setListener(
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator?) {
                            super.onAnimationStart(animation)
                            visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            visibility = View.GONE
                        }
                    }
            )
            .start()
}

fun FragmentManager.transaction(action: (FragmentTransaction) -> Unit) {
    val transaction: FragmentTransaction = beginTransaction()
    action.invoke(transaction)
    transaction.commit()
}

fun Drawable.setColor(@ColorInt colorRes: Int) {
    colorFilter = PorterDuffColorFilter(colorRes, PorterDuff.Mode.MULTIPLY)
}

fun ImageView.setColor(@ColorInt colorRes: Int) {
    colorFilter = PorterDuffColorFilter(colorRes, PorterDuff.Mode.MULTIPLY)
}

fun Context.isNetworkConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return cm.activeNetworkInfo != null
}

fun Context.copyToClipboard(txt: String, label: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, txt)
    clipboard.primaryClip = clip
}

fun ImageView.load(uri: String?) {
    Observable.fromCallable {
        if (uri == null) {
            throw Exception()
        }
        val load: RequestBuilder<Drawable> = Glide.with(this).load(uri)
        load
    }
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it: RequestBuilder<Drawable> ->
                it.into(this)
            }, {
                val drawable = ContextCompat.getDrawable(this.context, R.drawable.no_image)
                this.setImageDrawable(drawable)

            })
}


fun SearchActivity.alert(title: String, message: String, okAction: () -> Unit = {}, cancelAction: () -> Unit = {}) {
    val dialog: AlertDialog = AlertDialog.Builder(this).create().apply {
        this@alert.title = title
        setMessage(message)

        setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", { dialog, _ ->
            cancelAction.invoke()
            dialog.dismiss()
        })

        setButton(AlertDialog.BUTTON_POSITIVE, "OK", { dialog, _ ->
            okAction.invoke()
            dialog.dismiss()
        })
    }

    dialog.show()
}

fun Context.loadDrawable(name: String, imgView: ImageView) {
    imgView.setImageResource(resources.getIdentifier(name, "drawable", packageName))
}


fun <T> Flowable<T>.performOnIoThread(): Flowable<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
}

fun <T> Observable<T>.performOnIoThread(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
}

fun <T> Single<T>.performOnIoThread(): Single<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
}

fun <T> Maybe<T>.performOnIoThread(): Maybe<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
}

fun <T> Completable.performOnIoThread(): Completable {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
}

fun <T> Flowable<T>.performOnComputationThread(): Flowable<T> {
    return this.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.computation())
}

fun <T> Observable<T>.performOnComputationThread(): Observable<T> {
    return this.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.computation())
}

fun <T> Single<T>.performOnComputationThread(): Single<T> {
    return this.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.computation())
}

fun <T> Maybe<T>.performOnComputationThread(): Maybe<T> {
    return this.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.computation())
}

fun <T> Completable.performOnComputationThread(): Completable {
    return this.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.computation())
}