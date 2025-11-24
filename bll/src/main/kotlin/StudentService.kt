import java.util.UUID

class StudentService(
    private val studentRepository: IStudentRepository,
    private val groupRepository: IGroupRepository
) : BaseService<Student>(studentRepository) { // Наслідуємо BaseService

    fun getAllStudents() = getAll() // alias so ConsoleUI doesn't break

    // specific search functions
    fun searchStudents(query: String): List<Student> {
        return getAll().filter {
            it.firstName.contains(query, ignoreCase = true) ||
                    it.lastName.contains(query, ignoreCase = true)
        }
    }

    fun getStudentsByGroup(groupId: String): List<Student> {
        return getAll().filter { it.groupId == groupId }
    }

    fun createStudent(firstName: String, lastName: String, groupId: String? = null): String {
        if (!validateStudentData(firstName, lastName)) {
            throw ValidationException("Name too short")
        }
        if (groupId != null && groupRepository.getById(groupId) == null) {
            throw GroupNotFoundException(groupId)
        }

        val id = UUID.randomUUID().toString()
        val student = Student(id, firstName, lastName, groupId)
        studentRepository.add(student)
        return id
    }

    fun updateStudent(id: String, firstName: String, lastName: String, groupId: String? = null) {
        val existing = getByIdOrThrow(id) // Використовуємо метод з BaseService

        if (groupId != null && groupRepository.getById(groupId) == null) {
            throw GroupNotFoundException(groupId)
        }

        val updated = existing.copy(
            firstName = firstName.ifBlank { existing.firstName },
            lastName = lastName.ifBlank { existing.lastName },
            groupId = groupId ?: existing.groupId
        )
        studentRepository.update(updated)
    }

    fun removeStudent(id: String) = remove(id)

    private fun validateStudentData(f: String, l: String) = f.length > 1 && l.length > 1
}