import kotlinx.serialization.Serializable

@Serializable

data class Grade(
    override val id: String,
    val studentId: String,
    val subjectId: String,
    val value: Int, // grade (i.e, 0-100)
    val date: String
): Identifiable