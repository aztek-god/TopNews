package dv.serg.topnews.ui.activity

import android.os.Bundle
import android.view.MenuItem
import dv.serg.topnews.R
import kotlinx.android.synthetic.main.activity_subscribe.*


class SubSourceActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribe)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
    companion object {
        val SUBSCRIPTION_RESULT_CODE = 106
    }
}

