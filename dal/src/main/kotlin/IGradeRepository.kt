interface IGradeRepository : IBaseRepository<Grade> {
    // Grade has specific methods that have to be implemented
    fun getByStudentId(studentId: String): List<Grade>
    fun getBySubjectId(subjectId: String): List<Grade>
}