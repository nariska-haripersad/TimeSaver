package za.ac.iie.opsc7311.group_project_timesaver

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

class TotalHoursWorkedActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.total_hours_worked)

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
