package dv.serg.topnews.ui.fragment

import dv.serg.topnews.util.StatefulComponent
import kotlin.properties.Delegates

abstract class StatefulFragment : LoggingFragment(), StatefulComponent {

    private val stateComponent: StatefulComponent
        get() = this

    protected var state: StatefulComponent.State by Delegates.observable(StatefulComponent.State.IDLE) { _, _, newValue ->
        when (newValue) {
            StatefulComponent.State.IDLE -> {
                stateComponent.onIdle()
            }
            StatefulComponent.State.COMPLETE -> {
                stateComponent.onComplete()
            }
            StatefulComponent.State.ERROR -> {
                stateComponent.onError()
            }
            StatefulComponent.State.LOADING -> {
                stateComponent.onLoading()
            }
        }
    }

}
