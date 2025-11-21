import kotlinx.serialization.json.Json
import java.io.File

class StudentRepository(private val filePath: String): IStudentRepository {

    val students = mutableListOf<Student>()
    private val json = Json { prettyPrint = true }

    init {
        val file = File(filePath)
        if (file.exists()) {
            val content = file.readText()
            if (content.isNotEmpty()) {
                students.addAll(json.decodeFromString<List<Student>>(content))
            }
        }
    }

    override fun getAll(): List<Student> = students.toList()

    override fun getById(id: String): Student {
        return students.find { it.id == id }
            ?: throw StudentNotFoundException("Student with ID $id not found.")
    }

    override fun add(student: Student) {
        students.add(student)
        saveChanges()
    }

    override fun update(student: Student) {
        val index = students.indexOfFirst { it.id == student.id }

        if (index != -1) {
            students[index] = student
            saveChanges()
        } else {
            throw StudentNotFoundException("Student with ID $index id not found.")
        }
    }

    override fun delete(id: String) {
        students.removeIf { student -> student.id == id }
        saveChanges()
    }

    // writes into json, nothing special
    override fun saveChanges() {
        val content = json.encodeToString(students)
        File(filePath).writeText(content)
    }

}