package dv.serg.topnews.ui

interface ConfigurationAwareComponent {
    fun onSaveConfigChange()
    fun onRestoreConfigChange()
}