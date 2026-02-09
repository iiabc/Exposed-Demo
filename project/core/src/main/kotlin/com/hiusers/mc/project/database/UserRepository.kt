package com.hiusers.mc.project.database

import com.hiusers.mc.project.database.table.UsersTable
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import java.time.LocalDateTime

/**
 * 用户数据访问对象
 * @author ExampleProject
 */
@RuntimeDependencies(
    RuntimeDependency(
        "org.jetbrains.exposed:exposed-core:1.0.0",
        relocate = ["!org.jetbrains.exposed.v1", "!com.hiusers.mc.project.libs.exposed"]
    ),
    RuntimeDependency(
        "org.jetbrains.exposed:exposed-dao:1.0.0",
        relocate = ["!org.jetbrains.exposed.v1", "!com.hiusers.mc.project.libs.exposed"]
    ),
    RuntimeDependency(
        "org.jetbrains.exposed:exposed-jdbc:1.0.0",
        relocate = ["!org.jetbrains.exposed.v1", "!com.hiusers.mc.project.libs.exposed"]
    ),
    RuntimeDependency(
        "org.jetbrains.exposed:exposed-java-time:1.0.0",
        relocate = ["!org.jetbrains.exposed.v1", "!com.hiusers.mc.project.libs.exposed"]
    )
)
object UserRepository {
    
    /**
     * 创建用户
     */
    fun createUser(name: String, email: String? = null, age: Int = 0): Int {
        return transaction {
            UsersTable.insertAndGetId {
                it[UsersTable.name] = name
                it[UsersTable.email] = email
                it[UsersTable.age] = age
                it[UsersTable.createdAt] = LocalDateTime.now()
            }.value
        }
    }
    
    /**
     * 根据 ID 查询用户
     */
    fun getUserById(id: Int): User? {
        return transaction {
            UsersTable.selectAll()
                .where { UsersTable.id eq id }
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }
    
    /**
     * 根据名称查询用户
     */
    fun getUserByName(name: String): User? {
        return transaction {
            UsersTable.selectAll()
                .where { UsersTable.name eq name }
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }
    
    /**
     * 查询所有用户
     */
    fun getAllUsers(): List<User> {
        return transaction {
            UsersTable.selectAll()
                .map { rowToUser(it) }
        }
    }
    
    /**
     * 更新用户信息
     */
    fun updateUser(id: Int, name: String? = null, email: String? = null, age: Int? = null): Boolean {
        return transaction {
            val updated = UsersTable.update({ UsersTable.id eq id }) {
                name?.let { nameValue -> it[UsersTable.name] = nameValue }
                email?.let { emailValue -> it[UsersTable.email] = emailValue }
                age?.let { ageValue -> it[UsersTable.age] = ageValue }
                it[UsersTable.updatedAt] = LocalDateTime.now()
            }
            updated > 0
        }
    }
    
    /**
     * 删除用户
     */
    fun deleteUser(id: Int): Boolean {
        return transaction {
            val deleted = UsersTable.deleteWhere { UsersTable.id eq id }
            deleted > 0
        }
    }
    
    /**
     * 将数据库行转换为 User 对象
     */
    private fun rowToUser(row: ResultRow): User {
        return User(
            id = row[UsersTable.id].value,
            name = row[UsersTable.name],
            email = row[UsersTable.email],
            age = row[UsersTable.age],
            createdAt = row[UsersTable.createdAt],
            updatedAt = row[UsersTable.updatedAt]
        )
    }
}

/**
 * 用户数据类
 */
data class User(
    val id: Int,
    val name: String,
    val email: String?,
    val age: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)
