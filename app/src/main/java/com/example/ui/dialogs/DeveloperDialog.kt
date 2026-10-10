package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.DevAvailability
import com.example.model.DevRole
import com.example.model.Developer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperDialog(
    dev: Developer?,
    onDismiss: () -> Unit,
    onSave: (Developer) -> Unit
) {
    var name by remember { mutableStateOf(dev?.name ?: "") }
    var email by remember { mutableStateOf(dev?.email ?: "") }
    var githubHandle by remember { mutableStateOf(dev?.githubHandle ?: "") }
    var selectedRole by remember { mutableStateOf(dev?.role ?: DevRole.FULLSTACK_DEV) }
    var selectedAvailability by remember { mutableStateOf(dev?.availability ?: DevAvailability.AVAILABLE) }
    var skillsStr by remember { mutableStateOf(dev?.skills?.joinToString(", ") ?: "Kotlin, Compose") }

    var roleDropdownExpanded by remember { mutableStateOf(false) }
    var availDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (dev == null) "Add Team Member" else "Edit Developer Profile")
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
                    label = { Text("Full Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dev_name_input"),
                    singleLine = true
                )

                // Role Dropdown
                ExposedDropdownMenuBox(
                    expanded = roleDropdownExpanded,
                    onExpandedChange = { roleDropdownExpanded = !roleDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedRole.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Engineering Role") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = roleDropdownExpanded,
                        onDismissRequest = { roleDropdownExpanded = false }
                    ) {
                        DevRole.values().forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role.label) },
                                onClick = {
                                    selectedRole = role
                                    roleDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dev_email_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = githubHandle,
                    onValueChange = { githubHandle = it },
                    label = { Text("GitHub Username") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Availability Dropdown
                ExposedDropdownMenuBox(
                    expanded = availDropdownExpanded,
                    onExpandedChange = { availDropdownExpanded = !availDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedAvailability.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Availability Status") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = availDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = availDropdownExpanded,
                        onDismissRequest = { availDropdownExpanded = false }
                    ) {
                        DevAvailability.values().forEach { av ->
                            DropdownMenuItem(
                                text = { Text(av.label) },
                                onClick = {
                                    selectedAvailability = av
                                    availDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = skillsStr,
                    onValueChange = { skillsStr = it },
                    label = { Text("Skills & Technologies (comma separated)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val skillsList = skillsStr.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        val toSave = (dev ?: Developer(
                            id = "",
                            name = "",
                            role = selectedRole,
                            email = email,
                            githubHandle = githubHandle,
                            skills = skillsList,
                            availability = selectedAvailability
                        )).copy(
                            name = name.trim(),
                            role = selectedRole,
                            email = email.trim(),
                            githubHandle = githubHandle.trim(),
                            skills = skillsList,
                            availability = selectedAvailability
                        )
                        onSave(toSave)
                    }
                },
                enabled = name.isNotBlank(),
                modifier = Modifier.testTag("save_dev_button")
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_dev_button")
            ) {
                Text("Cancel")
            }
        }
    )
}
