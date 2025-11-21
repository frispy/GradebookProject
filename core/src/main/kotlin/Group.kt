import kotlinx.serialization.Serializable

@Serializable

data class Group(
    val id: String,
    var groupName: String,
    var course: Int,
)