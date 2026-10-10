package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.StarBottomNav
import com.example.ui.components.StarNavRail
import com.example.ui.components.StarTopBar
import com.example.ui.dialogs.*
import com.example.ui.screens.*
import com.example.ui.theme.StarCorpTheme
import com.example.viewmodel.AppDestination
import com.example.viewmodel.StarCorpViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarCorpTheme {
                StarCorpApp()
            }
        }
    }
}

@Composable
fun StarCorpApp(
    viewModel: StarCorpViewModel = viewModel()
) {
    val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearchActive by viewModel.isSearchActive.collectAsStateWithLifecycle()

    val projects by viewModel.filteredProjects.collectAsStateWithLifecycle()
    val websites by viewModel.filteredWebsites.collectAsStateWithLifecycle()
    val databases by viewModel.filteredDatabases.collectAsStateWithLifecycle()
    val developers by viewModel.filteredDevelopers.collectAsStateWithLifecycle()
    val stats by viewModel.fleetStats.collectAsStateWithLifecycle()

    val projectStatusFilter by viewModel.projectStatusFilter.collectAsStateWithLifecycle()
    val websiteEnvFilter by viewModel.websiteEnvFilter.collectAsStateWithLifecycle()
    val databaseEngineFilter by viewModel.databaseEngineFilter.collectAsStateWithLifecycle()
    val devRoleFilter by viewModel.devRoleFilter.collectAsStateWithLifecycle()

    val selectedProject by viewModel.selectedProject.collectAsStateWithLifecycle()
    val editingProject by viewModel.editingProject.collectAsStateWithLifecycle()
    val isProjectDialogOpen by viewModel.isProjectDialogOpen.collectAsStateWithLifecycle()

    val editingWebsite by viewModel.editingWebsite.collectAsStateWithLifecycle()
    val isWebsiteDialogOpen by viewModel.isWebsiteDialogOpen.collectAsStateWithLifecycle()

    val editingDatabase by viewModel.editingDatabase.collectAsStateWithLifecycle()
    val isDatabaseDialogOpen by viewModel.isDatabaseDialogOpen.collectAsStateWithLifecycle()

    val editingDeveloper by viewModel.editingDeveloper.collectAsStateWithLifecycle()
    val isDeveloperDialogOpen by viewModel.isDeveloperDialogOpen.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Back handling
    BackHandler(enabled = isSearchActive || selectedProject != null || currentDestination != AppDestination.DASHBOARD) {
        when {
            selectedProject != null -> viewModel.selectProject(null)
            isSearchActive -> viewModel.toggleSearch()
            currentDestination != AppDestination.DASHBOARD -> viewModel.setDestination(AppDestination.DASHBOARD)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 720.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                StarTopBar(
                    currentDestination = currentDestination,
                    searchQuery = searchQuery,
                    isSearchActive = isSearchActive,
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    onToggleSearch = { viewModel.toggleSearch() }
                )
            },
            bottomBar = {
                if (!isWideScreen) {
                    StarBottomNav(
                        currentDestination = currentDestination,
                        onDestinationSelected = { viewModel.setDestination(it) }
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.testTag("app_snackbar_host")
                )
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isWideScreen) {
                    StarNavRail(
                        currentDestination = currentDestination,
                        onDestinationSelected = { viewModel.setDestination(it) }
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    when (currentDestination) {
                        AppDestination.DASHBOARD -> DashboardScreen(
                            stats = stats,
                            recentProjects = projects,
                            websites = websites,
                            onNavigate = { viewModel.setDestination(it) },
                            onSelectProject = { viewModel.selectProject(it) },
                            onOpenAddProject = { viewModel.openProjectDialog() },
                            onOpenAddWebsite = { viewModel.openWebsiteDialog() },
                            onOpenAddDatabase = { viewModel.openDatabaseDialog() },
                            onOpenAddDev = { viewModel.openDeveloperDialog() }
                        )

                        AppDestination.PROJECTS -> ProjectsScreen(
                            projects = projects,
                            developers = developers,
                            selectedStatusFilter = projectStatusFilter,
                            onStatusFilterSelected = { viewModel.setProjectStatusFilter(it) },
                            onProjectClick = { viewModel.selectProject(it) },
                            onEditProject = { viewModel.openProjectDialog(it) },
                            onDeleteProject = { viewModel.deleteProject(it) },
                            onAddProject = { viewModel.openProjectDialog() }
                        )

                        AppDestination.WEBSITES -> WebsitesScreen(
                            websites = websites,
                            selectedEnvFilter = websiteEnvFilter,
                            onEnvFilterSelected = { viewModel.setWebsiteEnvFilter(it) },
                            onPingWebsite = { viewModel.pingWebsite(it) },
                            onEditWebsite = { viewModel.openWebsiteDialog(it) },
                            onDeleteWebsite = { viewModel.deleteWebsite(it) },
                            onAddWebsite = { viewModel.openWebsiteDialog() }
                        )

                        AppDestination.DATABASES -> DatabasesScreen(
                            databases = databases,
                            selectedEngineFilter = databaseEngineFilter,
                            onEngineFilterSelected = { viewModel.setDatabaseEngineFilter(it) },
                            onBackupDatabase = { viewModel.backupDatabase(it) },
                            onEditDatabase = { viewModel.openDatabaseDialog(it) },
                            onDeleteDatabase = { viewModel.deleteDatabase(it) },
                            onAddDatabase = { viewModel.openDatabaseDialog() }
                        )

                        AppDestination.DEVS -> DevsScreen(
                            developers = developers,
                            selectedRoleFilter = devRoleFilter,
                            onRoleFilterSelected = { viewModel.setDevRoleFilter(it) },
                            onEditDeveloper = { viewModel.openDeveloperDialog(it) },
                            onDeleteDeveloper = { viewModel.deleteDeveloper(it) },
                            onAddDeveloper = { viewModel.openDeveloperDialog() }
                        )
                    }
                }
            }
        }
    }

    // Dialogs
    selectedProject?.let { project ->
        ProjectDetailDialog(
            project = project,
            developers = developers,
            onDismiss = { viewModel.selectProject(null) },
            onEdit = {
                viewModel.selectProject(null)
                viewModel.openProjectDialog(project)
            },
            onDelete = {
                viewModel.deleteProject(project)
            },
            onToggleTask = { taskId ->
                viewModel.toggleProjectTask(project.id, taskId)
            }
        )
    }

    if (isProjectDialogOpen) {
        ProjectDialog(
            project = editingProject,
            onDismiss = { viewModel.closeProjectDialog() },
            onSave = { viewModel.saveProject(it) }
        )
    }

    if (isWebsiteDialogOpen) {
        WebsiteDialog(
            website = editingWebsite,
            onDismiss = { viewModel.closeWebsiteDialog() },
            onSave = { viewModel.saveWebsite(it) }
        )
    }

    if (isDatabaseDialogOpen) {
        DatabaseDialog(
            database = editingDatabase,
            onDismiss = { viewModel.closeDatabaseDialog() },
            onSave = { viewModel.saveDatabase(it) }
        )
    }

    if (isDeveloperDialogOpen) {
        DeveloperDialog(
            dev = editingDeveloper,
            onDismiss = { viewModel.closeDeveloperDialog() },
            onSave = { viewModel.saveDeveloper(it) }
        )
    }
}
