import java.util.UUID

class SubjectService(
    private val subjectRepository: ISubjectRepository,
    private val gradeRepository: IGradeRepository
) : BaseService<Subject>(subjectRepository) {

    fun getAllSubjects() = getAll() // alias so ConsoleUI doesn't break
    fun getSubjectById(id: String) = getByIdOrThrow(id)

    fun createSubject(name: String): String {
        if (name.isBlank()) throw ValidationException("Name empty")
        val id = UUID.randomUUID().toString()
        subjectRepository.add(Subject(id, name))
        return id
    }

    fun updateSubject(id: String, name: String) {
        val existing = getByIdOrThrow(id)
        if (name.isBlank()) throw ValidationException("Name empty")
        subjectRepository.update(existing.copy(subjectName = name))
    }

    fun removeSubject(id: String) {
        getByIdOrThrow(id)

        // delete associated grades
        val grades = gradeRepository.getBySubjectId(id)
        grades.forEach { gradeRepository.delete(it.id) }

        super.remove(id)
    }
}

