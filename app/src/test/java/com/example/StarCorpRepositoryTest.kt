package com.example

import com.example.data.StarCorpRepository
import com.example.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StarCorpRepositoryTest {

    @Test
    fun testInitialSeedData() {
        val repo = StarCorpRepository()
        assertTrue(repo.projects.value.isNotEmpty())
        assertTrue(repo.websites.value.isNotEmpty())
        assertTrue(repo.databases.value.isNotEmpty())
        assertTrue(repo.developers.value.isNotEmpty())
    }

    @Test
    fun testAddAndToggleProjectTask() {
        val repo = StarCorpRepository()
        val initialSize = repo.projects.value.size

        val newProject = Project(
            id = "test-proj",
            name = "Test Project",
            description = "Unit testing project",
            category = ProjectCategory.INTERNAL_TOOL,
            status = ProjectStatus.IN_PROGRESS,
            clientOrDepartment = "QA Team",
            progress = 0,
            repoUrl = "https://github.com/starcorp/test",
            techStack = listOf("Kotlin"),
            assignedDevIds = listOf("dev-1"),
            tasks = listOf(ProjectTask("t1", "Task 1", false)),
            targetDate = "Dec 2026"
        )
        repo.addProject(newProject)
        assertEquals(initialSize + 1, repo.projects.value.size)

        repo.toggleTask("test-proj", "t1")
        val updated = repo.projects.value.first { it.id == "test-proj" }
        assertTrue(updated.tasks.first().isCompleted)
        assertEquals(100, updated.progress)
    }

    @Test
    fun testPingWebsite() {
        val repo = StarCorpRepository()
        val firstSite = repo.websites.value.first()
        repo.pingWebsite(firstSite.id)
        val pinged = repo.websites.value.first { it.id == firstSite.id }
        assertTrue(pinged.pingMs > 0)
    }
}
