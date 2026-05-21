package ${servicePackage};

import ${packageName}.model.${entityName};
import ${packageName}.repository.${repositoryName};
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ${serviceName} {
    
    private final ${repositoryName} repository;

    /**
     * Get all ${entityNameLower}s
     */
    @Transactional(readOnly = true)
    public List<${entityName}> findAll() {
        return repository.findAll();
    }

    /**
     * Get ${entityNameLower} by ID
     */
    @Transactional(readOnly = true)
    public Optional<${entityName}> findById(UUID id) {
        return repository.findById(id);
    }

    /**
     * Create or update ${entityNameLower}
     */
    public ${entityName} save(${entityName} entity) {
        return repository.save(entity);
    }

    /**
     * Delete ${entityNameLower} by ID
     */
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    /**
     * Delete ${entityNameLower}
     */
    public void delete(${entityName} entity) {
        repository.delete(entity);
    }

    /**
     * Check if ${entityNameLower} exists
     */
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    /**
     * Count all ${entityNameLower}s
     */
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }
}
