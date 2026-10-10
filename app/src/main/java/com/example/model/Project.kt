package com.example.model

enum class ProjectCategory(val label: String) {
    WEB_APP("Web App"),
    MOBILE_APP("Mobile App"),
    BACKEND_API("Backend API"),
    CLOUD_INFRA("Cloud Infra"),
    INTERNAL_TOOL("Internal Tool")
}

enum class ProjectStatus(val label: String) {
    IN_PROGRESS("In Progress"),
    ACTIVE("Active / Deployed"),
    IN_REVIEW("In Review"),
    PLANNING("Planning"),
    ARCHIVED("Archived")
}

data class ProjectTask(
    val id: String,
    val title: String,
    val isCompleted: Boolean = false
)

data class Project(
    val id: String,
    val name: String,
    val description: String,
    val category: ProjectCategory,
    val status: ProjectStatus,
    val clientOrDepartment: String,
    val progress: Int, // 0 - 100
    val repoUrl: String,
    val liveUrl: String = "",
    val techStack: List<String>,
    val assignedDevIds: List<String>,
    val tasks: List<ProjectTask> = emptyList(),
    val targetDate: String
)
