package za.ac.iie.opsc7311.group_project_timesaver

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class TimesheetDetailsActivity : AppCompatActivity() {

    private lateinit var timesheetName: TextView
    private lateinit var description: TextView
    private lateinit var category: TextView
    private lateinit var date: TextView
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView
    private lateinit var timesheetImage: ImageView
    lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet_details)

        timesheetName = findViewById(R.id.timesheetName)
        description = findViewById(R.id.description)
        category = findViewById(R.id.category)
        date = findViewById(R.id.date)
        startTime = findViewById(R.id.startTime)
        endTime = findViewById(R.id.endTime)
        timesheetImage = findViewById(R.id.timesheetImage)
        back = findViewById(R.id.back_button)

        // Get data from intent
        val name = intent.getStringExtra("name")
        val desc = intent.getStringExtra("description")
        val cat = intent.getStringExtra("category")
        val dateStr = intent.getStringExtra("date")
        val start = intent.getStringExtra("startTime")
        val end = intent.getStringExtra("endTime")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Set data to views
        timesheetName.text = name
        description.text = desc
        category.text = cat
        date.text = dateStr
        startTime.text = start
        endTime.text = end

        // Load image using Glide or any other image loading library
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this).load(imageUrl).into(timesheetImage)
        }

        back.setOnClickListener {
            val intent = Intent(this, timesheet_viewing::class.java)
            startActivity(intent)
        }
    }
}
