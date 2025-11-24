import kotlinx.serialization.builtins.ListSerializer

class GroupRepository(path: String) : BaseRepository<Group>(path, ListSerializer(Group.serializer())), IGroupRepository