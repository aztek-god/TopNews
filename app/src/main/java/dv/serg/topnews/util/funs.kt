package dv.serg.topnews.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import dv.serg.lib.utils.logd
import dv.serg.topnews.R


fun openBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val activities = context.packageManager.queryIntentActivities(intent, 0)
    val isIntentSafe = activities.size > 0
    context.logd("isIntentSafe = ${activities.size}")
    if (isIntentSafe) {
        val chooser = Intent.createChooser(intent, context.getString(R.string.intent_chooser))
        context.startActivity(chooser)
    }
}
