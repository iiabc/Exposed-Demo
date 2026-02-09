package com.hiusers.mc.project.database.table

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.javatime.datetime

/**
 * 用户表定义
 * @author ExampleProject
 */
object UsersTable : IntIdTable("users") {
    
    val name = varchar("name", length = 50).uniqueIndex()
    val email = varchar("email", length = 100).nullable()
    val age = integer("age").default(0)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at").nullable()
    
}
