import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.File

abstract class BaseRepository<T : Identifiable>(
    private val filePath: String,
    private val serializer: KSerializer<List<T>>
) : IBaseRepository<T> {

    protected val items = mutableListOf<T>()
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    init {
        val file = File(filePath)
        if (file.exists()) {
            val content = file.readText()
            if (content.isNotEmpty()) {
                try {
                    items.addAll(json.decodeFromString(serializer, content))
                } catch (e: Exception) {
                    println("Error loading data from $filePath: ${e.message}")
                }
            }
        }
    }

    override fun getAll(): List<T> = items.toList()

    override fun getById(id: String): T? = items.find { it.id == id }

    override fun add(item: T) {
        items.add(item)
        saveChanges()
    }

    override fun update(item: T) {
        val index = items.indexOfFirst { it.id == item.id }
        if (index != -1) {
            items[index] = item
            saveChanges()
        } else {
            throw GradebookException("Item with ID ${item.id} not found")
        }
    }

    override fun delete(id: String) {
        if (items.removeIf { it.id == id }) {
            saveChanges()
        }
    }

    override fun saveChanges() {
        val content = json.encodeToString(serializer, items)
        File(filePath).writeText(content)
    }
}