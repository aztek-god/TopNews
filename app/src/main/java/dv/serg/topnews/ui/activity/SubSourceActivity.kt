package dv.serg.topnews.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dv.serg.topnews.R
import kotlinx.android.synthetic.main.activity_subscribe.*

class SubSourceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribe)
        setSupportActionBar(toolbar)
    }

    companion object {
        val SUBSCRIPTION_RESULT_CODE = 106
    }
}

