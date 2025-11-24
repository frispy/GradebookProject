interface IBaseRepository<T : Identifiable> {
    fun getAll(): List<T>
    fun getById(id: String): T?
    fun add(item: T)
    fun update(item: T)
    fun delete(id: String)
    fun saveChanges()
}