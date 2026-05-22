package ${servicePackage}

import ${packageName}.model.${entityName}
import ${packageName}.repository.${repositoryName}
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Service
@Transactional
class ${serviceName}(private val repository: ${repositoryName}) {

    @Transactional(readOnly = true)
    fun findAll(): List<${entityName}> = repository.findAll()

    @Transactional(readOnly = true)
    fun findById(id: UUID): Optional<${entityName}> = repository.findById(id)

    fun save(entity: ${entityName}): ${entityName} = repository.save(entity)

    fun deleteById(id: UUID) = repository.deleteById(id)

    fun delete(entity: ${entityName}) = repository.delete(entity)

    @Transactional(readOnly = true)
    fun existsById(id: UUID): Boolean = repository.existsById(id)

    @Transactional(readOnly = true)
    fun count(): Long = repository.count()
}
