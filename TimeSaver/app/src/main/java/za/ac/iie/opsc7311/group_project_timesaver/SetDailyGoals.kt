package za.ac.iie.opsc7311.group_project_timesaver

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SetDailyGoals : AppCompatActivity() {

    private lateinit var doneButton: Button
    private lateinit var projectNameEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var minimumHoursEditText: EditText
    private lateinit var maximumHoursEditText: EditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var backButton: ImageView

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_daily_goals)

        // Initialize UI components
        doneButton = findViewById(R.id.doneButton)
        projectNameEditText = findViewById(R.id.editTextProject)
        dateEditText = findViewById(R.id.editTextDate)
        descriptionEditText = findViewById(R.id.editTextDescription)
        minimumHoursEditText = findViewById(R.id.editMinimum)
        maximumHoursEditText = findViewById(R.id.editMaximum)
        prioritySpinner = findViewById(R.id.spinnerPriority)
        backButton = findViewById(R.id.back_button)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/").reference

        // Set up listeners
        doneButton.setOnClickListener {
            saveDailyGoal()
        }

        backButton.setOnClickListener {
            val intent = Intent(this, Main_Menu::class.java)
            startActivity(intent)
        }

        dateEditText.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                dateEditText.setText("$year-${month + 1}-$day")
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun saveDailyGoal() {
        val dateStr = dateEditText.text.toString().trim()
        val projectName = projectNameEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val minimumHours = minimumHoursEditText.text.toString().toIntOrNull() ?: 0
        val maximumHours = maximumHoursEditText.text.toString().toIntOrNull() ?: 0
        val priorityLevel = prioritySpinner.selectedItem.toString()

        // Validate the input
        if (dateStr.isEmpty() || projectName.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Convert dateStr to Date object
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: Date? = try {
            dateFormat.parse(dateStr)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show()
            return
        }

        if (date == null) {
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrieve the logged-in user's username
        val username = LoggedInUser.getUsername()
        if (username == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a DailyGoal object
        val dailyGoal = DailyGoal(date, projectName, description, minimumHours, maximumHours, priorityLevel)

        // Save to Firebase Database under the specific user's node
        database.child("dailyGoals").child(username).push().setValue(dailyGoal)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Daily goal saved", Toast.LENGTH_SHORT).show()
                    clearFields()
                } else {
                    Toast.makeText(this, "Failed to save daily goal", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun clearFields() {
        dateEditText.text.clear()
        projectNameEditText.text.clear()
        descriptionEditText.text.clear()
        minimumHoursEditText.text.clear()
        maximumHoursEditText.text.clear()
        prioritySpinner.setSelection(0)
    }
}
