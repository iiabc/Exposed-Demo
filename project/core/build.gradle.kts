dependencies {
    // H2 数据库用于测试（添加到主依赖，因为测试代码在 main 目录）
    implementation("com.h2database:h2:2.2.224")
}

// 子模块
taboolib { subproject = true }