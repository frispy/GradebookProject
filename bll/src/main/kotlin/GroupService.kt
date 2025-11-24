import java.util.UUID

class GroupService(
    private val groupRepository: IGroupRepository,
    private val studentRepository: IStudentRepository
) : BaseService<Group>(groupRepository) {

    fun getGroupById(id: String) = getByIdOrThrow(id)
    fun getAllGroups() = getAll() // alias so ConsoleUI doesn't break

    fun createGroup(name: String, course: Int): String {
        if (name.isBlank()) throw ValidationException("Empty name")
        if (course !in 1..6) throw ValidationException("Invalid course")

        val id = UUID.randomUUID().toString()
        groupRepository.add(Group(id, name, course))
        return id
    }

    fun updateGroup(id: String, name: String, course: Int) {
        val existing = getByIdOrThrow(id)
        if (name.isBlank()) throw ValidationException("Empty name")
        groupRepository.update(existing.copy(groupName = name, course = course))
    }

    fun removeGroup(id: String) {
        // check if group exists
        getByIdOrThrow(id)

        // remove group from students that may have had it
        val students = studentRepository.getAll().filter { it.groupId == id }
        students.forEach {
            studentRepository.update(it.copy(groupId = null))
        }

        // call remove function from BaseSerbice
        super.remove(id)
    }
}