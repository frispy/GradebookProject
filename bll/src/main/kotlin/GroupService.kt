import java.util.UUID

class GroupService(
    private val groupRepository: IGroupRepository,
    private val studentRepository: IStudentRepository
)  {

    fun getAllGroups(): List<Group> = groupRepository.getAll()

    fun getGroupById(id: String): Group {
        return groupRepository.getById(id)
            ?: throw GroupNotFoundException(id)
    }

    fun createGroup(groupName: String, course: Int): String {
        if (groupName.isBlank()) throw ValidationException("Group name cannot be empty")
        if (course !in 1..5) throw ValidationException("Course must be between 1 and 5")

        val groupId = UUID.randomUUID().toString()

        val newGroup = Group(
            groupName = groupName,
            course = course,
            id = groupId,
            )

        groupRepository.add(newGroup)

        return groupId
    }

    fun removeGroup(groupId: String) {
        if (groupRepository.getById(groupId) != null) {
            val studentsInGroup = studentRepository.getAll().filter { it.groupId == groupId }

            // remove the group from the students that may have had it
            for (student in studentsInGroup) {
                val unlinkedStudent = student.copy(groupId = null)
                studentRepository.update(unlinkedStudent)
            }

            groupRepository.delete(groupId)
        } else {
            throw GroupNotFoundException(groupId)
        }
    }


    fun updateGroup(id: String, newName: String, newCourse: Int) {
        val existingGroup = groupRepository.getById(id) ?: throw GroupNotFoundException(id)

        if (newName.isBlank()) throw ValidationException("Group name cannot be empty")

        val updatedGroup = existingGroup.copy(
            groupName = newName,
            course = newCourse
        )

        groupRepository.update(updatedGroup)
    }



}