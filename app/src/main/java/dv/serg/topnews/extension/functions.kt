package dv.serg.topnews.extension

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import dv.serg.lib.utils.logd
import dv.serg.lib.utils.loge
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

fun <T> PublishSubject<T>.toLiveData(compositeDisposable: CompositeDisposable): LiveData<T> {

    val mutableLiveData: MutableLiveData<T> = MutableLiveData()
    compositeDisposable.add(
            subscribe({
                logd("sergdv91:PublishSubject = $it")
                mutableLiveData.value = it
            }, {
                loge("error toLiveData conversion.")
            }, {
                logd("sergdv91:PublishSubject = complete")
            })
    )

    return mutableLiveData
}