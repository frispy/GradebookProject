import kotlinx.serialization.Serializable
import java.io.Serializable as JavaSerializable


@Serializable

data class Group(
    val id: String,
    var groupName: String,
    var course: Int,
)