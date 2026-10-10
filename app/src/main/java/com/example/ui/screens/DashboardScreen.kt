package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Project
import com.example.model.Website
import com.example.ui.components.MetricCard
import com.example.ui.components.ProjectStatusBadge
import com.example.ui.components.ServiceStatusBadge
import com.example.ui.theme.StarAmber
import com.example.ui.theme.StarCyan
import com.example.ui.theme.StarGreen
import com.example.ui.theme.StarTeal
import com.example.viewmodel.AppDestination
import com.example.viewmodel.FleetStats

@Composable
fun DashboardScreen(
    stats: FleetStats,
    recentProjects: List<Project>,
    websites: List<Website>,
    onNavigate: (AppDestination) -> Unit,
    onSelectProject: (Project) -> Unit,
    onOpenAddProject: () -> Unit,
    onOpenAddWebsite: () -> Unit,
    onOpenAddDatabase: () -> Unit,
    onOpenAddDev: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_hero_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.star_corp_hero),
                            contentDescription = "Star Corp Engineering Command Center",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.35f))
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "GLOBAL COMMAND CENTER",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = StarCyan
                            )
                            Text(
                                text = "Star Corp Projects v2.0",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }

                    // Fleet status bar under hero image
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(StarGreen)
                            )
                            Text(
                                text = "All Infrastructure Operational",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "${String.format("%.2f", stats.fleetUptime)}% uptime",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StarGreen
                        )
                    }
                }
            }
        }

        // Quick Actions Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Quick Provisioning",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = onOpenAddProject,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_add_project_button"),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Project", fontSize = 12.sp)
                    }

                    FilledTonalButton(
                        onClick = onOpenAddWebsite,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_add_website_button"),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Website", fontSize = 12.sp)
                    }

                    FilledTonalButton(
                        onClick = onOpenAddDatabase,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_add_database_button"),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Database", fontSize = 12.sp)
                    }

                    FilledTonalButton(
                        onClick = onOpenAddDev,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_add_dev_button"),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Dev", fontSize = 12.sp)
                    }
                }
            }
        }

        // Metrics Grid (2x2)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Key Performance Indicators",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "PROJECTS",
                        value = "${stats.totalProjects}",
                        subtext = "${stats.activeProjects} Active / Deployed",
                        icon = Icons.Default.Layers,
                        accentColor = StarTeal,
                        testTag = "kpi_projects_card",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppDestination.PROJECTS) }
                    )
                    MetricCard(
                        title = "WEBSITES",
                        value = "${stats.totalWebsites}",
                        subtext = "${stats.operationalWebsites} Live endpoints",
                        icon = Icons.Default.Language,
                        accentColor = StarGreen,
                        testTag = "kpi_websites_card",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppDestination.WEBSITES) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "DATABASES",
                        value = "${stats.totalDatabases}",
                        subtext = "${String.format("%.1f", stats.totalStorageUsedGb)} GB used",
                        icon = Icons.Default.Dns,
                        accentColor = StarAmber,
                        testTag = "kpi_databases_card",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppDestination.DATABASES) }
                    )
                    MetricCard(
                        title = "DEV ROSTER",
                        value = "${stats.totalDevs}",
                        subtext = "${stats.availableDevs} available now",
                        icon = Icons.Default.Groups,
                        accentColor = StarCyan,
                        testTag = "kpi_devs_card",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppDestination.DEVS) }
                    )
                }
            }
        }

        // Active Projects Spotlight
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Projects",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(
                        onClick = { onNavigate(AppDestination.PROJECTS) },
                        modifier = Modifier.testTag("view_all_projects_button")
                    ) {
                        Text("View all (${stats.totalProjects})", fontSize = 12.sp)
                    }
                }

                recentProjects.take(3).forEach { project ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dashboard_project_${project.id}"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                        ),
                        onClick = { onSelectProject(project) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = project.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                ProjectStatusBadge(status = project.status)
                            }

                            Text(
                                text = project.description,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                LinearProgressIndicator(
                                    progress = { project.progress / 100f },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                                Text(
                                    text = "${project.progress}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live Fleet Web Services Snapshot
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Fleet Websites Health",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(
                        onClick = { onNavigate(AppDestination.WEBSITES) },
                        modifier = Modifier.testTag("view_all_websites_button")
                    ) {
                        Text("Manage Fleet", fontSize = 12.sp)
                    }
                }

                websites.take(3).forEach { site ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = site.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${site.environment.label} • ${site.hostingProvider}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "${site.pingMs}ms",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (site.pingMs < 50) StarGreen else StarAmber
                                )
                                ServiceStatusBadge(status = site.status)
                            }
                        }
                    }
                }
            }
        }
    }
}
