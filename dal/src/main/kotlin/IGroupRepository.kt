interface IGroupRepository {
    fun getAll(): List<Group>
    fun getById(id: String): Group?
    fun add(group: Group)
    fun update(group: Group)
    fun delete(id: String)
    fun saveChanges()
}