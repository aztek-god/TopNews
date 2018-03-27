package dv.serg.topnews.data

class ObservableProperty<T>(private var propertyValue: T) {
    private var observers: MutableList<PropertyObserver<T>> = ArrayList()

    fun updateValue(newValue: T) {
        if (newValue != propertyValue) {
            propertyValue = newValue
            observers.forEach { it.observe(propertyValue) }
        }
    }

    fun registerAsObserver(observer: PropertyObserver<T>) {
        this.observers.add(observer)
    }

    fun clear() {
        observers.clear()
    }

    interface PropertyObserver<T> {
        fun observe(value: T)
    }
}