package ${dtoPackage}

import java.time.LocalDateTime
import java.util.UUID

data class ${dtoName}(
    val id: UUID? = null,
<#list fields as field>
<#if field.name != "id">
    val ${field.name}: ${field.kotlinType}? = null,
</#if>
</#list>
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)
