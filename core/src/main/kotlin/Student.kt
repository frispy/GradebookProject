import kotlinx.serialization.Serializable

@Serializable

data class Student (
    override val id: String,
    var firstName: String,
    var lastName: String,
    var groupId: String? = null,
): Identifiable