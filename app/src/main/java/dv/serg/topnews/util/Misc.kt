package dv.serg.topnews.util

import android.support.v4.widget.SwipeRefreshLayout
import android.util.DisplayMetrics
import dv.serg.topnews.app.AppContext
import org.jetbrains.anko.windowManager

interface SwitchActivity {
    fun showDataLayout()

    fun showErrorLayout()
}

interface StatefulComponent {
    enum class State {
        IDLE, LOADING, COMPLETE, ERROR;
    }

    fun onLoading()

    fun onIdle()

    fun onComplete()

    fun onError()
}

sealed class Outcome<T> {
    data class Progress<T>(var isLoading: Boolean, var type: Type = Type.UPDATABLE) : Outcome<T>()

    data class Success<T>(var data: T, var type: Type = Type.UPDATABLE) : Outcome<T>()

    enum class Type {
        UPDATABLE, APPENDABLE
    }

    data class Failure<T>(val e: Throwable) : Outcome<T>()

    companion object {
        fun <T> loading(isLoading: Boolean, type: Type = Type.UPDATABLE): Outcome<T> = Progress(isLoading, type)

        fun <T> success(data: T, type: Type = Type.UPDATABLE): Outcome<T> = Success(data, type)

        fun <T> failure(e: Throwable): Outcome<T> = Failure(e)
    }
}

fun <E> MutableList<E>.update(data: List<E>) {
    clear()
    addAll(data)
}

fun SwipeRefreshLayout.showAtBottomMode() {
    val displayMetrics = DisplayMetrics()
    AppContext.appContext.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels

    setProgressViewOffset(true, 0, (height / 100) * 80)
}