import java.util.UUID

class StudentService(private val studentRepository: IStudentRepository, private val groupRepository: IGroupRepository) {
    fun getAllStudents() = studentRepository.getAll()

    fun searchStudents(query: String): List<Student> {
        return studentRepository.getAll().filter {
            it.firstName.contains(query, ignoreCase = true) ||
                    it.lastName.contains(query, ignoreCase = true)
        }
    }

    fun createStudent(firstName: String, lastName: String, groupId: String? = null) {
        val newStudent = Student(
            firstName = firstName,
            lastName = lastName,
            groupId = groupId,
            id = UUID.randomUUID().toString(),
        )

        studentRepository.add(newStudent)
    }

    fun removeStudent(id: String) {
        studentRepository.delete(id)
    }

    fun updateStudent(id: String, firstName: String, lastName: String, groupId: String? = null) {
        val existingStudent = studentRepository.getById(id)
            ?: throw StudentNotFoundException("Student with ID $id not found.")

        val updatedStudent = existingStudent.copy(
            firstName = firstName.takeIf { it.isNotBlank() } ?: existingStudent.firstName,
            lastName = lastName.takeIf { it.isNotBlank() } ?: existingStudent.lastName,
            groupId = groupId ?: existingStudent.groupId
        )

        studentRepository.update(updatedStudent)
    }

    fun getStudentsByGroup(groupId: String): List<Student> {
        return studentRepository.getAll().filter { it.groupId == groupId }
    }

    fun getStudentsWithoutGroup(): List<Student> {
        return studentRepository.getAll().filter { it.groupId == null }
    }

    fun assignGroupToStudent(studentId: String, groupId: String) {
        val student = studentRepository.getById(studentId)
            ?: throw StudentNotFoundException("Student with ID $studentId not found.")

        val updatedStudent = student.copy(groupId = groupId)
        studentRepository.update(updatedStudent)
    }

    fun removeGroupFromStudent(studentId: String) {
        val student = studentRepository.getById(studentId)
            ?: throw StudentNotFoundException("Student with ID $studentId not found.")

        val updatedStudent = student.copy(groupId = null)
        studentRepository.update(updatedStudent)
    }

    fun studentExists(id: String): Boolean {
        return studentRepository.getAll().any { it.id == id }
    }

    fun validateStudentData(firstName: String, lastName: String): Boolean {
        return firstName.isNotBlank() && lastName.isNotBlank() && firstName.length > 1 && lastName.length > 1
    }

}