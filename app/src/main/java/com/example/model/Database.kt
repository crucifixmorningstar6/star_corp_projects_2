package com.example.model

enum class DatabaseEngine(val label: String) {
    POSTGRESQL("PostgreSQL"),
    CLOUD_SQL("Cloud SQL"),
    FIRESTORE("Firestore"),
    REDIS("Redis"),
    MONGODB("MongoDB"),
    MYSQL("MySQL")
}

enum class DatabaseStatus(val label: String) {
    HEALTHY("Healthy"),
    HIGH_LOAD("High Load"),
    SYNCING("Syncing"),
    MAINTENANCE("Maintenance")
}

data class DatabaseInstance(
    val id: String,
    val name: String,
    val engine: DatabaseEngine,
    val hostEndpoint: String,
    val region: String,
    val environment: Environment,
    val status: DatabaseStatus,
    val storageUsedGb: Double,
    val storageTotalGb: Double,
    val activeConnections: Int,
    val backupSchedule: String,
    val linkedProjectId: String? = null
)
