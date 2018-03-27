package dv.serg.topnews.ui.activity

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import dv.serg.lib.utils.logd

abstract class LoggingActivity : AppCompatActivity() {
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        logd("${hashCode()}:onCreate{savedInstanceState = $savedInstanceState}")
        super.onCreate(savedInstanceState)
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
    override fun onDestroy() {
        logd("${hashCode()}:onDestroy")
        super.onDestroy()
    }

    @CallSuper
    override fun onAttachFragment(fragment: Fragment?) {
        logd("${hashCode()}:onAttachFragment{fragment = $fragment}")
        super.onAttachFragment(fragment)
    }

    @CallSuper
    override fun onContentChanged() {
        logd("${hashCode()}:onContentChanged")
        super.onContentChanged()
    }

    @CallSuper
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        logd("${hashCode()}:onRestoreInstanceState = $savedInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
    }

    @CallSuper
    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        logd("${hashCode()}:onRestoreInstanceState = $savedInstanceState, persistentState = $persistentState")
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    @CallSuper
    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        logd("${hashCode()}:onPostCreate{savedInstanceState = $savedInstanceState, persistentState = $persistentState}")
        super.onPostCreate(savedInstanceState, persistentState)
    }

    @CallSuper
    override fun onPostResume() {
        logd("${hashCode()}:onPostResume")
        super.onPostResume()
    }

    @CallSuper
    override fun onAttachedToWindow() {
        logd("${hashCode()}:onAttachedToWindow")
        super.onAttachedToWindow()
    }

    @CallSuper
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        logd("${hashCode()}:onPrepareOptionsMenu{menu = $menu}")
        return super.onPrepareOptionsMenu(menu)
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        logd("${hashCode()}:onSaveInstanceState{outState = $outState, outPersistentState = $outPersistentState}")
        super.onSaveInstanceState(outState, outPersistentState)
    }

    @CallSuper
    override fun onUserInteraction() {
        logd("${hashCode()}:onUserInteraction")
        super.onUserInteraction()
    }

    @CallSuper
    override fun onUserLeaveHint() {
        logd("${hashCode()}:onUserLeaveHint")
        super.onUserLeaveHint()
    }

    @CallSuper
    override fun onRestart() {
        logd("${hashCode()}:onRestart")
        super.onRestart()
    }

    override fun onBackPressed() {
        logd("${hashCode()}:onBackPressed")
        super.onBackPressed()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        logd("${hashCode()}:onActivityResult{requestCode = $data, resultCode = $resultCode, data = $data}")
        super.onActivityResult(requestCode, resultCode, data)
    }
}