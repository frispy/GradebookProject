import kotlinx.serialization.Serializable

@Serializable

data class Subject (
    val id: String,
    val subjectName: String,
)