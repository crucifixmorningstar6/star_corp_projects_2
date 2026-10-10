package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.Project
import com.example.model.ProjectCategory
import com.example.model.ProjectStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDialog(
    project: Project?,
    onDismiss: () -> Unit,
    onSave: (Project) -> Unit
) {
    var name by remember { mutableStateOf(project?.name ?: "") }
    var description by remember { mutableStateOf(project?.description ?: "") }
    var clientOrDepartment by remember { mutableStateOf(project?.clientOrDepartment ?: "") }
    var repoUrl by remember { mutableStateOf(project?.repoUrl ?: "") }
    var liveUrl by remember { mutableStateOf(project?.liveUrl ?: "") }
    var techStackStr by remember { mutableStateOf(project?.techStack?.joinToString(", ") ?: "Kotlin, Compose") }
    var targetDate by remember { mutableStateOf(project?.targetDate ?: "Q4 2026") }
    var selectedCategory by remember { mutableStateOf(project?.category ?: ProjectCategory.WEB_APP) }
    var selectedStatus by remember { mutableStateOf(project?.status ?: ProjectStatus.IN_PROGRESS) }
    var progressSlider by remember { mutableStateOf((project?.progress ?: 25).toFloat()) }

    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var statusDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (project == null) "New Star Corp Project" else "Edit Project")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Project Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("project_name_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("project_desc_input"),
                    minLines = 2,
                    maxLines = 4
                )

                OutlinedTextField(
                    value = clientOrDepartment,
                    onValueChange = { clientOrDepartment = it },
                    label = { Text("Client / Department") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Category Selector
                ExposedDropdownMenuBox(
                    expanded = categoryDropdownExpanded,
                    onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }
                    ) {
                        ProjectCategory.values().forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.label) },
                                onClick = {
                                    selectedCategory = cat
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Status Selector
                ExposedDropdownMenuBox(
                    expanded = statusDropdownExpanded,
                    onExpandedChange = { statusDropdownExpanded = !statusDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedStatus.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = statusDropdownExpanded,
                        onDismissRequest = { statusDropdownExpanded = false }
                    ) {
                        ProjectStatus.values().forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st.label) },
                                onClick = {
                                    selectedStatus = st
                                    statusDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Text(
                    text = "Completion Progress: ${progressSlider.toInt()}%",
                    style = MaterialTheme.typography.bodyMedium
                )
                Slider(
                    value = progressSlider,
                    onValueChange = { progressSlider = it },
                    valueRange = 0f..100f,
                    modifier = Modifier.testTag("project_progress_slider")
                )

                OutlinedTextField(
                    value = repoUrl,
                    onValueChange = { repoUrl = it },
                    label = { Text("Repository URL") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = liveUrl,
                    onValueChange = { liveUrl = it },
                    label = { Text("Live Deployment URL (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = techStackStr,
                    onValueChange = { techStackStr = it },
                    label = { Text("Tech Stack (comma separated)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = targetDate,
                    onValueChange = { targetDate = it },
                    label = { Text("Target Release / Milestone") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val stack = techStackStr.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        val toSave = (project ?: Project(
                            id = "",
                            name = "",
                            description = "",
                            category = selectedCategory,
                            status = selectedStatus,
                            clientOrDepartment = "",
                            progress = 0,
                            repoUrl = "",
                            techStack = emptyList(),
                            assignedDevIds = emptyList(),
                            targetDate = ""
                        )).copy(
                            name = name.trim(),
                            description = description.trim(),
                            category = selectedCategory,
                            status = selectedStatus,
                            clientOrDepartment = clientOrDepartment.trim(),
                            progress = progressSlider.toInt(),
                            repoUrl = repoUrl.trim(),
                            liveUrl = liveUrl.trim(),
                            techStack = stack,
                            targetDate = targetDate.trim()
                        )
                        onSave(toSave)
                    }
                },
                enabled = name.isNotBlank(),
                modifier = Modifier.testTag("save_project_button")
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_project_button")
            ) {
                Text("Cancel")
            }
        }
    )
}
