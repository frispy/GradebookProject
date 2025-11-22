fun main() {
    val studentRepo = StudentRepository("students.json")
    val groupRepo = GroupRepository("groups.json")
    val gradeRepo = GradeRepository("grades.json")
    val subjectRepo = SubjectRepository("subjects.json")

    val studentService = StudentService(studentRepo, groupRepo)
    val groupService = GroupService(groupRepo, studentRepo)
    val gradeService = GradeService(gradeRepo, studentRepo)
    val subjectService = SubjectService(subjectRepo, gradeRepo)

    val ui = ConsoleUI(studentService, groupService, gradeService, subjectService)
    println("Starting the app!")
    ui.start()
}

