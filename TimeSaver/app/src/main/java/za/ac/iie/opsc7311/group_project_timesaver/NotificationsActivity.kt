package za.ac.iie.opsc7311.group_project_timesaver

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

data class Notification(val message: String, val time: String)

class NotificationsActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val notificationsList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        listView = findViewById(R.id.notification_list_view)
        adapter = ArrayAdapter(this, R.layout.notification_item, R.id.notification_text, notificationsList)
        listView.adapter = adapter

        fetchNotifications()

    }

    private fun fetchNotifications() {
        val username = LoggedInUser.getUsername()
        if (username.isNullOrEmpty()) {
            return
        }

        val database = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/")
        val timesheetsRef = database.getReference("timesheets").child(username)

        timesheetsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                notificationsList.clear()

                for (timesheetSnapshot in dataSnapshot.children) {
                    val name = timesheetSnapshot.child("name").getValue(String::class.java) ?: "Unnamed Task"
                    val dateSnapshot = timesheetSnapshot.child("date")
                    val startTimeSnapshot = timesheetSnapshot.child("startTime")

                    if (dateSnapshot.exists() && startTimeSnapshot.exists()) {
                        val year = dateSnapshot.child("year").getValue(Int::class.java)
                        val month = dateSnapshot.child("month").getValue(Int::class.java)
                        val day = dateSnapshot.child("day").getValue(Int::class.java)
                        val hour = startTimeSnapshot.child("hours").getValue(Int::class.java)
                        val minute = startTimeSnapshot.child("minutes").getValue(Int::class.java)

                        if (year != null && month != null && day != null && hour != null && minute != null) {
                            // Adjust year and month based on Firebase data format
                            val taskDate = Calendar.getInstance().apply {
                                set(year + 1900, month, day, hour, minute, 0)
                            }

                            val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                            val taskDateString = dateFormat.format(taskDate.time)
                            val taskTimeString = timeFormat.format(taskDate.time)

                            val notification = "Start working on $name\n$taskDateString - $taskTimeString"
                            notificationsList.add(notification)
                        }
                    }
                }

                // Sort the notifications list in descending order
                notificationsList.sortByDescending { notification ->
                    val dateTime = notification.split("\n")[1].split(" - ")
                    val date = dateTime[0]
                    val time = dateTime[1]
                    SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).parse("$date $time")
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("NotificationsActivity", "Database error: ${databaseError.message}")
            }
        })
    }
}
