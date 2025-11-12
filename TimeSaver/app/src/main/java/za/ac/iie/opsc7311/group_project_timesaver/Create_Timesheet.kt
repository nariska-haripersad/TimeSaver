package za.ac.iie.opsc7311.group_project_timesaver

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class CreateTimesheet : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var pickCategory: Spinner
    private lateinit var datePicker: EditText
    private lateinit var startTimePicker: EditText
    private lateinit var endTimePicker: EditText
    private lateinit var addButton: Button
    private lateinit var addImageButton: Button
    private lateinit var selectedImageView: ImageView
    lateinit var back: ImageView

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: String? = null  // store the selected image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_timesheet)

        // initialize views
        nameEditText = findViewById(R.id.nameEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        pickCategory = findViewById(R.id.categorySpinner)
        datePicker = findViewById(R.id.pickDate)
        startTimePicker = findViewById(R.id.pickStartTime)
        endTimePicker = findViewById(R.id.pickEndTime)
        addButton = findViewById(R.id.addSheet)
        addImageButton = findViewById(R.id.addImage)
        selectedImageView = findViewById(R.id.selectedImageView)
        back = findViewById(R.id.back_button)

        setUpCategorySpinner() // category spinner is set up


        datePicker.setOnClickListener { showDatePicker() } // click to add date
        startTimePicker.setOnClickListener { showStartTimePicker() } // click to add start time
        endTimePicker.setOnClickListener { showEndTimePicker() } //click to add end time
        addImageButton.setOnClickListener { openGallery() } // click to open photo gallery

        // button to add a timesheet
        addButton.setOnClickListener {
            addTimesheetEntry()
        }
        back.setOnClickListener {
            val intent = Intent(this, Main_Menu::class.java)
            startActivity(intent)
        }
    }

    // to open the gallery
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data.toString()
            selectedImageView.setImageURI(data.data)
            selectedImageView.visibility = View.VISIBLE
        }
    }

    // to add a timesheet entry
    private fun addTimesheetEntry() {
        val name = nameEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val category = pickCategory.selectedItem.toString()
        val dateStr = datePicker.text.toString()
        val startTimeStr = startTimePicker.text.toString()
        val endTimeStr = endTimePicker.text.toString()

        // Validate if all fields are filled
        if (name.isEmpty() || description.isEmpty() || category.isEmpty() || dateStr.isEmpty() || startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        try {
            val date = dateFormat.parse(dateStr)
            val startTime = timeFormat.parse(startTimeStr)
            val endTime = timeFormat.parse(endTimeStr)

            // Calculate total hours
            val totalHours = if (startTime != null && endTime != null) {
                val diff = endTime.time - startTime.time
                diff / (1000 * 60 * 60.0)  // Convert milliseconds to hours
            } else {
                0.0
            }

            // Get the logged-in user's username
            val username = LoggedInUser.getUsername() ?: return

            // Create a TimesheetEntry object
            val entry = TimesheetEntry(name, description, category, date, startTime, endTime, selectedImageUri, totalHours)

            // Add the entry to the database using TimesheetManager
            TimesheetManager.addTimesheetEntry(username, entry) { success ->
                if (success) {
                    Toast.makeText(this, "Timesheet added", Toast.LENGTH_SHORT).show()
                    clearFields()
                } else {
                    Toast.makeText(this, "Error adding timesheet entry", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error adding timesheet entry", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun setUpCategorySpinner() {
        val username = LoggedInUser.getUsername() ?: return
        val databaseReference = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/").getReference("categories").child(username)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryNames = mutableListOf<String>()
                for (categorySnapshot in snapshot.children) {
                    val categoryName = categorySnapshot.child("name").getValue(String::class.java)
                    if (categoryName != null) {
                        categoryNames.add(categoryName)
                    }
                }

                val adapter = ArrayAdapter(this@CreateTimesheet, android.R.layout.simple_spinner_item, categoryNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                pickCategory.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CreateTimesheet, "Error loading categories", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // clear the fields
    private fun clearFields() {
        nameEditText.text.clear()
        descriptionEditText.text.clear()
        datePicker.text.clear()
        startTimePicker.text.clear()
        endTimePicker.text.clear()
        selectedImageUri = null
        selectedImageView.setImageURI(null)
        selectedImageView.visibility = View.GONE
    }

    // shows the date picker and displays the date in an appropriate format to the user
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                // set the selected date to the EditText
                datePicker.setText("$year-${month + 1}-$day")
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    // shows the time picker for picking the start time
    private fun showStartTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                // Set the selected time to the EditText
                startTimePicker.setText("$hourOfDay:$minute")
            },
            hour,
            minute,
            true // 24-hour format
        )
        timePickerDialog.show()
    }

    // shows the time picker for picking the end time
    private fun showEndTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                // set the selected time to the EditText
                endTimePicker.setText("$hourOfDay:$minute")
            },
            hour,
            minute,
            true // 24-hour format
        )
        timePickerDialog.show()
    }
}
