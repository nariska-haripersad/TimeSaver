package za.ac.iie.opsc7311.group_project_timesaver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.util.*

// A class for the achievements page
class Achievements : AppCompatActivity() {

    private lateinit var achievement1CheckBox: CheckBox
    private lateinit var achievement2CheckBox: CheckBox
    private lateinit var achievement3CheckBox: CheckBox
    private lateinit var achievement4CheckBox: CheckBox
    private lateinit var achievement5CheckBox: CheckBox
    private lateinit var achievement6CheckBox: CheckBox
    private lateinit var achievement7CheckBox: CheckBox
    lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        achievement1CheckBox = findViewById(R.id.achievement1CheckBox)
        achievement2CheckBox = findViewById(R.id.achievement2CheckBox)
        achievement3CheckBox = findViewById(R.id.achievement3CheckBox)
        achievement4CheckBox = findViewById(R.id.achievement4CheckBox)
        achievement5CheckBox = findViewById(R.id.achievement5CheckBox)
        achievement6CheckBox = findViewById(R.id.achievement6CheckBox)
        achievement7CheckBox = findViewById(R.id.achievement7CheckBox)
        back = findViewById(R.id.back_button)

        // Update the checkboxes based on achievements
        updateAchievements()

        back.setOnClickListener {
            val intent = Intent(this, view_menu::class.java)
            startActivity(intent)
        }
    }

    private fun updateAchievements() {
        // Example logic for updating achievements
        // Replace with your actual logic for determining achievements

        userHasCompleted30HoursThisMonth { completed ->
            runOnUiThread {
                achievement1CheckBox.isChecked = completed
            }
        }

        userHasCompleted100HoursThisYear { completed ->
            runOnUiThread {
                achievement2CheckBox.isChecked = completed
            }
        }

        userHasCompleted20HoursForOneCategory { completed ->
            runOnUiThread {
                achievement3CheckBox.isChecked = completed
            }
        }

        userHasReachedMaximumGoalToday { completed ->
            runOnUiThread {
                achievement4CheckBox.isChecked = completed
            }
        }


        if (userHasReachedMinimumSetGoalsForThisWeek()) {
            achievement5CheckBox.isChecked = true
        }

        userHasMade5TimesheetEntriesThisWeek { completed ->
            runOnUiThread {
                achievement6CheckBox.isChecked = completed
            }
        }

        userHasCompleted10000Hours { completed ->
            runOnUiThread {
                achievement7CheckBox.isChecked = completed
            }
        }
    }

    private fun userHasCompleted30HoursThisMonth(callback: (Boolean) -> Unit) {
        val username = LoggedInUser.getUsername()
        if (username.isNullOrEmpty()) {
            callback(false)
            return
        }

        val database = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/")
        val timesheetsRef = database.getReference("timesheets").child(username)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        timesheetsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var totalHoursThisMonth = 0

                for (timesheetSnapshot in dataSnapshot.children) {
                    val dateSnapshot = timesheetSnapshot.child("date")
                    if (dateSnapshot.exists()) {
                        val month = dateSnapshot.child("month").getValue(Int::class.java)
                        val year = dateSnapshot.child("year").getValue(Int::class.java)

                        if (month == currentMonth && (year ?: 0) + 1900 == currentYear) {
                            val totalHours = timesheetSnapshot.child("totalHours").getValue(Int::class.java) ?: 0
                            totalHoursThisMonth += totalHours
                        }
                    }
                }

                callback(totalHoursThisMonth >= 30)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Achievements", "Database error: ${databaseError.message}")
                callback(false)
            }
        })
    }

    private fun userHasCompleted100HoursThisYear(callback: (Boolean) -> Unit) {
        val username = LoggedInUser.getUsername()
        if (username.isNullOrEmpty()) {
            callback(false)
            return
        }

        val database = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/")
        val timesheetsRef = database.getReference("timesheets").child(username)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        timesheetsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var totalHoursThisYear = 0

                for (timesheetSnapshot in dataSnapshot.children) {
                    val dateSnapshot = timesheetSnapshot.child("date")
                    if (dateSnapshot.exists()) {
                        val year = dateSnapshot.child("year").getValue(Int::class.java)

                        if ((year ?: 0) + 1900 == currentYear) {
                            val totalHours = timesheetSnapshot.child("totalHours").getValue(Int::class.java) ?: 0
                            totalHoursThisYear += totalHours
                        }
                    }
                }

                callback(totalHoursThisYear >= 100)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Achievements", "Database error: ${databaseError.message}")
                callback(false)
            }
        })
    }

    private fun userHasCompleted20HoursForOneCategory(callback: (Boolean) -> Unit) {
        val username = LoggedInUser.getUsername()
        if (username.isNullOrEmpty()) {
            callback(false)
            return
        }

        val database = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/")
        val timesheetsRef = database.getReference("timesheets").child(username)

        timesheetsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val categoryHoursMap = mutableMapOf<String, Int>()

                for (timesheetSnapshot in dataSnapshot.children) {
                    val category = timesheetSnapshot.child("category").getValue(String::class.java)
                    val totalHours = timesheetSnapshot.child("totalHours").getValue(Int::class.java) ?: 0

                    if (category != null) {
                        categoryHoursMap[category] = categoryHoursMap.getOrDefault(category, 0) + totalHours
                    }
                }

                val hasCategoryWith20OrMoreHours = categoryHoursMap.values.any { it >= 20 }
                callback(hasCategoryWith20OrMoreHours)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Achievements", "Database error: ${databaseError.message}")
                callback(false)
            }
        })
    }

    private fun userHasReachedMaximumGoalToday(callback: (Boolean) -> Unit) {
        val username = LoggedInUser.getUsername()
        if (username.isNullOrEmpty()) {
            callback(false)
            return
        }

        val database = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/")
        val dailyGoalsRef = database.getReference("dailyGoals").child(username)
        val timesheetsRef = database.getReference("timesheets").child(username)
        val currentDate = Calendar.getInstance().time

        // Fetch the daily goal for today
        dailyGoalsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var maximumHours: Int? = null

                for (goalSnapshot in dataSnapshot.children) {
                    val dateSnapshot = goalSnapshot.child("date")
                    if (dateSnapshot.exists()) {
                        val year = dateSnapshot.child("year").getValue(Int::class.java)
                        val month = dateSnapshot.child("month").getValue(Int::class.java)
                        val day = dateSnapshot.child("day").getValue(Int::class.java)

                        if (year != null && month != null && day != null) {
                            val goalDate = Calendar.getInstance().apply {
                                set(year + 1900, month, day)
                            }

                            if (goalDate.time == currentDate) {
                                maximumHours = goalSnapshot.child("maximumHours").getValue(Int::class.java)
                                break
                            }
                        }
                    }
                }

                if (maximumHours == null) {
                    callback(false)
                    return
                }

                // Fetch the timesheet for today
                timesheetsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var totalHoursToday = 0

                        for (timesheetSnapshot in dataSnapshot.children) {
                            val dateSnapshot = timesheetSnapshot.child("date")
                            if (dateSnapshot.exists()) {
                                val year = dateSnapshot.child("year").getValue(Int::class.java)
                                val month = dateSnapshot.child("month").getValue(Int::class.java)
                                val day = dateSnapshot.child("day").getValue(Int::class.java)

                                if (year != null && month != null && day != null) {
                                    val timesheetDate = Calendar.getInstance().apply {
                                        set(year + 1900, month, day)
                                    }

                                    if (timesheetDate.time == currentDate) {
                                        totalHoursToday = timesheetSnapshot.child("totalHours").getValue(Int::class.java) ?: 0
                                        break
                                    }
                                }
                            }
                        }

                        callback(totalHoursToday >= maximumHours)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("Achievements", "Database error: ${databaseError.message}")
                        callback(false)
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Achievements", "Database error: ${databaseError.message}")
                callback(false)
            }
        })
    }

    private fun userHasReachedMinimumSetGoalsForThisWeek(): Boolean {
        // Replace with actual logic
        return false
    }

    private fun userHasMade5TimesheetEntriesThisWeek(callback: (Boolean) -> Unit) {
        val username = LoggedInUser.getUsername()
        if (username.isNullOrEmpty()) {
            callback(false)
            return
        }

        val database = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/")
        val timesheetsRef = database.getReference("timesheets").child(username)

        // Calculate the start and end dates of the current week
        val calendar = Calendar.getInstance()
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val startOfWeek = (calendar.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, -(currentDayOfWeek - Calendar.SUNDAY))
        }
        val endOfWeek = (calendar.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, Calendar.SATURDAY - currentDayOfWeek)
        }

        timesheetsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var timesheetCountThisWeek = 0

                for (timesheetSnapshot in dataSnapshot.children) {
                    val dateSnapshot = timesheetSnapshot.child("date")
                    if (dateSnapshot.exists()) {
                        val year = dateSnapshot.child("year").getValue(Int::class.java)
                        val month = dateSnapshot.child("month").getValue(Int::class.java)
                        val day = dateSnapshot.child("day").getValue(Int::class.java)

                        if (year != null && month != null && day != null) {
                            val entryDate = Calendar.getInstance().apply {
                                set(year + 1900, month, day)
                            }

                            if (entryDate.timeInMillis in startOfWeek.timeInMillis..endOfWeek.timeInMillis) {
                                timesheetCountThisWeek++
                            }
                        }
                    }
                }

                callback(timesheetCountThisWeek >= 5)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Achievements", "Database error: ${databaseError.message}")
                callback(false)
            }
        })
    }

    private fun userHasCompleted10000Hours(callback: (Boolean) -> Unit) {
        val username = LoggedInUser.getUsername()
        if (username.isNullOrEmpty()) {
            callback(false)
            return
        }

        val database = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/")
        val timesheetsRef = database.getReference("timesheets").child(username)

        timesheetsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var totalHours = 0

                for (timesheetSnapshot in dataSnapshot.children) {
                    val hours = timesheetSnapshot.child("totalHours").getValue(Int::class.java) ?: 0
                    totalHours += hours
                }

                callback(totalHours >= 10000)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Achievements", "Database error: ${databaseError.message}")
                callback(false)
            }
        })
    }
}

