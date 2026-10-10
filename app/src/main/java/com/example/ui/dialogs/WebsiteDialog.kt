package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.Environment
import com.example.model.ServiceStatus
import com.example.model.Website

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebsiteDialog(
    website: Website?,
    onDismiss: () -> Unit,
    onSave: (Website) -> Unit
) {
    var name by remember { mutableStateOf(website?.name ?: "") }
    var url by remember { mutableStateOf(website?.url ?: "https://") }
    var hostingProvider by remember { mutableStateOf(website?.hostingProvider ?: "Google Cloud Run") }
    var selectedEnv by remember { mutableStateOf(website?.environment ?: Environment.PRODUCTION) }
    var selectedStatus by remember { mutableStateOf(website?.status ?: ServiceStatus.OPERATIONAL) }
    var sslActive by remember { mutableStateOf(website?.sslActive ?: true) }

    var envDropdownExpanded by remember { mutableStateOf(false) }
    var statusDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (website == null) "Add Fleet Website" else "Edit Website")
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
                    label = { Text("Site Name / Service") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("website_name_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("Domain / URL") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("website_url_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = hostingProvider,
                    onValueChange = { hostingProvider = it },
                    label = { Text("Hosting Infrastructure") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Environment dropdown
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

                // Status dropdown
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
                        ServiceStatus.values().forEach { st ->
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("SSL Certificate Active")
                    Switch(
                        checked = sslActive,
                        onCheckedChange = { sslActive = it },
                        modifier = Modifier.testTag("ssl_active_switch")
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && url.isNotBlank()) {
                        val toSave = (website ?: Website(
                            id = "",
                            name = "",
                            url = "",
                            environment = selectedEnv,
                            hostingProvider = hostingProvider,
                            status = selectedStatus
                        )).copy(
                            name = name.trim(),
                            url = url.trim(),
                            hostingProvider = hostingProvider.trim(),
                            environment = selectedEnv,
                            status = selectedStatus,
                            sslActive = sslActive
                        )
                        onSave(toSave)
                    }
                },
                enabled = name.isNotBlank() && url.isNotBlank(),
                modifier = Modifier.testTag("save_website_button")
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_website_button")
            ) {
                Text("Cancel")
            }
        }
    )
}
