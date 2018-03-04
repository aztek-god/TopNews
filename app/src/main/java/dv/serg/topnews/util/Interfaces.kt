package dv.serg.topnews.util

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