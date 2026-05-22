package ${packageName}

import javax.persistence.*
import java.time.LocalDateTime
import java.util.UUID
<#list imports as import>
${import}
</#list>

@Entity
@Table(name = "${tableName}")
class ${entityName}(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: UUID? = null,
<#list fields as field>
<#if field.name != "id">

    @Column(name = "${field.name}"<#if field.unique>, unique = true</#if><#if !field.nullable>, nullable = false</#if>)
    var ${field.name}: ${field.kotlinType}? = null,
</#if>
</#list>

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
) {
    @PrePersist
    protected fun onCreate() {
        createdAt = LocalDateTime.now()
        updatedAt = LocalDateTime.now()
    }

    @PreUpdate
    protected fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
