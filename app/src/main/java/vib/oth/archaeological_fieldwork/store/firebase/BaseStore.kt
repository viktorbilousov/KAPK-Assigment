package vib.oth.archaeological_fieldwork.store.firebase

interface BaseStore<T> {
    fun findAll(): List<T>
    fun create(obj: T)
    fun update(obj: T)
    fun delete(obj: T)
    fun findById(id:Long) : T?
    fun fetch(ready: () -> Unit)
    fun clear()
}