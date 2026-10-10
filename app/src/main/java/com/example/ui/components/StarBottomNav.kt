package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.viewmodel.AppDestination

data class NavItem(
    val destination: AppDestination,
    val icon: ImageVector,
    val testTag: String
)

val navItems = listOf(
    NavItem(AppDestination.DASHBOARD, Icons.Default.Dashboard, "nav_dashboard"),
    NavItem(AppDestination.PROJECTS, Icons.Default.Layers, "nav_projects"),
    NavItem(AppDestination.WEBSITES, Icons.Default.Language, "nav_websites"),
    NavItem(AppDestination.DATABASES, Icons.Default.Dns, "nav_databases"),
    NavItem(AppDestination.DEVS, Icons.Default.Groups, "nav_devs")
)

@Composable
fun StarBottomNav(
    currentDestination: AppDestination,
    onDestinationSelected: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("star_bottom_navigation"),
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        navItems.forEach { item ->
            val selected = currentDestination == item.destination
            NavigationBarItem(
                selected = selected,
                onClick = { onDestinationSelected(item.destination) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.destination.title
                    )
                },
                label = { Text(item.destination.title) },
                modifier = Modifier.testTag(item.testTag),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun StarNavRail(
    currentDestination: AppDestination,
    onDestinationSelected: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier.testTag("star_navigation_rail"),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        navItems.forEach { item ->
            val selected = currentDestination == item.destination
            NavigationRailItem(
                selected = selected,
                onClick = { onDestinationSelected(item.destination) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.destination.title
                    )
                },
                label = { Text(item.destination.title) },
                modifier = Modifier.testTag("${item.testTag}_rail")
            )
        }
    }
}
