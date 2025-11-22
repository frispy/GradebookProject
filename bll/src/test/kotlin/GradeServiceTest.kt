import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GradeServiceTest {

    private val gradeRepository = mockk<IGradeRepository>(relaxed = true)
    private val studentRepository = mockk<IStudentRepository>(relaxed = true)
    private val gradeService = GradeService(gradeRepository, studentRepository)

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `addGrade should throw InvalidGradeException when grade is over 100`() {
        // Arrange
        val studentId = "student1"
        every { studentRepository.getById(studentId) } returns Student(studentId, "Test", "Test")

        // Act & Assert
        assertThrows(InvalidGradeException::class.java) {
            gradeService.addGrade(studentId, "subject1", 105)
        }
    }

    @Test
    fun `addGrade should save grade when valid`() {
        // Arrange
        val studentId = "student1"
        every { studentRepository.getById(studentId) } returns Student(studentId, "Test", "Test")

        // Act
        gradeService.addGrade(studentId, "subject1", 95)

        // Assert
        verify { gradeRepository.add(any()) }
    }

    @Test
    fun `addGrade should throw StudentNotFoundException if student missing`() {
        // Arrange
        every { studentRepository.getById(any()) } returns null

        // Act & Assert
        assertThrows(StudentNotFoundException::class.java) {
            gradeService.addGrade("missing", "sub", 50)
        }
    }

    @Test
    fun `getAverageGrade should calculate correctly`() {
        // Arrange
        val studentId = "student1"
        val grades = listOf(
            Grade("1", studentId, "math", 90, ""),
            Grade("2", studentId, "history", 100, "")
        )

        every { gradeRepository.getByStudentId(studentId) } returns grades

        // Act
        val average = gradeService.getAverageGrade(studentId)

        // Assert
        assertEquals(95.0, average)
    }

    @Test
    fun `getAverageGrade should return 0 when no grades`() {
        // Arrange
        every { gradeRepository.getByStudentId(any()) } returns emptyList()

        // Act
        val average = gradeService.getAverageGrade("student1")

        // Assert
        assertEquals(0.0, average)
    }
}