import java.util.Scanner


class ConsoleUI(
    private val studentService: StudentService,
    private val groupService: GroupService,
    private val gradeService: GradeService,
    private val subjectService: SubjectService
) {

    private val scanner = Scanner(System.`in`).useDelimiter("\n") // useDelimiter fixes input with spaces

    fun start() {
        ensureTestData() // load demo data only when nothing exists
        println("Welcome to Gradebook")

        while (true) {
            println()
            println("1. Students")
            println("2. Groups")
            println("3. Subjects & Grades")
            println("4. Search & Reports")
            println("0. Exit")

            when (prompt("Select")) {
                "1" -> menu("Students", listOf(
                    "List All" to { listStudents() },
                    "Create" to { createStudent() },
                    "Update" to { updateStudent() },
                    "Remove" to { removeStudent() },
                    "View Performance (by Student)" to { viewPerformance() }
                ))
                "2" -> menu("Groups", listOf(
                    "List All" to { listGroups() },
                    "Create" to { createGroup() },
                    "Update" to { updateGroup() },
                    "Remove" to { removeGroup() },
                    "View Group Details" to { viewGroupDetails() }
                ))
                "3" -> menu("Academic", listOf(
                    "List Subjects" to { listSubjects() },
                    "Create Subject" to { createSubject() },
                    "Update Subject" to { updateSubject() },
                    "Remvove Subject" to { removeSubject() },
                    "Add Grade" to { addGrade() },
                    "Update Grade" to { updateGrade() }
                ))
                "4" -> menu("Search", listOf(
                    "By Name" to { searchByName() },
                    "By Group" to { viewGroupDetails() },
                    "By Avg Grade" to { searchByAvg() },
                    "Successful/Failing" to { searchBySuccess() }
                ))
                "0" -> return
                else -> println("Invalid.")
            }
        }
    }

    // ----------------------------
    // ---------------------------
    // Students
    // ----------------------------
    // ---------------------------

    private fun listStudents() {
        // get all students and show basic info
        val students = studentService.getAllStudents()
        if (students.isEmpty()) println("No students.")
        for (s in students) {
            // try to resolve group name safely
            val groupName = s.groupId?.let { gid ->
                try { groupService.getGroupById(gid).groupName } catch(e: Exception) { "Unknown" }
            } ?: "No Group"
            println("- ${s.lastName} ${s.firstName} [$groupName]")
        }
    }

    private fun createStudent() = perform {
        // ask for basic student info
        val first = prompt("First Name")
        val last = prompt("Last Name")
        // optional group assignment
        val group = if (prompt("Assign Group? (y/n)") == "y") {
            select(groupService.getAllGroups()) { it.groupName }
        } else null

        studentService.createStudent(first, last, group?.id)
        println("Created.")
    }

    private fun updateStudent() = perform {
        // select which student to modify
        val selected = select(studentService.getAllStudents()) { "${it.lastName} ${it.firstName}" } ?: return@perform

        // allow skipping values with "."
        val newFirst = prompt("First Name (enter . to keep '${selected.firstName}')")
        val finalFirst = if (newFirst == ".") selected.firstName else newFirst

        val newLast = prompt("Last Name (enter . to keep '${selected.lastName}')")
        val finalLast = if (newLast == ".") selected.lastName else newLast

        // handle group change separately
        val changeGroup = prompt("Change group? (y/n)")
        val newGroup = if (changeGroup == "y") {
            println("1. Select new group\n2. Remove group")
            if (prompt("Choice") == "1") select(groupService.getAllGroups()) { it.groupName } else null
        } else {
            // keep old group if it exists
            if (selected.groupId != null) try { groupService.getGroupById(selected.groupId!!) } catch(e:Exception){ null } else null
        }

        studentService.updateStudent(selected.id, finalFirst, finalLast, newGroup?.id)
        println("Updated.")
    }

    private fun removeStudent() = perform {
        // simple select then delete
        val selected = select(studentService.getAllStudents()) { "${it.lastName} ${it.firstName}" }
        if (selected != null) {
            studentService.removeStudent(selected.id)
            println("Removed.")
        }
    }

    // ----------------------------
    // ---------------------------
    // Groups
    // ----------------------------
    // ---------------------------

    private fun listGroups() {
        // show all groups with their course
        val groups = groupService.getAllGroups()
        if (groups.isEmpty()) println("No groups.")
        for (g in groups) {
            println("- ${g.groupName} (Course ${g.course})")
        }
    }

    private fun createGroup() = perform {
        // basic create flow
        groupService.createGroup(prompt("Name"), prompt("Course").toInt())
        println("Created.")
    }

    private fun updateGroup() = perform {
        val group = select(groupService.getAllGroups()) { it.groupName } ?: return@perform
        val newName = prompt("Name (current: ${group.groupName})")
        val newCourse = prompt("Course (current: ${group.course})").toInt()
        groupService.updateGroup(group.id, newName, newCourse)
        println("Updated.")
    }

    private fun removeGroup() = perform {
        // remove selected group
        val group = select(groupService.getAllGroups()) { it.groupName } ?: return@perform
        groupService.removeGroup(group.id)
        println("Removed.")
    }

    private fun viewGroupDetails() = perform {
        // show students and group's average grade
        val group = select(groupService.getAllGroups()) { it.groupName } ?: return@perform

        println("\n--- Group: ${group.groupName} ---")
        val students = studentService.getStudentsByGroup(group.id)

        if (students.isEmpty()) {
            println("No students in this group.")
        } else {
            var totalAvg = 0.0
            for (s in students) {
                // calculate each student's average grade
                val avg = gradeService.getAverageGrade(s.id)
                totalAvg += avg
                println("- ${s.lastName} ${s.firstName} (Avg: ${"%.2f".format(avg)})")
            }
            // show group average
            println("Group Average Grade: ${"%.2f".format(totalAvg / students.size)}")
        }
    }

    // ----------------------------
    // ----------------------------
    // Subjects & Grades
    // ----------------------------
    // ----------------------------

    private fun listSubjects() {
        // simple subject listing
        val subjects = subjectService.getAllSubjects()
        if (subjects.isEmpty()) println("No subjects.")
        for (s in subjects) {
            println("- ${s.subjectName}")
        }
    }

    private fun createSubject() = perform {
        subjectService.createSubject(prompt("Name"))
        println("Created.")
    }

    private fun updateSubject() = perform {
        val selected = select(subjectService.getAllSubjects()) { it.subjectName } ?: return@perform
        val newName = prompt("New Name (current: ${selected.subjectName})")
        subjectService.updateSubject(selected.id, newName)
        println("Updated.")
    }

    private fun removeSubject() = perform {
        val selected = select(subjectService.getAllSubjects()) { it.subjectName } ?: return@perform
        subjectService.removeSubject(selected.id)
        println("Removed.")
    }

    private fun addGrade() = perform {
        // fully manual grade adding
        val student = select(studentService.getAllStudents()) { "${it.lastName} ${it.firstName}" } ?: return@perform
        val subject = select(subjectService.getAllSubjects()) { it.subjectName } ?: return@perform
        val gradeValue = prompt("Grade (0-100)").toInt()

        gradeService.addGrade(student.id, subject.id, gradeValue)
        println("Added.")
    }

    private fun updateGrade() = perform {
        // step 1: pick student
        val student = select(studentService.getAllStudents()) { "${it.lastName} ${it.firstName}" } ?: return@perform

        // step 2: filter only his grades
        val allGrades = gradeService.getAllGrade().filter { it.studentId == student.id }

        if (allGrades.isEmpty()) {
            println("This student has no grades.")
            return@perform
        }

        println("Select grade to update:")
        // show subject name + current value
        val gradeToUpdate = select(allGrades) { grade ->
            val subjName = try { subjectService.getSubjectById(grade.subjectId).subjectName } catch(e:Exception){"Unknown"}
            "$subjName: ${grade.value}"
        } ?: return@perform

        val newValue = prompt("New Value").toInt()
        gradeService.updateGrade(gradeToUpdate.id, newValue)
        println("Grade updated.")
    }

    private fun viewPerformance() = perform {
        // show all subjects and averages for one student
        val student = select(studentService.getAllStudents()) { "${it.lastName} ${it.firstName}" } ?: return@perform

        val allGrades = gradeService.getAllGrade().filter { it.studentId == student.id }

        println("\n--- Performance: ${student.lastName} ${student.firstName} ---")
        if (allGrades.isEmpty()) {
            println("No grades recorded.")
        } else {
            val gradesBySubject = allGrades.groupBy { it.subjectId }

            for ((subjId, grades) in gradesBySubject) {
                // show values + average per subject
                val subjName = try { subjectService.getSubjectById(subjId).subjectName } catch(e:Exception){"Deleted Subject"}
                val values = grades.joinToString(", ") { it.value.toString() }
                val avg = grades.map { it.value }.average()
                println("$subjName: [$values] (Avg: ${"%.1f".format(avg)})")
            }

            val totalAvg = gradeService.getAverageGrade(student.id)
            println("---")
            println("Total Average: ${"%.2f".format(totalAvg)}")
        }
    }

    // ----------------------------
    // ----------------------------
    // Search
    // ----------------------------
    // ----------------------------

    private fun searchByName() {
        // simple search by contains
        val query = prompt("Query")
        val result = studentService.searchStudents(query)
        if (result.isEmpty()) println("No matches.")
        for (s in result) {
            println("- ${s.lastName} ${s.firstName}")
        }
    }

    private fun searchByAvg() = perform {
        // search for students in avg range
        val minVal = prompt("Min").toDouble()
        val maxVal = prompt("Max").toDouble()
        val ids = gradeService.getStudentsByAverageGrade(minVal, maxVal)

        println("Found ${ids.size} students:")
        ids.forEach { id ->
            val s = try { studentService.getAllStudents().find { it.id == id } } catch(e:Exception){null}
            if (s != null) {
                println("- ${s.lastName} ${s.firstName} (Avg: ${"%.2f".format(gradeService.getAverageGrade(id))})")
            }
        }
    }

    private fun searchBySuccess() = perform {
        // simple success/fail filter
        println("1. Find Successful (Avg >= 75)")
        println("2. Find Failing (Avg < 60)")
        val choice = prompt("Choice")

        val (min, max) = if (choice == "1") (75.0 to 100.0) else (0.0 to 59.9)

        val ids = gradeService.getStudentsByAverageGrade(min, max)
        println("Found ${ids.size} students:")
        ids.forEach { id ->
            val s = try { studentService.getAllStudents().find { it.id == id } } catch(e:Exception){null}
            if (s != null) println("- ${s.lastName} ${s.firstName}")
        }
    }

    // ----------------------------
    // ----------------------------
    // Helpers
    // ----------------------------
    // ----------------------------

    private fun menu(title: String, options: List<Pair<String, () -> Unit>>) {
        // standard reusable menu
        println("\n--- $title ---")
        for (i in options.indices) {
            println("${i + 1}. ${options[i].first}")
        }
        println("0. Back")

        val choice = prompt("Select").toIntOrNull()
        if (choice != null && choice in 1..options.size) {
            options[choice - 1].second.invoke()
        }
    }

    private fun <T> select(list: List<T>, label: (T) -> String): T? {
        // generic select-from-list helper
        if (list.isEmpty()) {
            println("List is empty.")
            return null
        }
        println("\nSelect item:")
        for (i in list.indices) {
            println("${i + 1}. ${label(list[i])}")
        }
        val number = prompt("Enter #").toIntOrNull()
        if (number == null || number !in 1..list.size) return null
        return list[number - 1]
    }

    private fun perform(action: () -> Unit) {
        // wrapper to catch any errors from service layer
        try {
            action()
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun prompt(text: String): String {
        // simple input prompt
        print("$text: ")
        if (scanner.hasNext()) {
            val input = scanner.next().trim()
            return input
        }
        return ""
    }


// ---------------------------
// ---------------------------
// TEST DATA
// ---------------------------
// ---------------------------

// function that needed so I don't manually enter all of that crap

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
        val s6 = studentService.createStudent("Oleg", "Yermak", groupKP31) // loh


        // Subjects
        val subjMath = subjectService.createSubject("Maths")
        val subjProg = subjectService.createSubject("Programming")
        val subjAlgo = subjectService.createSubject("Algorithmic")

        // Grades
        gradeService.addGrade(s1, subjMath, 88)
        gradeService.addGrade(s1, subjProg, 75)

        gradeService.addGrade(s2, subjMath, 92)
        gradeService.addGrade(s2, subjMath, 82)
        gradeService.addGrade(s2, subjAlgo, 81)

        gradeService.addGrade(s3, subjProg, 69)
        gradeService.addGrade(s3, subjAlgo, 73)
        gradeService.addGrade(s4, subjProg, 74)


        gradeService.addGrade(s4, subjMath, 95)
        gradeService.addGrade(s4, subjProg, 84)
        gradeService.addGrade(s4, subjProg, 54)

        gradeService.addGrade(s5, subjAlgo, 78)
        gradeService.addGrade(s5, subjMath, 48)

        gradeService.addGrade(s6, subjAlgo, 28)
        gradeService.addGrade(s6, subjMath, 18)

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
