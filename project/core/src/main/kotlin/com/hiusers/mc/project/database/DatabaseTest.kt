package com.hiusers.mc.project.database

import com.hiusers.mc.project.database.table.UsersTable
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * Exposed 数据库测试类
 * @author ExampleProject
 */
object DatabaseTest {

    @Awake(LifeCycle.ENABLE)
    fun load() {
        DatabaseTest.initDatabase()
        DatabaseTest.runTest()
    }
    
    /**
     * 初始化数据库连接（使用 H2 内存数据库进行测试）
     */
    fun initDatabase() {
        Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver",
            user = "root",
            password = ""
        )
        
        // 创建表
        transaction {
            SchemaUtils.create(UsersTable)
        }
    }
    
    /**
     * 运行测试
     */
    fun runTest() {
        println("=== Exposed 数据库测试开始 ===")
        
        // 创建用户
        println("\n1. 创建用户...")
        val userId1 = UserRepository.createUser("张三", "zhangsan@example.com", 25)
        val userId2 = UserRepository.createUser("李四", "lisi@example.com", 30)
        val userId3 = UserRepository.createUser("王五", null, 28)
        println("创建了 3 个用户，ID: $userId1, $userId2, $userId3")
        
        // 根据 ID 查询
        println("\n2. 根据 ID 查询用户...")
        val user1 = UserRepository.getUserById(userId1)
        println("用户 ID $userId1: $user1")
        
        // 根据名称查询
        println("\n3. 根据名称查询用户...")
        val user2 = UserRepository.getUserByName("李四")
        println("用户 '李四': $user2")
        
        // 查询所有用户
        println("\n4. 查询所有用户...")
        val allUsers = UserRepository.getAllUsers()
        allUsers.forEach { user ->
            println("  - ID: ${user.id}, 名称: ${user.name}, 邮箱: ${user.email ?: "无"}, 年龄: ${user.age}")
        }
        
        // 更新用户
        println("\n5. 更新用户信息...")
        val updated = UserRepository.updateUser(userId1, age = 26)
        if (updated) {
            val updatedUser = UserRepository.getUserById(userId1)
            println("更新后的用户: $updatedUser")
        }
        
        // 删除用户
        println("\n6. 删除用户...")
        val deleted = UserRepository.deleteUser(userId3)
        if (deleted) {
            println("用户 ID $userId3 已删除")
        }
        
        // 再次查询所有用户
        println("\n7. 删除后的所有用户...")
        val remainingUsers = UserRepository.getAllUsers()
        remainingUsers.forEach { user ->
            println("  - ID: ${user.id}, 名称: ${user.name}, 邮箱: ${user.email ?: "无"}, 年龄: ${user.age}")
        }
        
        println("\n=== Exposed 数据库测试完成 ===")
    }
}
