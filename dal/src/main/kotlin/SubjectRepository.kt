import kotlinx.serialization.json.Json
import java.io.File

class SubjectRepository(private val filePath: String) : ISubjectRepository {
    private val subjects = mutableListOf<Subject>()
    private val json = Json { prettyPrint = true }

    init {
        val file = File(filePath)
        if (file.exists()) {
            val content = file.readText()
            if (content.isNotEmpty()) {
                subjects.addAll(json.decodeFromString<List<Subject>>(content))
            }
        }
    }

    override fun getAll(): List<Subject> = subjects.toList()

    override fun getById(id: Int): Subject? {
        return subjects.find { it.id == id }
    }

    override fun add(subject: Subject) {
        subjects.add(subject)
        saveChanges()
    }

    override fun delete(id: Int) {
        val removed = subjects.removeIf { it.id == id }
        if (removed) {
            saveChanges()
        }
    }

    override fun saveChanges() {
        val content = json.encodeToString(subjects)
        File(filePath).writeText(content)
    }
}