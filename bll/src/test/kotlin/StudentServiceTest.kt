import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StudentServiceTest {

    private val studentRepository = mockk<IStudentRepository>(relaxed = true)
    private val groupRepository = mockk<IGroupRepository>(relaxed = true)

    private val studentService = StudentService(studentRepository, groupRepository)

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `createStudent should create student when data is valid`() {
        // Arrange
        val firstName = "Vasyl"
        val lastName = "Vasylko"
        val slot = slot<Student>()

        every { studentRepository.add(capture(slot)) } just Runs

        // Act
        val resultId = studentService.createStudent(firstName, lastName, null)

        // Assert
        verify(exactly = 1) { studentRepository.add(any()) }
        assertEquals(resultId, slot.captured.id)
        assertEquals(firstName, slot.captured.firstName)
    }

    @Test
    fun `createStudent should throw ValidationException when name is empty`() {
        // Arrange
        val firstName = ""
        val lastName = "Vasylko"

        // Act & Assert
        assertThrows(ValidationException::class.java) {
            studentService.createStudent(firstName, lastName)
        }
    }

    @Test
    fun `updateStudent should update fields and group when valid`() {
        // Arrange
        val id = "student1"
        val groupId = "group1"
        val existingStudent = Student(id = id, firstName = "OldName", lastName = "OldLast", groupId = null)
        val existingGroup = Group(id = groupId, groupName = "SE-1", course = 1)

        every { studentRepository.getById(id) } returns existingStudent
        every { groupRepository.getById(groupId) } returns existingGroup

        // Act
        studentService.updateStudent(id, "NewName", "NewLast", groupId)

        // Assert
        verify {
            studentRepository.update(match {
                it.firstName == "NewName" &&
                        it.lastName == "NewLast" &&
                        it.groupId == groupId
            })
        }
    }

    @Test
    fun `updateStudent should keep old values if new ones are blank`() {
        // Arrange
        val id = "student1"
        val existingStudent = Student(id = id, firstName = "KeepMe", lastName = "KeepMeToo", groupId = null)

        every { studentRepository.getById(id) } returns existingStudent

        // Act
        // Передаємо пусті рядки та null групу
        studentService.updateStudent(id, "", "", null)

        // Assert
        verify {
            studentRepository.update(match {
                it.firstName == "KeepMe" && // Ім'я не змінилось
                        it.lastName == "KeepMeToo" &&
                        it.groupId == null
            })
        }
    }

    @Test
    fun `updateStudent should throw GroupNotFoundException when new group does not exist`() {
        // Arrange
        val id = "student1"
        val groupId = "missing_group"

        every { studentRepository.getById(id) } returns Student(id, "Test", "User")
        every { groupRepository.getById(groupId) } returns null // Групи немає

        // Act & Assert
        assertThrows(GroupNotFoundException::class.java) {
            studentService.updateStudent(id, "New", "Name", groupId)
        }
    }

    @Test
    fun `updateStudent should throw StudentNotFoundException when student does not exist`() {
        // Arrange
        every { studentRepository.getById("missing") } returns null

        // Act & Assert
        assertThrows(StudentNotFoundException::class.java) {
            studentService.updateStudent("missing", "Name", "Surname", null)
        }
    }

    @Test
    fun `searchStudents should return filtered list`() {
        // Arrange
        val s1 = Student("1", "John", "Doe", null)
        val s2 = Student("2", "Jane", "Smith", null)
        every { studentRepository.getAll() } returns listOf(s1, s2)

        // Act
        val result = studentService.searchStudents("Doe")

        // Assert
        assertEquals(1, result.size)
        assertEquals("John", result[0].firstName)
    }

    @Test
    fun `removeStudent should call delete in repository`() {
        // Arrange
        val id = "123"
        every { studentRepository.getById(id) } returns Student(id, "A", "B")

        // Act
        studentService.removeStudent(id)

        // Assert
        verify { studentRepository.delete(id) }
    }
}