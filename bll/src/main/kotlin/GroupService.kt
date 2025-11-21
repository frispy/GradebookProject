import java.util.UUID

class GroupService(private val groupRepository: GroupRepository, private val studentRepository: StudentRepository)  {
    fun createGroup(groupName: String, course: Int) {
        val newGroup = Group(
            groupName = groupName,
            course = course,
            id = UUID.randomUUID().toString(),
            )

        groupRepository.add(newGroup)
    }

    fun removeGroup(groupId: String) {
        if (groupRepository.getById(groupId) != null) {
            groupRepository.delete(groupId)
        } else {
            // TODO replace with exception
            println("Group with specified id $groupId does not exist. It was not removed")
        }
    }
}