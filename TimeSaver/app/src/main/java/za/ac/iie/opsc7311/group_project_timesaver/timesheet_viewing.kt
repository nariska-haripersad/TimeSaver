package za.ac.iie.opsc7311.group_project_timesaver

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// To view the list of timesheet entries
class timesheet_viewing : AppCompatActivity() {

    private lateinit var generateTableButton: Button
    private lateinit var startDatePicker: EditText
    private lateinit var endDatePicker: EditText
    private lateinit var timesheetList: ListView
    lateinit var back: ImageView
    private var startDateSelected = false
    private var endDateSelected = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_timesheet)

        timesheetList = findViewById(R.id.timesheetListView)
        generateTableButton = findViewById(R.id.generateTableButton)
        startDatePicker = findViewById(R.id.startDate)
        endDatePicker = findViewById(R.id.endDate)
        back = findViewById(R.id.back_button)

        // For the user to enter a start date
        startDatePicker.setOnClickListener {
            showStartDatePicker() }
        // For the user to enter a end date
        endDatePicker.setOnClickListener {
            showEndDatePicker() }

        generateTableButton.setOnClickListener {
            if (startDateSelected && endDateSelected) { // if a start date and end date was entered, the timesheets created during that time will display
                displayTimesheetNames()
            } else {
                Toast.makeText(this, "Please enter a start date and end date", Toast.LENGTH_SHORT).show()
            }
        }

        back.setOnClickListener {
            val intent = Intent(this, view_menu::class.java)
            startActivity(intent)
        }
    }

    private fun displayTimesheetNames() {
        val username = LoggedInUser.getUsername() ?: return

        // Get selected start and end dates
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate = dateFormatter.parse(startDatePicker.text.toString()) ?: return
        val endDate = dateFormatter.parse(endDatePicker.text.toString()) ?: return

        // Reference to the user's timesheet entries in the database
        val databaseReference = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/")
            .getReference("timesheets").child(username)

        // Query to get timesheet entries within the date range
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val timesheetEntries = mutableListOf<TimesheetEntry>()
                val timesheetNames = mutableListOf<String>()

                for (entrySnapshot in snapshot.children) {
                    val timesheetEntry = entrySnapshot.getValue(TimesheetEntry::class.java)
                    if (timesheetEntry != null) {
                        val entryDate = timesheetEntry.date
                        if (entryDate != null && entryDate in startDate..endDate) {
                            timesheetNames.add(timesheetEntry.name)
                            timesheetEntries.add(timesheetEntry)
                        }
                    }
                }

                if (timesheetNames.isEmpty()) {
                    Toast.makeText(this@timesheet_viewing, "No timesheet entries found", Toast.LENGTH_SHORT).show()
                } else {
                    val adapter = ArrayAdapter(this@timesheet_viewing, android.R.layout.simple_list_item_1, timesheetNames)
                    timesheetList.adapter = adapter
                    timesheetList.visibility = View.VISIBLE

                    timesheetList.setOnItemClickListener { _, _, position, _ ->
                        val selectedTimesheet = timesheetEntries[position]

                        // Convert Date objects to strings
                        val dateStr = dateFormatter.format(selectedTimesheet.date)
                        val startTimeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedTimesheet.startTime)
                        val endTimeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedTimesheet.endTime)

                        val intent = Intent(this@timesheet_viewing, TimesheetDetailsActivity::class.java)
                        intent.putExtra("name", selectedTimesheet.name)
                        intent.putExtra("description", selectedTimesheet.description)
                        intent.putExtra("category", selectedTimesheet.category)
                        intent.putExtra("date", dateStr) // Date as string
                        intent.putExtra("startTime", startTimeStr) // Start time as string
                        intent.putExtra("endTime", endTimeStr) // End time as string
                        intent.putExtra("imageUrl", selectedTimesheet.imageUri)
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@timesheet_viewing, "Error loading timesheet entries", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showStartDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                // Set the selected date to the EditText
                startDatePicker.setText("$year-${month + 1}-$day")
                startDateSelected = true
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun showEndDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                // Set the selected date to the EditText
                endDatePicker.setText("$year-${month + 1}-$day")
                endDateSelected = true
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

}



