package za.ac.iie.opsc7311.group_project_timesaver

import java.util.Date

data class DailyGoal(
    val date: Date,
    val projectName: String,
    val description: String,
    val minimumHours: Int,
    val maximumHours: Int,
    val priorityLevel: String
)

