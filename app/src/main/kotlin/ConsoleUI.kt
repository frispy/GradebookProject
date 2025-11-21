class ConsoleUI(
    private val studentService: StudentService,
    private val groupService: GroupService,
    private val gradeService: GradeService,
    private val subjectService: SubjectService
) {
    fun start() {
        println("App started!")

        testRun()
    }

    fun testRun(): Unit {
        println("Creating new subject...")
        val testSubjectId = subjectService.createSubject("testSubject")
        println("Creating new Group...")
        val testGroupId = groupService.createGroup("testGroup", 5)
        println("Creating new Student...")
        val testStudentId = studentService.createStudent("firstNameTest", "LastNameTest", testGroupId)

        println("Adding grade to the student...")
        gradeService.addGrade(testStudentId, testSubjectId, 5)

        println("Test Results")
        println("${gradeService.getAllGrade()}")
        println("Test Completed")
    }
}