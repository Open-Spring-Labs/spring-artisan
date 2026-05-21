package ${repositoryPackage};

import ${packageName}.model.${entityName};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for ${entityName} entity
 * Provides CRUD operations and custom queries
 */
@Repository
public interface ${repositoryName} extends JpaRepository<${entityName}, UUID> {
    // Custom queries can be added here
}
