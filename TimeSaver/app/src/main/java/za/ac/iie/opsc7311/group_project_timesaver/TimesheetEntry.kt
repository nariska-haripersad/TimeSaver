package za.ac.iie.opsc7311.group_project_timesaver

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import java.util.Date


// To store a timesheet entry
data class TimesheetEntry(
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val date: Date = Date(),
    val startTime: Date = Date(),
    val endTime: Date = Date(),
    val imageUri: String? = null,
    val totalHours: Double = 0.0
)




object TimesheetManager {

    // a list of the time entries
    private val timesheetEntries: MutableMap<String, MutableList<TimesheetEntry>> = mutableMapOf()
    private val databaseReference = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/").getReference("timesheets")

    // Function to add a timesheet entry to the database
    fun addTimesheetEntry(username: String, entry: TimesheetEntry, callback: (Boolean) -> Unit) {
        val userTimesheetsRef = databaseReference.child(username)
        val entryId = userTimesheetsRef.push().key ?: return

        val entryRef = userTimesheetsRef.child(entryId)
        entryRef.setValue(entry)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
            .addOnFailureListener { exception ->
                callback(false)
                Log.e("TimesheetManager", "Error adding timesheet entry", exception)
            }
    }

    // to get the timesheet entries associated with the user
        fun getTimesheetEntries(username: String): List<TimesheetEntry>? {
            return timesheetEntries[username]
        }
    }

