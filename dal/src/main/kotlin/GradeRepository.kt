import kotlinx.serialization.json.Json
import java.io.File

class GradeRepository(private val filePath: String) : IGradeRepository {
    private val grades = mutableListOf<Grade>()
    private val json = Json { prettyPrint = true }

    init {
        val file = File(filePath)
        if (file.exists()) {
            val content = file.readText()
            if (content.isNotEmpty()) {
                grades.addAll(json.decodeFromString<List<Grade>>(content))
            }
        }
    }

    override fun getAll(): List<Grade> = grades.toList()

    override fun getById(id: String): Grade? {
        return grades.find { it.id == id }
    }

    override fun add(grade: Grade) {
        grades.add(grade)
        saveChanges()
    }

    override fun update(grade: Grade) {
        val index = grades.indexOfFirst { it.id == grade.id }

        if (index != -1) {
            grades[index] = grade
            saveChanges()
        } else {
            // TODO replace this with GradebookException one
            println("Error: Grade not found")
        }
    }

    override fun getByStudentId(studentId: String): List<Grade> {
        return grades.filter { it.studentId == studentId }
    }

    override fun getBySubjectId(subjectId: String): List<Grade> {
        return grades.filter { it.subjectId == subjectId }
    }

    override fun delete(id: String) {
        grades.removeIf { it.id == id }
        saveChanges()
    }

    override fun saveChanges() {
        val content = json.encodeToString(grades)
        File(filePath).writeText(content)
    }
}