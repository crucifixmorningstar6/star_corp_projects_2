package com.example.model

enum class DevRole(val label: String) {
    LEAD_ARCHITECT("Lead Architect"),
    FULLSTACK_DEV("Fullstack Engineer"),
    FRONTEND_DEV("Frontend Engineer"),
    BACKEND_DEV("Backend Engineer"),
    DEVOPS_SRE("DevOps / SRE"),
    MOBILE_DEV("Mobile Engineer")
}

enum class DevAvailability(val label: String) {
    AVAILABLE("Available"),
    BUSY("Busy"),
    ON_LEAVE("On Leave")
}

data class Developer(
    val id: String,
    val name: String,
    val role: DevRole,
    val email: String,
    val githubHandle: String,
    val skills: List<String>,
    val availability: DevAvailability,
    val assignedProjectIds: List<String> = emptyList(),
    val avatarBgColorHex: Long = 0xFF0077B6
)
