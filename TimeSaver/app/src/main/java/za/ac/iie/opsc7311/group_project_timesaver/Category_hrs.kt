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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Activity for calculating the total number of hours for each category in a user selectable period of time
class Category_hrs : AppCompatActivity() {

    // Variable declarations
    private lateinit var categoryNamesTextView: TextView
    private lateinit var generateTableButton: Button
    private lateinit var startDatePicker: EditText
    private lateinit var endDatePicker: EditText
    private lateinit var timesheetList: ListView
    lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_hours)

        timesheetList = findViewById(R.id.timesheetListView)
        categoryNamesTextView = findViewById(R.id.categoryNamesTextView)
        generateTableButton = findViewById(R.id.generateTableButton)
        startDatePicker = findViewById(R.id.startDate)
        endDatePicker = findViewById(R.id.endDate)
        back = findViewById(R.id.back_button)

        startDatePicker.setOnClickListener { showStartDatePicker() } // Shows the date picker for the start date when clicked
        endDatePicker.setOnClickListener { showEndDatePicker() } // Shows the date picker for the end date when clicked

        generateTableButton.setOnClickListener { displayCategoryNames() } // Displays the category names when clicked

        back.setOnClickListener {
            val intent = Intent(this, view_menu::class.java) // Back button
            startActivity(intent)
        }
    }

    // Display the category names with the total hours next to it
    private fun displayCategoryNames() {
        val startDate = startDatePicker.text.toString()
        val endDate = endDatePicker.text.toString()

        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please enter a start date and end date", Toast.LENGTH_SHORT).show() //Have to enter a start date and end date
            return
        }

        val username = LoggedInUser.getUsername() ?: return // Gets the username for the current logged in user

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDateDate = dateFormat.parse(startDate)
        val endDateDate = dateFormat.parse(endDate)

        val databaseReference = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/") // Database connection
            .getReference("timesheets").child(username) // Gets the timesheets for the user

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryHoursMap = mutableMapOf<String, Double>() // A list of the category names with their total hours

                // Goes through every timesheet entry
                for (entrySnapshot in snapshot.children) {
                    val timesheetEntry = entrySnapshot.getValue(TimesheetEntry::class.java)
                    if (timesheetEntry != null) {
                        val entryDate = timesheetEntry.date // Gets the date of each timesheet
                        if (entryDate != null && entryDate in startDateDate..endDateDate) { // Sees if the timesheet date fits between the start and end date entered
                            val category = timesheetEntry.category // Gets the category for the timesheet entry
                            val totalHours = timesheetEntry.totalHours // Gets the totalHours for the timesheet entry

                            categoryHoursMap[category] = categoryHoursMap.getOrDefault(category, 0.0) + totalHours // Adds each category with its total hours
                        }
                    }
                }

                if (categoryHoursMap.isEmpty()) {
                    categoryNamesTextView.visibility = View.GONE
                    Toast.makeText(this@Category_hrs, "No categories found.", Toast.LENGTH_SHORT).show()
                } else {
                    val timesheetNames = categoryHoursMap.map { (category, totalHours) ->
                        val formattedHours = if (totalHours % 1 == 0.0) {
                            String.format("%.0f", totalHours)
                        } else {
                            String.format("%.1f", totalHours)
                        }
                        "$category - $formattedHours hours"
                    }

                    val adapter = ArrayAdapter(this@Category_hrs, android.R.layout.simple_list_item_1, timesheetNames)
                    timesheetList.adapter = adapter
                    timesheetList.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Category_hrs, "Error loading timesheet entries", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to display the date picker and save the date as the start date
    private fun showStartDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day -> startDatePicker.setText("$year-${month + 1}-$day") },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    // Function to display the date picker and save the date as the end date
    private fun showEndDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day -> endDatePicker.setText("$year-${month + 1}-$day") },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }
}
