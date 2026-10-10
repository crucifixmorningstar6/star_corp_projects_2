package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.StarGreen
import com.example.ui.theme.StarAmber
import com.example.ui.theme.StarRed
import com.example.ui.theme.StarTeal

@Composable
fun StatusBadge(
    statusText: String,
    statusColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(statusColor.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(statusColor)
        )
        Text(
            text = statusText,
            color = statusColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ProjectStatusBadge(status: ProjectStatus, modifier: Modifier = Modifier) {
    val color = when (status) {
        ProjectStatus.ACTIVE -> StarGreen
        ProjectStatus.IN_PROGRESS -> StarTeal
        ProjectStatus.IN_REVIEW -> StarAmber
        ProjectStatus.PLANNING -> Color(0xFF9E00FF)
        ProjectStatus.ARCHIVED -> Color(0xFF64748B)
    }
    StatusBadge(statusText = status.label, statusColor = color, modifier = modifier)
}

@Composable
fun ServiceStatusBadge(status: ServiceStatus, modifier: Modifier = Modifier) {
    val color = when (status) {
        ServiceStatus.OPERATIONAL -> StarGreen
        ServiceStatus.DEGRADED -> StarAmber
        ServiceStatus.MAINTENANCE -> Color(0xFF00B4D8)
        ServiceStatus.DOWN -> StarRed
    }
    StatusBadge(statusText = status.label, statusColor = color, modifier = modifier)
}

@Composable
fun DatabaseStatusBadge(status: DatabaseStatus, modifier: Modifier = Modifier) {
    val color = when (status) {
        DatabaseStatus.HEALTHY -> StarGreen
        DatabaseStatus.SYNCING -> StarTeal
        DatabaseStatus.HIGH_LOAD -> StarAmber
        DatabaseStatus.MAINTENANCE -> Color(0xFF64748B)
    }
    StatusBadge(statusText = status.label, statusColor = color, modifier = modifier)
}

@Composable
fun DevAvailabilityBadge(availability: DevAvailability, modifier: Modifier = Modifier) {
    val color = when (availability) {
        DevAvailability.AVAILABLE -> StarGreen
        DevAvailability.BUSY -> StarAmber
        DevAvailability.ON_LEAVE -> Color(0xFF94A3B8)
    }
    StatusBadge(statusText = availability.label, statusColor = color, modifier = modifier)
}
