package dv.serg.topnews.app

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


fun ImageView.load(uri: String) {
    Observable.fromCallable {
        val load: RequestBuilder<Drawable> = Glide.with(this).load(uri)
        load
    }
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it: RequestBuilder<Drawable> ->
                it.into(this)
            }
}

fun Context.openBrowser(url: String, chooserTitle: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    val chooser = Intent.createChooser(intent, chooserTitle)
    if (isIntentSafe(intent)) {
        intent.data = Uri.parse(url)
        startActivity(chooser)
    }
}

private fun Context.isIntentSafe(intent: Intent): Boolean {
    val activities = packageManager.queryIntentActivities(intent,
            PackageManager.MATCH_DEFAULT_ONLY)
    return activities.size > 0
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