package dv.serg.topnews.current

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import dv.serg.topnews.R
import dv.serg.topnews.di.Injector
import kotlinx.android.synthetic.main.activity_subscribe.*
import javax.inject.Inject

class SubSourceActivity : AppCompatActivity() {

    @Inject
    lateinit var vm: SubSourceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribe)
        setSupportActionBar(toolbar)

        // todo in strings.xml
        toolbar.title = "Subscribes"

        Injector.injectSubSourceActivity(this)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


    }


}

