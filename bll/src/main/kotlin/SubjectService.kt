import java.util.UUID

class SubjectService(
    private val subjectRepository: ISubjectRepository,
    private val gradeRepository: IGradeRepository
) {

    fun getAllSubjects(): List<Subject> = subjectRepository.getAll()

    fun getSubjectById(id: String): Subject {
        return subjectRepository.getById(id)
            ?: throw SubjectNotFoundException(id)
    }

    // Returns String (UUID)
    fun createSubject(subjectName: String): String {
        if (subjectName.isBlank()) {
            throw ValidationException("Subject name cannot be empty")
        }

        val newId = UUID.randomUUID().toString()

        val newSubject = Subject(
            id = newId,
            subjectName = subjectName
        )

        subjectRepository.add(newSubject)
        return newId
    }

    fun updateSubject(id: String, newName: String) {
        val existingSubject = subjectRepository.getById(id)
            ?: throw SubjectNotFoundException(id)

        if (newName.isBlank()) throw ValidationException("Subject name cannot be empty")

        val updatedSubject = existingSubject.copy(subjectName = newName)
        subjectRepository.update(updatedSubject)
    }

    fun removeSubject(id: String) {
        if (subjectRepository.getById(id) == null) {
            throw SubjectNotFoundException(id)
        }

        // delete associated grades
        val grades = gradeRepository.getBySubjectId(id)
        grades.forEach { grade ->
            gradeRepository.delete(grade.id)
        }

        subjectRepository.delete(id)
    }
}