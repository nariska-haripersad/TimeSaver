package za.ac.iie.opsc7311.group_project_timesaver

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

// the main menu containing various options of what the user can do
class Main_Menu : AppCompatActivity() {
     lateinit var category: TextView
     lateinit var timesheet: TextView
     lateinit var goal: TextView
     lateinit var view_text: TextView
     lateinit var messages: TextView
     lateinit var contact: TextView
     lateinit var back: ImageView

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)

        category = findViewById(R.id.categoryText)
        timesheet = findViewById(R.id.timesheetTextview)
        goal = findViewById(R.id.setDailyGoal)
        view_text = findViewById(R.id.view)
        messages = findViewById(R.id.Notifications)
        contact = findViewById(R.id.contact_textView)
        back = findViewById(R.id.back_button)

        //adding event handlers
        category.setOnClickListener {
            val intent = Intent(this, create_category::class.java)
            startActivity(intent)
        }
        timesheet.setOnClickListener {
            val intent = Intent(this, CreateTimesheet::class.java)
           startActivity(intent)
     }
        goal.setOnClickListener {
            val intent = Intent(this, SetDailyGoals::class.java)
            startActivity(intent)
        }
           view_text.setOnClickListener {
            val intent = Intent(this, view_menu::class.java)
            startActivity(intent)
        }
       messages.setOnClickListener {
          val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
       }
         contact.setOnClickListener {
            val intent = Intent(this, Contact_Us::class.java)
            startActivity(intent)
        }

        back.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
}