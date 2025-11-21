import java.util.UUID

class StudentService(
    private val studentRepository: IStudentRepository,
    private val groupRepository: IGroupRepository
) {
    fun getAllStudents() = studentRepository.getAll()

    fun searchStudents(query: String): List<Student> {
        return studentRepository.getAll().filter {
            it.firstName.contains(query, ignoreCase = true) ||
                    it.lastName.contains(query, ignoreCase = true)
        }
    }

    fun createStudent(firstName: String, lastName: String, groupId: String? = null): String {
        if (!validateStudentData(firstName, lastName)) {
            throw ValidationException("First name and Last name must be at least 2 characters long")
        }

//        // if a group ID is provided - verify it exists
//        if (groupId != null && groupRepository.getById(groupId) == null) {
//            throw GroupNotFoundException(groupId)
//        }

        val studentId = UUID.randomUUID().toString() // UUID may be replaced with some sort of custom ID creation logic that makes no sense for such project

        val newStudent = Student(
            firstName = firstName,
            lastName = lastName,
            groupId = groupId,
            id = studentId,
        )

        studentRepository.add(newStudent)

        return studentId
    }

    fun removeStudent(id: String) {
        studentRepository.delete(id)
    }

    fun updateStudent(id: String, firstName: String, lastName: String, groupId: String? = null) {
        val existingStudent = studentRepository.getById(id)
            ?: throw StudentNotFoundException("Student with ID $id not found.")

        // if a group ID is provided - verify it exists
        if (groupId != null && groupRepository.getById(groupId) == null) {
            throw GroupNotFoundException(groupId)
        }

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
            ?: throw StudentNotFoundException(studentId)

        // check if group exists before assigning

        if (groupRepository.getById(groupId) == null) {
            throw GroupNotFoundException(groupId)
        }

        val updatedStudent = student.copy(groupId = groupId)
        studentRepository.update(updatedStudent)
    }

    fun removeGroupFromStudent(studentId: String) {
        val student = studentRepository.getById(studentId)
            ?: throw StudentNotFoundException("Student with ID $studentId not found.")

        val updatedStudent = student.copy(groupId = null)
        studentRepository.update(updatedStudent)
    }

    fun validateStudentData(firstName: String, lastName: String): Boolean {
        return firstName.isNotBlank() && lastName.isNotBlank() && firstName.length > 1 && lastName.length > 1
    }

}