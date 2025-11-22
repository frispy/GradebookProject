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
    fun `assignGroupToStudent should update student when both exist`() {
        // Arrange
        val studentId = "student1"
        val groupId = "group1"

        val existingStudent = Student(id = studentId, firstName = "Andriy", lastName = "Yermak", groupId = null)
        val existingGroup = Group(id = groupId, groupName = "SE-1", course = 1)

        every { studentRepository.getById(studentId) } returns existingStudent
        every { groupRepository.getById(groupId) } returns existingGroup

        // Act
        studentService.assignGroupToStudent(studentId, groupId)

        // Assert
        verify {
            studentRepository.update(match { it.groupId == groupId })
        }
    }

    @Test
    fun `assignGroupToStudent should throw GroupNotFoundException when group does not exist`() {
        // Arrange
        val studentId = "14120388-3434-4683-8313-3cf57e2082d8"
        val groupId = "missing_group"

        every { studentRepository.getById(studentId) } returns Student(studentId, "A", "B")
        every { groupRepository.getById(groupId) } returns null // group does not exist

        // Act & Assert
        assertThrows(GroupNotFoundException::class.java) {
            studentService.assignGroupToStudent(studentId, groupId)
        }
    }

    @Test
    fun `assignGroupToStudent should throw StudentNotFoundException when student does not exist`() {
        // Arrange
        every { studentRepository.getById("missing") } returns null

        // Act & Assert
        assertThrows(StudentNotFoundException::class.java) {
            studentService.assignGroupToStudent("missing", "group1")
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