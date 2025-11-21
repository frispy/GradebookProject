import kotlinx.serialization.Serializable

@Serializable

data class Subject (
    val id: Int,
    val subjectName: String,
)