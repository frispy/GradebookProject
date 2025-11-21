interface IStudentRepository {
    fun getAll(): List<Student>
    fun getById(id: String): Student?
    fun add(student: Student)
    fun update(student: Student)
    fun delete(id: String)
    fun saveChanges()
}