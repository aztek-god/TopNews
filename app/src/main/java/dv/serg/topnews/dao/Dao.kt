package dv.serg.topnews.dao

interface Dao<T> {
    fun insert(item: T) {
        throw Exception("This feature is not implemented yet.")
    }


    fun delete(item: T) {
        throw Exception("This feature is not implemented yet.")
    }


    fun update(item: T) {
        throw Exception("This feature is not implemented yet.")
    }


    fun get(id: Long): T {
        throw Exception("This feature is not implemented yet.")
    }


    fun getAll(): List<T> {
        throw Exception("This feature is not implemented yet.")
    }

}