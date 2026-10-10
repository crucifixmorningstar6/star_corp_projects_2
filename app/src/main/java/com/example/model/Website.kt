package com.example.model

enum class Environment(val label: String) {
    PRODUCTION("Production"),
    STAGING("Staging"),
    DEVELOPMENT("Development")
}

enum class ServiceStatus(val label: String) {
    OPERATIONAL("Operational"),
    DEGRADED("Degraded"),
    MAINTENANCE("Maintenance"),
    DOWN("Down")
}

data class Website(
    val id: String,
    val name: String,
    val url: String,
    val environment: Environment,
    val hostingProvider: String, // e.g. "Cloud Run", "AWS ECS", "Vercel", "Firebase"
    val status: ServiceStatus,
    val sslActive: Boolean = true,
    val uptimePercent: Double = 99.99,
    val pingMs: Int = 38,
    val linkedProjectId: String? = null,
    val lastChecked: String = "Just now"
)
