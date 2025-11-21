class ConsoleUI(
    private val studentService: StudentService,
    private val groupService: GroupService,
    private val gradeService: GradeService
) {
    fun start() {
        println("App started!")
    }
}