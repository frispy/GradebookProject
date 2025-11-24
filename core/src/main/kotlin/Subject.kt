import kotlinx.serialization.Serializable

@Serializable

data class Subject (
    override val id: String,
    val subjectName: String,
): Identifiable