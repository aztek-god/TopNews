package dv.serg.topnews.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.view.*
import dv.serg.lib.utils.logd

/**
 * This class is only for learning purposes
 */

/**
 * See detailed fragment lifecycle from here: https://github.com/xxv/android-lifecycle
 */
abstract class LoggingFragment : Fragment() {

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @CallSuper
    override fun onAttach(context: Context?) {
        logd("${hashCode()}:onAttach{context = $context}")
        super.onAttach(context)
        // in this method add listeners
    }

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created, or re-created.
    // Use onCreate for any standard setup that does not require the activity to be fully created
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        logd("${hashCode()}:onCreate{savedInstanceState = $savedInstanceState}")
        super.onCreate(savedInstanceState)

        // for example init adapter here
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        logd("${hashCode()}:onCreateView{inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState}")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logd("${hashCode()}:onViewCreated{view = $view, savedInstanceState = $savedInstanceState}")
        super.onViewCreated(view, savedInstanceState)

    }

    // This method is called after the parent Activity's onCreate() method has completed.
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
    // At this point, it is safe to search for activity View objects by their ID, for example.
    @CallSuper
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        logd("${hashCode()}:onActivityCreated{savedInstanceState = $savedInstanceState}")
        super.onActivityCreated(savedInstanceState)
    }

    @CallSuper
    override fun onStart() {
        logd("${hashCode()}:onStart")
        super.onStart()
    }

    @CallSuper
    override fun onResume() {
        logd("${hashCode()}:onResume")
        super.onResume()
    }

    @CallSuper
    override fun onPause() {
        logd("${hashCode()}:onPause")
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        logd("${hashCode()}:onStop")
        super.onStop()
    }

    @CallSuper
    override fun onDestroyView() {
        logd("${hashCode()}:onDestroyView")
        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        logd("${hashCode()}:onDestroy")
        super.onDestroy()
    }

    // This method is called when the fragment is no longer connected to the Activity
    // Any references saved in onAttach should be nulled out here to prevent memory leaks.
    @CallSuper
    override fun onDetach() {
        logd("${hashCode()}:onDetach")
        super.onDetach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        logd("${hashCode()}:onSaveInstanceState{outState = $outState}")
        super.onSaveInstanceState(outState)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        logd("${hashCode()}:onPrepareOptionsMenu{menu = $menu}")
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        logd("${hashCode()}:onCreateOptionsMenu{menu = $menu, inflater = $inflater}")
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        logd("${hashCode()}:onViewStateRestored{savedInstanceState = $savedInstanceState}")
        super.onViewStateRestored(savedInstanceState)
    }
}
