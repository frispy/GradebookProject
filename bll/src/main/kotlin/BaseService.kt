abstract class BaseService<T : Identifiable>(
    private val repository: IBaseRepository<T>
) {
    fun getAll(): List<T> = repository.getAll()

    protected fun getByIdOrThrow(id: String): T {
        return repository.getById(id)
            ?: throw GradebookException("Entity with ID $id not found")
    }

    open fun remove(id: String) {
        if (repository.getById(id) == null) {
            throw GradebookException("Entity with ID $id not found")
        }
        repository.delete(id)
    }
}