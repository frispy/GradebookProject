import kotlinx.serialization.builtins.ListSerializer


class GradeRepository(path: String) : BaseRepository<Grade>(path, ListSerializer(Grade.serializer())), IGradeRepository {
    override fun getByStudentId(studentId: String): List<Grade> = items.filter { it.studentId == studentId }
    override fun getBySubjectId(subjectId: String): List<Grade> = items.filter { it.subjectId == subjectId }
}