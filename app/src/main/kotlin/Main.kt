fun main() {
    val studentRepo = StudentRepository("students.json")
    val groupRepo = GroupRepository("groups.json")
    val gradeRepo = GradeRepository("grades.json")

    val studentService = StudentService(studentRepo, groupRepo)
    val groupService = GroupService(groupRepo, studentRepo)
    val gradeService = GradeService(gradeRepo, studentRepo)

    val ui = ConsoleUI(studentService, groupService, gradeService)
    println("Starting the app!")
    ui.start()
}