package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.DatabaseEngine
import com.example.model.DatabaseInstance
import com.example.model.DatabaseStatus
import com.example.model.Environment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseDialog(
    database: DatabaseInstance?,
    onDismiss: () -> Unit,
    onSave: (DatabaseInstance) -> Unit
) {
    var name by remember { mutableStateOf(database?.name ?: "") }
    var hostEndpoint by remember { mutableStateOf(database?.hostEndpoint ?: "") }
    var region by remember { mutableStateOf(database?.region ?: "europe-west2") }
    var selectedEngine by remember { mutableStateOf(database?.engine ?: DatabaseEngine.POSTGRESQL) }
    var selectedEnv by remember { mutableStateOf(database?.environment ?: Environment.PRODUCTION) }
    var selectedStatus by remember { mutableStateOf(database?.status ?: DatabaseStatus.HEALTHY) }
    var storageUsedStr by remember { mutableStateOf(database?.storageUsedGb?.toString() ?: "50.0") }
    var storageTotalStr by remember { mutableStateOf(database?.storageTotalGb?.toString() ?: "200.0") }
    var activeConnectionsStr by remember { mutableStateOf(database?.activeConnections?.toString() ?: "45") }
    var backupSchedule by remember { mutableStateOf(database?.backupSchedule ?: "Daily at 02:00 UTC") }

    var engineDropdownExpanded by remember { mutableStateOf(false) }
    var envDropdownExpanded by remember { mutableStateOf(false) }
    var statusDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (database == null) "Provision Database" else "Edit Database Cluster")
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
                    label = { Text("Cluster Identifier") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("database_name_input"),
                    singleLine = true
                )

                // Engine Dropdown
                ExposedDropdownMenuBox(
                    expanded = engineDropdownExpanded,
                    onExpandedChange = { engineDropdownExpanded = !engineDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedEngine.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Engine") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = engineDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = engineDropdownExpanded,
                        onDismissRequest = { engineDropdownExpanded = false }
                    ) {
                        DatabaseEngine.values().forEach { eng ->
                            DropdownMenuItem(
                                text = { Text(eng.label) },
                                onClick = {
                                    selectedEngine = eng
                                    engineDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = hostEndpoint,
                    onValueChange = { hostEndpoint = it },
                    label = { Text("Host / Endpoint") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = region,
                    onValueChange = { region = it },
                    label = { Text("Cloud Region") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Environment
                ExposedDropdownMenuBox(
                    expanded = envDropdownExpanded,
                    onExpandedChange = { envDropdownExpanded = !envDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedEnv.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Environment") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = envDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = envDropdownExpanded,
                        onDismissRequest = { envDropdownExpanded = false }
                    ) {
                        Environment.values().forEach { env ->
                            DropdownMenuItem(
                                text = { Text(env.label) },
                                onClick = {
                                    selectedEnv = env
                                    envDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = storageUsedStr,
                        onValueChange = { storageUsedStr = it },
                        label = { Text("Used (GB)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = storageTotalStr,
                        onValueChange = { storageTotalStr = it },
                        label = { Text("Total (GB)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = activeConnectionsStr,
                    onValueChange = { activeConnectionsStr = it },
                    label = { Text("Active Connections") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = backupSchedule,
                    onValueChange = { backupSchedule = it },
                    label = { Text("Backup Schedule") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && hostEndpoint.isNotBlank()) {
                        val used = storageUsedStr.toDoubleOrNull() ?: 20.0
                        val total = storageTotalStr.toDoubleOrNull() ?: 100.0
                        val conns = activeConnectionsStr.toIntOrNull() ?: 10
                        val toSave = (database ?: DatabaseInstance(
                            id = "",
                            name = "",
                            engine = selectedEngine,
                            hostEndpoint = "",
                            region = region,
                            environment = selectedEnv,
                            status = selectedStatus,
                            storageUsedGb = used,
                            storageTotalGb = total,
                            activeConnections = conns,
                            backupSchedule = backupSchedule
                        )).copy(
                            name = name.trim(),
                            engine = selectedEngine,
                            hostEndpoint = hostEndpoint.trim(),
                            region = region.trim(),
                            environment = selectedEnv,
                            status = selectedStatus,
                            storageUsedGb = used,
                            storageTotalGb = total,
                            activeConnections = conns,
                            backupSchedule = backupSchedule.trim()
                        )
                        onSave(toSave)
                    }
                },
                enabled = name.isNotBlank() && hostEndpoint.isNotBlank(),
                modifier = Modifier.testTag("save_database_button")
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_database_button")
            ) {
                Text("Cancel")
            }
        }
    )
}
