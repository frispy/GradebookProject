import kotlinx.serialization.Serializable
import java.io.Serializable as JavaSerializable

@Serializable

data class Student (
    val id: String,
    var firstName: String,
    var lastName: String,
    var groupId: String? = null,
): JavaSerializable