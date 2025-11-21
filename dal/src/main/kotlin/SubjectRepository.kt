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

    override fun getById(id: String): Subject? {
        return subjects.find { it.id == id }
    }

    override fun delete(id: String) {
        val removed = subjects.removeIf { it.id == id }
        if (removed) {
            saveChanges()
        }
    }

    override fun add(subject: Subject) {
        subjects.add(subject)
        saveChanges()
    }

    override fun update(subject: Subject) {
        val index = subjects.indexOfFirst { it.id == subject.id }

        if (index != -1) {
            subjects[index] = subject
            saveChanges()
        } else {
            println("Error: Subject with ID ${subject.id} not found.")
        }
    }


    override fun saveChanges() {
        val content = json.encodeToString(subjects)
        File(filePath).writeText(content)
    }
}