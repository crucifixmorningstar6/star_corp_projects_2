package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.StarCorpRepository
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppDestination(val title: String) {
    DASHBOARD("Dashboard"),
    PROJECTS("Projects"),
    WEBSITES("Websites"),
    DATABASES("Databases"),
    DEVS("Devs")
}

data class FleetStats(
    val totalProjects: Int = 0,
    val activeProjects: Int = 0,
    val totalWebsites: Int = 0,
    val operationalWebsites: Int = 0,
    val fleetUptime: Double = 99.98,
    val totalDatabases: Int = 0,
    val totalStorageUsedGb: Double = 0.0,
    val totalStorageCapacityGb: Double = 0.0,
    val totalDevs: Int = 0,
    val availableDevs: Int = 0
)

class StarCorpViewModel(
    private val repository: StarCorpRepository = StarCorpRepository()
) : ViewModel() {

    private val _currentDestination = MutableStateFlow(AppDestination.DASHBOARD)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    // Filters
    private val _projectStatusFilter = MutableStateFlow<ProjectStatus?>(null)
    val projectStatusFilter: StateFlow<ProjectStatus?> = _projectStatusFilter.asStateFlow()

    private val _websiteEnvFilter = MutableStateFlow<Environment?>(null)
    val websiteEnvFilter: StateFlow<Environment?> = _websiteEnvFilter.asStateFlow()

    private val _databaseEngineFilter = MutableStateFlow<DatabaseEngine?>(null)
    val databaseEngineFilter: StateFlow<DatabaseEngine?> = _databaseEngineFilter.asStateFlow()

    private val _devRoleFilter = MutableStateFlow<DevRole?>(null)
    val devRoleFilter: StateFlow<DevRole?> = _devRoleFilter.asStateFlow()

    // Dialog & Detail states
    private val _selectedProject = MutableStateFlow<Project?>(null)
    val selectedProject: StateFlow<Project?> = _selectedProject.asStateFlow()

    private val _editingProject = MutableStateFlow<Project?>(null)
    val editingProject: StateFlow<Project?> = _editingProject.asStateFlow()
    val isProjectDialogOpen = MutableStateFlow(false)

    private val _editingWebsite = MutableStateFlow<Website?>(null)
    val editingWebsite: StateFlow<Website?> = _editingWebsite.asStateFlow()
    val isWebsiteDialogOpen = MutableStateFlow(false)

    private val _editingDatabase = MutableStateFlow<DatabaseInstance?>(null)
    val editingDatabase: StateFlow<DatabaseInstance?> = _editingDatabase.asStateFlow()
    val isDatabaseDialogOpen = MutableStateFlow(false)

    private val _editingDeveloper = MutableStateFlow<Developer?>(null)
    val editingDeveloper: StateFlow<Developer?> = _editingDeveloper.asStateFlow()
    val isDeveloperDialogOpen = MutableStateFlow(false)

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    // Data streams
    val allProjects: StateFlow<List<Project>> = repository.projects
    val allWebsites: StateFlow<List<Website>> = repository.websites
    val allDatabases: StateFlow<List<DatabaseInstance>> = repository.databases
    val allDevelopers: StateFlow<List<Developer>> = repository.developers

    // Filtered streams
    val filteredProjects: StateFlow<List<Project>> = combine(
        allProjects, _searchQuery, _projectStatusFilter
    ) { projects, query, statusFilter ->
        projects.filter { project ->
            val matchesQuery = query.isBlank() ||
                    project.name.contains(query, ignoreCase = true) ||
                    project.description.contains(query, ignoreCase = true) ||
                    project.techStack.any { it.contains(query, ignoreCase = true) }
            val matchesStatus = statusFilter == null || project.status == statusFilter
            matchesQuery && matchesStatus
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredWebsites: StateFlow<List<Website>> = combine(
        allWebsites, _searchQuery, _websiteEnvFilter
    ) { websites, query, envFilter ->
        websites.filter { site ->
            val matchesQuery = query.isBlank() ||
                    site.name.contains(query, ignoreCase = true) ||
                    site.url.contains(query, ignoreCase = true) ||
                    site.hostingProvider.contains(query, ignoreCase = true)
            val matchesEnv = envFilter == null || site.environment == envFilter
            matchesQuery && matchesEnv
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredDatabases: StateFlow<List<DatabaseInstance>> = combine(
        allDatabases, _searchQuery, _databaseEngineFilter
    ) { databases, query, engineFilter ->
        databases.filter { db ->
            val matchesQuery = query.isBlank() ||
                    db.name.contains(query, ignoreCase = true) ||
                    db.hostEndpoint.contains(query, ignoreCase = true) ||
                    db.region.contains(query, ignoreCase = true)
            val matchesEngine = engineFilter == null || db.engine == engineFilter
            matchesQuery && matchesEngine
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredDevelopers: StateFlow<List<Developer>> = combine(
        allDevelopers, _searchQuery, _devRoleFilter
    ) { devs, query, roleFilter ->
        devs.filter { dev ->
            val matchesQuery = query.isBlank() ||
                    dev.name.contains(query, ignoreCase = true) ||
                    dev.email.contains(query, ignoreCase = true) ||
                    dev.skills.any { it.contains(query, ignoreCase = true) }
            val matchesRole = roleFilter == null || dev.role == roleFilter
            matchesQuery && matchesRole
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val fleetStats: StateFlow<FleetStats> = combine(
        allProjects, allWebsites, allDatabases, allDevelopers
    ) { projects, websites, databases, devs ->
        FleetStats(
            totalProjects = projects.size,
            activeProjects = projects.count { it.status == ProjectStatus.ACTIVE },
            totalWebsites = websites.size,
            operationalWebsites = websites.count { it.status == ServiceStatus.OPERATIONAL },
            fleetUptime = if (websites.isNotEmpty()) websites.map { it.uptimePercent }.average() else 100.0,
            totalDatabases = databases.size,
            totalStorageUsedGb = databases.sumOf { it.storageUsedGb },
            totalStorageCapacityGb = databases.sumOf { it.storageTotalGb },
            totalDevs = devs.size,
            availableDevs = devs.count { it.availability == DevAvailability.AVAILABLE }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FleetStats())

    fun setDestination(destination: AppDestination) {
        _currentDestination.value = destination
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleSearch() {
        _isSearchActive.value = !_isSearchActive.value
        if (!_isSearchActive.value) {
            _searchQuery.value = ""
        }
    }

    fun setProjectStatusFilter(status: ProjectStatus?) {
        _projectStatusFilter.value = if (_projectStatusFilter.value == status) null else status
    }

    fun setWebsiteEnvFilter(env: Environment?) {
        _websiteEnvFilter.value = if (_websiteEnvFilter.value == env) null else env
    }

    fun setDatabaseEngineFilter(engine: DatabaseEngine?) {
        _databaseEngineFilter.value = if (_databaseEngineFilter.value == engine) null else engine
    }

    fun setDevRoleFilter(role: DevRole?) {
        _devRoleFilter.value = if (_devRoleFilter.value == role) null else role
    }

    // Project Actions
    fun selectProject(project: Project?) {
        _selectedProject.value = project
    }

    fun openProjectDialog(project: Project? = null) {
        _editingProject.value = project
        isProjectDialogOpen.value = true
    }

    fun closeProjectDialog() {
        _editingProject.value = null
        isProjectDialogOpen.value = false
    }

    fun saveProject(project: Project) {
        if (_editingProject.value == null) {
            val newProj = if (project.id.isBlank()) project.copy(id = "proj-${UUID.randomUUID().toString().take(6)}") else project
            repository.addProject(newProj)
            emitMessage("Project '${newProj.name}' created")
        } else {
            repository.updateProject(project)
            emitMessage("Project '${project.name}' updated")
        }
        closeProjectDialog()
    }

    fun deleteProject(project: Project) {
        repository.deleteProject(project.id)
        if (_selectedProject.value?.id == project.id) {
            _selectedProject.value = null
        }
        emitMessage("Project '${project.name}' deleted")
    }

    fun toggleProjectTask(projectId: String, taskId: String) {
        repository.toggleTask(projectId, taskId)
    }

    // Website Actions
    fun openWebsiteDialog(website: Website? = null) {
        _editingWebsite.value = website
        isWebsiteDialogOpen.value = true
    }

    fun closeWebsiteDialog() {
        _editingWebsite.value = null
        isWebsiteDialogOpen.value = false
    }

    fun saveWebsite(website: Website) {
        if (_editingWebsite.value == null) {
            val newSite = if (website.id.isBlank()) website.copy(id = "web-${UUID.randomUUID().toString().take(6)}") else website
            repository.addWebsite(newSite)
            emitMessage("Website '${newSite.name}' added")
        } else {
            repository.updateWebsite(website)
            emitMessage("Website '${website.name}' updated")
        }
        closeWebsiteDialog()
    }

    fun deleteWebsite(website: Website) {
        repository.deleteWebsite(website.id)
        emitMessage("Website '${website.name}' deleted")
    }

    fun pingWebsite(website: Website) {
        repository.pingWebsite(website.id)
        emitMessage("Pinged ${website.name}: ${website.pingMs}ms")
    }

    // Database Actions
    fun openDatabaseDialog(database: DatabaseInstance? = null) {
        _editingDatabase.value = database
        isDatabaseDialogOpen.value = true
    }

    fun closeDatabaseDialog() {
        _editingDatabase.value = null
        isDatabaseDialogOpen.value = false
    }

    fun saveDatabase(database: DatabaseInstance) {
        if (_editingDatabase.value == null) {
            val newDb = if (database.id.isBlank()) database.copy(id = "db-${UUID.randomUUID().toString().take(6)}") else database
            repository.addDatabase(newDb)
            emitMessage("Database instance '${newDb.name}' created")
        } else {
            repository.updateDatabase(database)
            emitMessage("Database '${database.name}' updated")
        }
        closeDatabaseDialog()
    }

    fun deleteDatabase(database: DatabaseInstance) {
        repository.deleteDatabase(database.id)
        emitMessage("Database '${database.name}' removed")
    }

    fun backupDatabase(database: DatabaseInstance) {
        repository.triggerBackup(database.id)
        emitMessage("Backup initiated for ${database.name}")
    }

    // Developer Actions
    fun openDeveloperDialog(dev: Developer? = null) {
        _editingDeveloper.value = dev
        isDeveloperDialogOpen.value = true
    }

    fun closeDeveloperDialog() {
        _editingDeveloper.value = null
        isDeveloperDialogOpen.value = false
    }

    fun saveDeveloper(dev: Developer) {
        if (_editingDeveloper.value == null) {
            val newDev = if (dev.id.isBlank()) dev.copy(id = "dev-${UUID.randomUUID().toString().take(6)}") else dev
            repository.addDeveloper(newDev)
            emitMessage("Developer '${newDev.name}' added to roster")
        } else {
            repository.updateDeveloper(dev)
            emitMessage("Profile for '${dev.name}' updated")
        }
        closeDeveloperDialog()
    }

    fun deleteDeveloper(dev: Developer) {
        repository.deleteDeveloper(dev.id)
        emitMessage("Developer '${dev.name}' removed")
    }

    private fun emitMessage(msg: String) {
        viewModelScope.launch {
            _userMessage.emit(msg)
        }
    }
}
