class GradeService(
    private val gradeRepository: IGradeRepository,
    private val studentRepository: IStudentRepository
) {
    fun getAllGrade() = gradeRepository.getAll()

    fun addGrade(studentId: String, subjectId: String, value: Int) {

        // validate if student exists
        if (studentRepository.getById(studentId) == null) {
            throw StudentNotFoundException(studentId)
        }

        // validate if grade value is in range
        if (value !in 1..100) {
            throw InvalidGradeException(value)
        }


        val grade = Grade(
            id = java.util.UUID.randomUUID().toString(),
            studentId = studentId,
            subjectId = subjectId.toString(),
            value = value,
            date = java.time.LocalDate.now().toString()
        )

        gradeRepository.add(grade)
    }

    fun getAverageGrade(studentId: String): Double {
        val grades = gradeRepository.getByStudentId(studentId)
        if (grades.isEmpty()) return 0.0
        return grades.map { it.value }.average()
    }

    // returns ids of students with an average grade in range of values

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