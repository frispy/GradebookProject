interface ISubjectRepository {
    fun getAll(): List<Subject>
    fun getById(id: Int): Subject?
    fun add(subject: Subject)
    fun delete(id: Int)
    fun saveChanges()
}