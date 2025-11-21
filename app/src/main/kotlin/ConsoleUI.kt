import java.util.Scanner

class ConsoleUI(
    private val studentService: StudentService,
    private val groupService: GroupService,
    private val gradeService: GradeService,
    private val subjectService: SubjectService
) {

    private val scanner = Scanner(System.`in`)

    fun start() {
        ensureTestData()
        println("Welcome to Gradebook")

        while (true) {
            println()
            println("1. Students")
            println("2. Groups")
            println("3. Subjects & Grades")
            println("4. Search")
            println("0. Exit")

            val choice = prompt("Select")

            when (choice) {
                "1" -> {
                    menu(
                        "Students",
                        listOf(
                            "List" to { listStudents() },
                            "Create" to { createStudent() },
                            "Remove" to { removeStudent() }
                        )
                    )
                }
                "2" -> {
                    menu(
                        "Groups",
                        listOf(
                            "List" to { listGroups() },
                            "Create" to { createGroup() },
                            "Remove" to { removeGroup() }
                        )
                    )
                }
                "3" -> {
                    menu(
                        "Academic",
                        listOf(
                            "List Subjects" to { listSubjects() },
                            "Create Subject" to { createSubject() },
                            "Add Grade" to { addGrade() },
                            "View Performance" to { viewPerformance() }
                        )
                    )
                }
                "4" -> {
                    menu(
                        "Search",
                        listOf(
                            "By Name" to { searchByName() },
                            "By Avg Grade" to { searchByAvg() }
                        )
                    )
                }
                "0" -> return
                else -> println("Invalid.")
            }
        }
    }

    // ----------------------------
    // ----------------------------
    // Students
    // ----------------------------
    // ----------------------------

    private fun listStudents() {
        val students = studentService.getAllStudents()
        for (s in students) {
            println("- ${s.lastName} ${s.firstName}")
        }
    }

    private fun createStudent() = perform {
        val first = prompt("First Name")
        val last = prompt("Last Name")

        val assignGroup = prompt("Assign Group? (y/n)")
        val group =
            if (assignGroup == "y") {
                select(groupService.getAllGroups()) { it.groupName }
            } else {
                null
            }

        studentService.createStudent(first, last, group?.id)
        println("Created.")
    }

    private fun removeStudent() = perform {
        val selected = select(studentService.getAllStudents()) { "${it.lastName} ${it.firstName}" }
        if (selected != null) {
            studentService.removeStudent(selected.id)
            println("Removed.")
        }
    }

    // ----------------------------
    // ----------------------------
    // Groups
    // ----------------------------
    // ----------------------------

    private fun listGroups() {
        val groups = groupService.getAllGroups()
        for (g in groups) {
            println("- ${g.groupName} (Course ${g.course})")
        }
    }

    private fun createGroup() = perform {
        val name = prompt("Name")
        val course = prompt("Course").toInt()
        groupService.createGroup(name, course)
        println("Created.")
    }

    private fun removeGroup() = perform {
        val group = select(groupService.getAllGroups()) { it.groupName }
        if (group != null) {
            groupService.removeGroup(group.id)
            println("Removed.")
        }
    }

    // ----------------------------
    // ----------------------------
    // Subjects & Grades
    // ----------------------------
    // ----------------------------

    private fun listSubjects() {
        val subjects = subjectService.getAllSubjects()
        for (s in subjects) {
            println("- ${s.subjectName}")
        }
    }

    private fun createSubject() = perform {
        val name = prompt("Name")
        subjectService.createSubject(name)
        println("Created.")
    }

    private fun addGrade() = perform {
        val student = select(studentService.getAllStudents()) { "${it.lastName} ${it.firstName}" }
        if (student == null) return@perform

        val subject = select(subjectService.getAllSubjects()) { it.subjectName }
        if (subject == null) return@perform

        val gradeValue = prompt("Grade (0-100)").toInt()
        gradeService.addGrade(student.id, subject.id, gradeValue)
        println("Added.")
    }

    private fun viewPerformance() = perform {
        val student = select(studentService.getAllStudents()) { "${it.lastName} ${it.firstName}" }
        if (student != null) {
            val average = gradeService.getAverageGrade(student.id)
            println("Average Grade: $average")
        }
    }

    // ----------------------------
    // ----------------------------
    // Search
    // ----------------------------
    // ---------------------------

    private fun searchByName() {
        val query = prompt("Query")
        val result = studentService.searchStudents(query)

        for (s in result) {
            println("- ${s.lastName} ${s.firstName}")
        }
    }

    private fun searchByAvg() = perform {
        val minVal = prompt("Min").toDouble()
        val maxVal = prompt("Max").toDouble()

        val ids = gradeService.getStudentsByAverageGrade(minVal, maxVal)

        println("Found ${ids.size} students.")
        for (id in ids) {
            println("- ID: $id")
        }
    }

    // ---------------------------
    // ----------------------------
    // Helpers
    // ---------------------------
    // ----------------------------

    private fun menu(title: String, options: List<Pair<String, () -> Unit>>) {
        println()
        println("--- $title ---")

        for (i in options.indices) {
            val (name, _) = options[i]
            println("${i + 1}. $name")
        }
        println("0. Back")

        val choice = prompt("Select").toIntOrNull()
        if (choice != null && choice in 1..options.size) {
            val action = options[choice - 1].second
            action.invoke()
        }
    }

    private fun <T> select(list: List<T>, label: (T) -> String): T? {
        if (list.isEmpty()) {
            println("List is empty.")
            return null
        }

        println()
        println("Select item:")

        for (i in list.indices) {
            val item = list[i]
            println("${i + 1}. ${label(item)}")
        }

        val number = prompt("Enter #").toIntOrNull()
        if (number == null) return null

        val index = number - 1
        return list.getOrNull(index)
    }

    private fun perform(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun prompt(text: String): String {
        print("$text: ")
        return scanner.next()
    }

    // ---------------------------
    // ---------------------------
    // TEST DATA
    // ---------------------------
    // ----------------------------

    private fun ensureTestData() {
        val noGroups = groupService.getAllGroups().isEmpty()
        val noSubjects = subjectService.getAllSubjects().isEmpty()
        val noStudents = studentService.getAllStudents().isEmpty()

        if (noGroups && noStudents && noSubjects) {
            loadTestData()
        }
    }

    fun loadTestData() {
        println("Loading test data...")

        // Groups
        val groupKP11 = groupService.createGroup("KP-11", 1)
        val groupKP21 = groupService.createGroup("KP-21", 2)
        val groupKP31 = groupService.createGroup("KP-31", 3)

        // Students
        val s1 = studentService.createStudent("Oleksandr", "Koval", groupKP11)
        val s2 = studentService.createStudent("Andrii", "Melnyk", groupKP11)
        val s3 = studentService.createStudent("Iryna", "Shevchenko", groupKP21)
        val s4 = studentService.createStudent("Mariia", "Semenenko", groupKP31)
        val s5 = studentService.createStudent("Vasyl", "Vasylchuk", groupKP31)

        // Subjects
        val subjMath = subjectService.createSubject("Maths")
        val subjProg = subjectService.createSubject("Programming")
        val subjAlgo = subjectService.createSubject("Algorithmic")

        // Grades
        gradeService.addGrade(s1, subjMath, 88)
        gradeService.addGrade(s1, subjProg, 75)

        gradeService.addGrade(s2, subjMath, 92)
        gradeService.addGrade(s2, subjAlgo, 81)

        gradeService.addGrade(s3, subjProg, 69)
        gradeService.addGrade(s3, subjAlgo, 73)

        gradeService.addGrade(s4, subjMath, 95)
        gradeService.addGrade(s4, subjProg, 84)

        gradeService.addGrade(s5, subjAlgo, 78)

        println("Test data loaded.")
    }

}



//    fun testRun(): Unit {
//        println("Creating new subject...")
//        val testSubjectId = subjectService.createSubject("testSubject")
//        println("Creating new Group...")
//        val testGroupId = groupService.createGroup("testGroup", 5)
//        println("Creating new Student...")
//        val testStudentId = studentService.createStudent("firstNameTest", "LastNameTest", testGroupId)
//
//        println("Adding grade to the student...")
//        gradeService.addGrade(testStudentId, testSubjectId, 5)
//
//        println("Test Results")
//        println("${gradeService.getAllGrade()}")
//        println("Test Completed")
//    }
