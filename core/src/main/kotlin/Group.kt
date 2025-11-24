import kotlinx.serialization.Serializable
import java.io.Serializable as JavaSerializable


@Serializable

data class Group(
    override val id: String,
    var groupName: String,
    var course: Int,
): Identifiable