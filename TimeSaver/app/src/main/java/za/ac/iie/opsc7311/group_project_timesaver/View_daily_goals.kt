package za.ac.iie.opsc7311.group_project_timesaver

<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Toast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

data class Timesheet_Entry(
    val date: String = "",
    val totalHours: Double = 0.0
)

data class Daily_Goal(
    val date: String = "",
    val minimumHours: Double = 0.0,
    val maximumHours: Double = 0.0
)


class View_daily_goals : AppCompatActivity() {
    private lateinit var yearPicker: NumberPicker
    private lateinit var monthPicker: NumberPicker
    private lateinit var back: ImageView
    private lateinit var database: DatabaseReference
    private lateinit var save: Button
=======
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class View_daily_goals : AppCompatActivity() {

    private lateinit var startDate: EditText
    private lateinit var endDate: EditText
    private lateinit var selectTime: TextView
    private lateinit var calendar: ImageView
    private lateinit var toText: TextView
    private lateinit var calendar1: ImageView
    private lateinit var saveButton: Button
    private lateinit var categoryHours: TextView
    private lateinit var backButton: ImageView
    private lateinit var imageView3: ImageView
>>>>>>> 3744471 (Bar graph added)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_daily_goals)

<<<<<<< HEAD
        yearPicker = findViewById(R.id.yearPicker)
        monthPicker = findViewById(R.id.monthPicker)
        save = findViewById(R.id.saveButton)
                back = findViewById(R.id.back_button2)
                database = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/").reference

                val lineChart: LineChart = findViewById(R.id.idLineChart) // Make sure this ID matches your layout

        // Set up the year picker
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        yearPicker.minValue = currentYear - 10
        yearPicker.maxValue = currentYear
        yearPicker.value = currentYear

        // Set up the month picker
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1
        back.setOnClickListener {
            val intent = Intent(this, view_menu::class.java)
            startActivity(intent)
        }
        save.setOnClickListener {
            test()
//             fetchAndDisplayData(lineChart)
                }
            }

    private fun test(){
        Toast.makeText(applicationContext, "message", Toast.LENGTH_SHORT).show()
    }

            private fun fetchAndDisplayData(lineChart: LineChart) {
                val year = yearPicker.value.toString()
                val month = monthPicker.value.toString()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val years = dateFormat.parse(year) ?: return
                val months = dateFormat.parse(month) ?: return

                val username = LoggedInUser.getUsername() ?: return
                val timesheetsRef = database.child("timesheets").child(username)
                val dailyGoalsRef = database.child("dailyGoals").child(username)

                timesheetsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val timesheetEntries = mutableListOf<Timesheet_Entry>()
                        for (entrySnapshot in dataSnapshot.children) {
                            val timesheetEntry = entrySnapshot.getValue(Timesheet_Entry::class.java)
                            if (timesheetEntry != null) {
                                val entryDate = dateFormat.parse(timesheetEntry.date)
                                if (entryDate != null && entryDate in months..years) {
                                    timesheetEntries.add(timesheetEntry)
                                }
                            }
                        }
                        dailyGoalsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(goalSnapshot: DataSnapshot) {
                                val dailyGoals = mutableListOf<Daily_Goal>()
                                for (goalEntrySnapshot in goalSnapshot.children) {
                                    val dailyGoal = goalEntrySnapshot.getValue(Daily_Goal::class.java)
                                    if (dailyGoal != null) {
                                        val goalDate = dateFormat.parse(dailyGoal.date)
                                        if (goalDate != null && goalDate in months..years) {
                                            dailyGoals.add(dailyGoal)
                                        }
                                    }
                                }
                                createLineChart(lineChart, timesheetEntries, dailyGoals)
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.e("TotalHoursWorked", "Database error: ${databaseError.message}")
                            }
                        })
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("TotalHoursWorked", "Database error: ${databaseError.message}")
                    }
                })
            }

            private fun createLineChart(lineChart: LineChart, timesheetEntries: List<Timesheet_Entry>, dailyGoals: List<Daily_Goal>) {
                val entries = mutableListOf<Entry>()
                val minEntries = mutableListOf<Entry>()
                val maxEntries = mutableListOf<Entry>()

                // Assuming you want to create line chart entries based on the timesheetEntries and dailyGoals
                timesheetEntries.forEach { timesheetEntry ->
                    val goal = dailyGoals.find { it.date == timesheetEntry.date }
                    if (goal != null) {
                        val dateIndex = getDayIndex(timesheetEntry.date) // Use the date as the index
                        entries.add(Entry(dateIndex, timesheetEntry.totalHours.toFloat()))
                        minEntries.add(Entry(dateIndex, goal.minimumHours.toFloat()))
                        maxEntries.add(Entry(dateIndex, goal.maximumHours.toFloat()))
                    }
                }

                val dataSet = LineDataSet(entries, "Total Hours")
                dataSet.color = Color.RED
                val minDataSet = LineDataSet(minEntries, "Minimum Hours")
                minDataSet.color = Color.GREEN
                val maxDataSet = LineDataSet(maxEntries, "Maximum Hours")
                maxDataSet.color = Color.BLUE

                val lineData = LineData(dataSet, minDataSet, maxDataSet)
                lineChart.data = lineData

                // Customize chart properties
                lineChart.description.isEnabled = false // Hide description
                lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM // X-axis position
                lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(getXAxisLabels()) // Set X-axis labels
                lineChart.axisRight.isEnabled = false // Hide right Y-axis

                // Customize X-axis (bottom axis)
                lineChart.xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    valueFormatter = IndexAxisValueFormatter(getXAxisLabels())
                }

                // Customize Y-axis (left axis)
                lineChart.axisLeft.apply {
                    setDrawLabels(true)
                    axisMinimum = 0f
                    axisMaximum = 100f
                }

                // Refresh the chart
                lineChart.invalidate()
            }

            // Function to get the index of the day (e.g., Monday -> 1, Tuesday -> 2, ...)
            fun getDayIndex(day: String): Float {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = dateFormat.parse(day)
                val calendar = Calendar.getInstance()
                calendar.time = date!!
                return (calendar.get(Calendar.DAY_OF_WEEK) - 1).toFloat() // Convert Sunday = 1, Monday = 2, etc.
            }

            // Function to get X-axis labels (days of the week)
            private fun getXAxisLabels(): List<String> {
                return listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
            }
        }
=======
        // Initialize the views
        startDate = findViewById(R.id.start_date)
        endDate = findViewById(R.id.end_date)
        selectTime = findViewById(R.id.selectTime)
        calendar = findViewById(R.id.calender)
        toText = findViewById(R.id.to)
        calendar1 = findViewById(R.id.calender1)
        saveButton = findViewById(R.id.saveButton)
        categoryHours = findViewById(R.id.categoryHours2)
        backButton = findViewById(R.id.back_button2)
        imageView3 = findViewById(R.id.imageView3)

        // Initially hide the ImageView
        imageView3.visibility = View.INVISIBLE

        // Set up DatePickers for start and end dates
        startDate.setOnClickListener {
            showDatePickerDialog(startDate)
        }

        endDate.setOnClickListener {
            showDatePickerDialog(endDate)
        }

        // Set a click listener on the saveButton
        saveButton.setOnClickListener {
            imageView3.visibility = View.VISIBLE
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                editText.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
>>>>>>> 3744471 (Bar graph added)
