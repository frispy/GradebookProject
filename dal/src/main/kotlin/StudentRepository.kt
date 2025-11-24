import kotlinx.serialization.builtins.ListSerializer

class StudentRepository(path: String) : BaseRepository<Student>(path, ListSerializer(Student.serializer())), IStudentRepository