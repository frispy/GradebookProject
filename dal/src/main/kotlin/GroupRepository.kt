import kotlinx.serialization.json.Json
import java.io.File

class GroupRepository(private val filePath: String): IGroupRepository {
    val groups = mutableListOf<Group>()
    private val json = Json { prettyPrint = true }

    init {
        val file = File(filePath)
        if (file.exists()) {
            val content = file.readText()
            if (content.isNotEmpty()) {
                groups.addAll(json.decodeFromString<List<Group>>(content))
            }
        }
    }

    override fun getAll(): List<Group> = groups.toList()

    override fun getById(id: String): Group? {
        return groups.find { it.id == id }
    }

    override fun add(group: Group) {
        groups.add(group)
        saveChanges()
    }

    override fun update(group: Group) {
        val index = groups.indexOfFirst { it.id == group.id }

        if (index != -1) {
            groups[index] = group
            saveChanges()
        } else {
            println("Error: Group not found")
            // TODO replace with exception
        }
    }

    override fun delete(id: String) {
        groups.removeIf { it.id == id }
        saveChanges()
    }

    override fun saveChanges() {
        val content = json.encodeToString(groups)
        File(filePath).writeText(content)
    }
}