
import java.util.UUID
class GradeService(
    private val gradeRepository: IGradeRepository,
    private val studentRepository: IStudentRepository
) : BaseService<Grade>(gradeRepository) {

    fun getAllGrade() = getAll() // alias so ConsoleUI doesn't break

    fun addGrade(studentId: String, subjectId: String, value: Int) {
        if (studentRepository.getById(studentId) == null) throw StudentNotFoundException(studentId)
        if (value !in 0..100) throw InvalidGradeException(value)

        val grade = Grade(
            id = UUID.randomUUID().toString(),
            studentId = studentId,
            subjectId = subjectId,
            value = value,
            date = java.time.LocalDate.now().toString()
        )
        gradeRepository.add(grade)
    }

    fun updateGrade(id: String, newValue: Int) {
        if (newValue !in 0..100) {
            throw InvalidGradeException(newValue)
        }
        val existing = getByIdOrThrow(id)
        gradeRepository.update(existing.copy(value = newValue))
    }

    fun getAverageGrade(studentId: String): Double {
        val grades = gradeRepository.getByStudentId(studentId)
        if (grades.isEmpty()) return 0.0
        return grades.map { it.value }.average()
    }

    fun getStudentsByAverageGrade(averageGradeMin: Double, averageGradeMax: Double): List<String> {
        val students = studentRepository.getAll()
        val result = mutableListOf<String>()

        for (student in students) {
            if (getAverageGrade(student.id)
                in averageGradeMin..averageGradeMax) {
                result.add(student.id)
            }
        }

        return result
    }
}


