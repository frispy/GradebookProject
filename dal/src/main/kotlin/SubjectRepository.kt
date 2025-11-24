import kotlinx.serialization.builtins.ListSerializer

class SubjectRepository(path: String) : BaseRepository<Subject>(path, ListSerializer(Subject.serializer())), ISubjectRepository