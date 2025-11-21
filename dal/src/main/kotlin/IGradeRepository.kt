interface IGradeRepository {
    fun getAll(): List<Grade>
    fun getById(id: String): Grade?
    fun add(grade: Grade)
    fun update(grade: Grade)
    fun delete(id: String)

    fun getByStudentId(studentId: String): List<Grade>
    fun getBySubjectId(subjectId: String): List<Grade>

    fun saveChanges()
}