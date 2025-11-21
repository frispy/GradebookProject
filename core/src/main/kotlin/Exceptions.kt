open class GradebookException(message: String) : Exception(message)

class StudentNotFoundException(id: String) : GradebookException("Student with id $id not found")
class GroupNotFoundException(id: String) : GradebookException("Group with id $id not found")
class InvalidGradeException(value: Int) : GradebookException("Grade $value is invalid. Must be 0-100")
class SubjectNotFoundException(id: String) : GradebookException("Subject with id $id not found")

class ValidationException(message: String) : GradebookException(message)





