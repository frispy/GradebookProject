interface ISubjectRepository {
    fun getAll(): List<Subject>
    fun getById(id: String): Subject?
    fun add(subject: Subject)
    fun update(subject: Subject)
    fun delete(id: String)
    fun saveChanges()
}

  