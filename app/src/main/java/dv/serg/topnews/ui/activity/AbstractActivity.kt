package dv.serg.topnews.ui.activity

import android.support.v7.app.AppCompatActivity
import dv.serg.topnews.R

abstract class AbstractActivity : AppCompatActivity() {

    override fun onBackPressed() {
        finish()
        exitTransition()
        super.onBackPressed()
    }

    fun enterTransition() {
        overridePendingTransition(R.anim.push_in_right_to_left, R.anim.push_out_right_to_left)
    }

    fun exitTransition() {
        overridePendingTransition(R.anim.push_in_left_to_right, R.anim.push_out_left_to_right)
    }
}