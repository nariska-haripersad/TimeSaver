package za.ac.iie.opsc7311.group_project_timesaver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class view_menu : AppCompatActivity() {
    lateinit var sheet: Button
    lateinit var categoryHrs: Button
    lateinit var worked_hrs: Button
    lateinit var daily: Button
    lateinit var archived: Button
    lateinit var achievements: Button
    lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_menu)

        sheet = findViewById(R.id.timesheets_button)
        categoryHrs = findViewById(R.id.total_category_button)
        worked_hrs = findViewById(R.id.total_hrs_button)
        daily = findViewById(R.id.daily_goals_button)
        archived = findViewById(R.id.archived_button)
        achievements = findViewById(R.id.achievements_button)
        back = findViewById(R.id.back_button)

        //adding event handlers
        sheet.setOnClickListener {
           val intent = Intent(this, timesheet_viewing::class.java)
            startActivity(intent)
       }
         categoryHrs.setOnClickListener {
        val intent = Intent(this, Category_hrs::class.java)
     startActivity(intent)
        }
        worked_hrs.setOnClickListener {
            val intent = Intent(this, TotalHoursWorkedActivity::class.java)
            startActivity(intent)
        }
          daily.setOnClickListener {
            val intent = Intent(this, View_daily_goals::class.java)
<<<<<<< HEAD
            startActivity(intent)
        }
=======
           startActivity(intent)
       }
        //  archived.setOnClickListener {
//            val intent = Intent(this, archived_screen::class.java)
//            startActivity(intent)
//        }
>>>>>>> 3744471 (Bar graph added)
        achievements.setOnClickListener {
            val intent = Intent(this, Achievements::class.java)
          startActivity(intent)
         }
          back.setOnClickListener {
            val intent = Intent(this, Main_Menu::class.java)
            startActivity(intent)
        }


    }
}