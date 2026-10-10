package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class StarCorpRepository {

    private val _projects = MutableStateFlow<List<Project>>(getInitialProjects())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    private val _websites = MutableStateFlow<List<Website>>(getInitialWebsites())
    val websites: StateFlow<List<Website>> = _websites.asStateFlow()

    private val _databases = MutableStateFlow<List<DatabaseInstance>>(getInitialDatabases())
    val databases: StateFlow<List<DatabaseInstance>> = _databases.asStateFlow()

    private val _developers = MutableStateFlow<List<Developer>>(getInitialDevelopers())
    val developers: StateFlow<List<Developer>> = _developers.asStateFlow()

    // --- Projects CRUD ---
    fun addProject(project: Project) {
        _projects.value = listOf(project) + _projects.value
    }

    fun updateProject(updated: Project) {
        _projects.value = _projects.value.map { if (it.id == updated.id) updated else it }
    }

    fun deleteProject(id: String) {
        _projects.value = _projects.value.filter { it.id != id }
    }

    fun toggleTask(projectId: String, taskId: String) {
        _projects.value = _projects.value.map { project ->
            if (project.id == projectId) {
                val updatedTasks = project.tasks.map { task ->
                    if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
                }
                val completedCount = updatedTasks.count { it.isCompleted }
                val newProgress = if (updatedTasks.isNotEmpty()) {
                    (completedCount * 100) / updatedTasks.size
                } else project.progress
                project.copy(tasks = updatedTasks, progress = newProgress)
            } else project
        }
    }

    // --- Websites CRUD ---
    fun addWebsite(website: Website) {
        _websites.value = listOf(website) + _websites.value
    }

    fun updateWebsite(updated: Website) {
        _websites.value = _websites.value.map { if (it.id == updated.id) updated else it }
    }

    fun deleteWebsite(id: String) {
        _websites.value = _websites.value.filter { it.id != id }
    }

    fun pingWebsite(id: String) {
        _websites.value = _websites.value.map { site ->
            if (site.id == id) {
                val newPing = (18..65).random()
                site.copy(
                    pingMs = newPing,
                    lastChecked = "Just now",
                    status = if (site.status == ServiceStatus.DOWN) ServiceStatus.OPERATIONAL else site.status
                )
            } else site
        }
    }

    // --- Databases CRUD ---
    fun addDatabase(database: DatabaseInstance) {
        _databases.value = listOf(database) + _databases.value
    }

    fun updateDatabase(updated: DatabaseInstance) {
        _databases.value = _databases.value.map { if (it.id == updated.id) updated else it }
    }

    fun deleteDatabase(id: String) {
        _databases.value = _databases.value.filter { it.id != id }
    }

    fun triggerBackup(id: String) {
        _databases.value = _databases.value.map { db ->
            if (db.id == id) {
                db.copy(status = DatabaseStatus.HEALTHY)
            } else db
        }
    }

    // --- Developers CRUD ---
    fun addDeveloper(dev: Developer) {
        _developers.value = listOf(dev) + _developers.value
    }

    fun updateDeveloper(updated: Developer) {
        _developers.value = _developers.value.map { if (it.id == updated.id) updated else it }
    }

    fun deleteDeveloper(id: String) {
        _developers.value = _developers.value.filter { it.id != id }
    }

    private companion object {
        fun getInitialDevelopers(): List<Developer> = listOf(
            Developer(
                id = "dev-1",
                name = "Elena Vance",
                role = DevRole.LEAD_ARCHITECT,
                email = "elena.vance@starcorp.io",
                githubHandle = "elenavance-star",
                skills = listOf("Kotlin", "Cloud Architecture", "Compose", "gRPC"),
                availability = DevAvailability.AVAILABLE,
                assignedProjectIds = listOf("proj-1", "proj-4"),
                avatarBgColorHex = 0xFF7209B7
            ),
            Developer(
                id = "dev-2",
                name = "Marcus Chen",
                role = DevRole.FULLSTACK_DEV,
                email = "marcus.chen@starcorp.io",
                githubHandle = "marcuschen-dev",
                skills = listOf("TypeScript", "React", "Node.js", "PostgreSQL"),
                availability = DevAvailability.BUSY,
                assignedProjectIds = listOf("proj-1", "proj-2"),
                avatarBgColorHex = 0xFF0077B6
            ),
            Developer(
                id = "dev-3",
                name = "Aisha Al-Mansoor",
                role = DevRole.DEVOPS_SRE,
                email = "aisha.mansoor@starcorp.io",
                githubHandle = "aishadevops",
                skills = listOf("Kubernetes", "Terraform", "GCP", "CI/CD", "Prometheus"),
                availability = DevAvailability.AVAILABLE,
                assignedProjectIds = listOf("proj-2", "proj-3"),
                avatarBgColorHex = 0xFF00B4D8
            ),
            Developer(
                id = "dev-4",
                name = "Liam O'Connor",
                role = DevRole.BACKEND_DEV,
                email = "liam.oconnor@starcorp.io",
                githubHandle = "liam-oc",
                skills = listOf("Go", "GraphQL", "Redis", "Cloud SQL", "Kafka"),
                availability = DevAvailability.BUSY,
                assignedProjectIds = listOf("proj-3", "proj-4"),
                avatarBgColorHex = 0xFF4361EE
            ),
            Developer(
                id = "dev-5",
                name = "Sofia Reyes",
                role = DevRole.FRONTEND_DEV,
                email = "sofia.reyes@starcorp.io",
                githubHandle = "sofiareyes-ui",
                skills = listOf("Next.js", "TailwindCSS", "Figma", "WebSockets"),
                availability = DevAvailability.AVAILABLE,
                assignedProjectIds = listOf("proj-2"),
                avatarBgColorHex = 0xFFF72585
            ),
            Developer(
                id = "dev-6",
                name = "David Kim",
                role = DevRole.MOBILE_DEV,
                email = "david.kim@starcorp.io",
                githubHandle = "davidkim-mob",
                skills = listOf("Android", "Kotlin Multiplatform", "Jetpack Compose", "Coroutines"),
                availability = DevAvailability.AVAILABLE,
                assignedProjectIds = listOf("proj-1"),
                avatarBgColorHex = 0xFF48CAE4
            )
        )

        fun getInitialProjects(): List<Project> = listOf(
            Project(
                id = "proj-1",
                name = "Star Fleet Mobile Suite",
                description = "Primary field and operations mobile application for Star Corp technicians and operations leads.",
                category = ProjectCategory.MOBILE_APP,
                status = ProjectStatus.IN_PROGRESS,
                clientOrDepartment = "Operations Group",
                progress = 75,
                repoUrl = "https://github.com/starcorp/star-fleet-mobile",
                liveUrl = "https://fleet.starcorp.io",
                techStack = listOf("Kotlin", "Jetpack Compose", "Coroutines", "Room"),
                assignedDevIds = listOf("dev-1", "dev-2", "dev-6"),
                tasks = listOf(
                    ProjectTask("t1", "Implement offline cache sync", true),
                    ProjectTask("t2", "Audit sensor telemetry logging", true),
                    ProjectTask("t3", "Material 3 tablet adaptive layouts", true),
                    ProjectTask("t4", "Push notification alert pipeline", false)
                ),
                targetDate = "Nov 15, 2026"
            ),
            Project(
                id = "proj-2",
                name = "Nexus Enterprise Portal",
                description = "Centralized partner and client billing portal featuring automated invoices and analytics.",
                category = ProjectCategory.WEB_APP,
                status = ProjectStatus.ACTIVE,
                clientOrDepartment = "Commercial Banking",
                progress = 100,
                repoUrl = "https://github.com/starcorp/nexus-portal",
                liveUrl = "https://nexus.starcorp.io",
                techStack = listOf("TypeScript", "Next.js", "TailwindCSS", "PostgreSQL"),
                assignedDevIds = listOf("dev-2", "dev-3", "dev-5"),
                tasks = listOf(
                    ProjectTask("t5", "Enterprise SSO integration", true),
                    ProjectTask("t6", "Stripe billing webhooks", true),
                    ProjectTask("t7", "PDF statement generator", true)
                ),
                targetDate = "Completed"
            ),
            Project(
                id = "proj-3",
                name = "Star Stream Gateway",
                description = "Ultra low latency event streaming and messaging backbone connecting global microservices.",
                category = ProjectCategory.BACKEND_API,
                status = ProjectStatus.IN_PROGRESS,
                clientOrDepartment = "Core Infrastructure",
                progress = 60,
                repoUrl = "https://github.com/starcorp/star-stream-gw",
                liveUrl = "https://api.stream.starcorp.io/health",
                techStack = listOf("Go", "gRPC", "Redis", "Kafka", "Cloud Run"),
                assignedDevIds = listOf("dev-3", "dev-4"),
                tasks = listOf(
                    ProjectTask("t8", "Connection pool benchmark tests", true),
                    ProjectTask("t9", "Distributed rate limiter filter", true),
                    ProjectTask("t10", "Zero-downtime cluster rollout", false)
                ),
                targetDate = "Dec 01, 2026"
            ),
            Project(
                id = "proj-4",
                name = "Aegis Observability Hub",
                description = "Internal monitoring, log aggregator, and real-time security alerting dashboard for DevOps.",
                category = ProjectCategory.INTERNAL_TOOL,
                status = ProjectStatus.IN_REVIEW,
                clientOrDepartment = "SecOps & DevOps",
                progress = 90,
                repoUrl = "https://github.com/starcorp/aegis-observability",
                liveUrl = "https://aegis.internal.starcorp.io",
                techStack = listOf("Python", "ClickHouse", "React", "Grafana"),
                assignedDevIds = listOf("dev-1", "dev-4"),
                tasks = listOf(
                    ProjectTask("t11", "Slack incident paging bot", true),
                    ProjectTask("t12", "Audit log encryption at rest", true),
                    ProjectTask("t13", "Final security penetration signoff", false)
                ),
                targetDate = "Oct 28, 2026"
            )
        )

        fun getInitialWebsites(): List<Website> = listOf(
            Website(
                id = "web-1",
                name = "Star Corp Main Site",
                url = "https://starcorp.io",
                environment = Environment.PRODUCTION,
                hostingProvider = "Google Cloud Run",
                status = ServiceStatus.OPERATIONAL,
                sslActive = true,
                uptimePercent = 99.98,
                pingMs = 24,
                linkedProjectId = "proj-2",
                lastChecked = "2m ago"
            ),
            Website(
                id = "web-2",
                name = "Nexus Client Portal",
                url = "https://nexus.starcorp.io",
                environment = Environment.PRODUCTION,
                hostingProvider = "Vercel Enterprise",
                status = ServiceStatus.OPERATIONAL,
                sslActive = true,
                uptimePercent = 99.95,
                pingMs = 38,
                linkedProjectId = "proj-2",
                lastChecked = "5m ago"
            ),
            Website(
                id = "web-3",
                name = "Fleet Operations Staging",
                url = "https://staging-fleet.starcorp.io",
                environment = Environment.STAGING,
                hostingProvider = "Cloud Run",
                status = ServiceStatus.OPERATIONAL,
                sslActive = true,
                uptimePercent = 99.82,
                pingMs = 45,
                linkedProjectId = "proj-1",
                lastChecked = "12m ago"
            ),
            Website(
                id = "web-4",
                name = "Developer Documentation Hub",
                url = "https://docs.starcorp.io",
                environment = Environment.PRODUCTION,
                hostingProvider = "Cloudflare Pages",
                status = ServiceStatus.OPERATIONAL,
                sslActive = true,
                uptimePercent = 100.0,
                pingMs = 18,
                lastChecked = "1m ago"
            ),
            Website(
                id = "web-5",
                name = "Internal Ops Sandbox",
                url = "https://dev-sandbox.internal.starcorp.io",
                environment = Environment.DEVELOPMENT,
                hostingProvider = "AWS ECS",
                status = ServiceStatus.MAINTENANCE,
                sslActive = true,
                uptimePercent = 98.40,
                pingMs = 112,
                lastChecked = "18m ago"
            )
        )

        fun getInitialDatabases(): List<DatabaseInstance> = listOf(
            DatabaseInstance(
                id = "db-1",
                name = "starcorp-core-pg-prod",
                engine = DatabaseEngine.POSTGRESQL,
                hostEndpoint = "pg-primary.prod.starcorp.internal:5432",
                region = "europe-west2",
                environment = Environment.PRODUCTION,
                status = DatabaseStatus.HEALTHY,
                storageUsedGb = 412.5,
                storageTotalGb = 1000.0,
                activeConnections = 142,
                backupSchedule = "Daily at 02:00 UTC",
                linkedProjectId = "proj-2"
            ),
            DatabaseInstance(
                id = "db-2",
                name = "star-stream-redis-cluster",
                engine = DatabaseEngine.REDIS,
                hostEndpoint = "redis-cache.stream.starcorp.internal:6379",
                region = "europe-west2",
                environment = Environment.PRODUCTION,
                status = DatabaseStatus.HEALTHY,
                storageUsedGb = 28.4,
                storageTotalGb = 64.0,
                activeConnections = 860,
                backupSchedule = "Continuous RDB/AOF",
                linkedProjectId = "proj-3"
            ),
            DatabaseInstance(
                id = "db-3",
                name = "cloudsql-operations-replica",
                engine = DatabaseEngine.CLOUD_SQL,
                hostEndpoint = "cloudsql-read-replica.gcp:3306",
                region = "us-central1",
                environment = Environment.PRODUCTION,
                status = DatabaseStatus.HEALTHY,
                storageUsedGb = 188.0,
                storageTotalGb = 500.0,
                activeConnections = 54,
                backupSchedule = "Daily at 04:00 UTC",
                linkedProjectId = "proj-1"
            ),
            DatabaseInstance(
                id = "db-4",
                name = "firestore-mobile-state",
                engine = DatabaseEngine.FIRESTORE,
                hostEndpoint = "firestore.googleapis.com/v1/star-fleet",
                region = "nam5 (multi-region)",
                environment = Environment.PRODUCTION,
                status = DatabaseStatus.HEALTHY,
                storageUsedGb = 42.1,
                storageTotalGb = 250.0,
                activeConnections = 310,
                backupSchedule = "Managed Cloud Firestore Backups",
                linkedProjectId = "proj-1"
            ),
            DatabaseInstance(
                id = "db-5",
                name = "mongodb-logs-staging",
                engine = DatabaseEngine.MONGODB,
                hostEndpoint = "mongo-atlas.staging.starcorp.io:27017",
                region = "europe-west1",
                environment = Environment.STAGING,
                status = DatabaseStatus.SYNCING,
                storageUsedGb = 85.3,
                storageTotalGb = 120.0,
                activeConnections = 18,
                backupSchedule = "Weekly automated snapshot",
                linkedProjectId = "proj-4"
            )
        )
    }
}
