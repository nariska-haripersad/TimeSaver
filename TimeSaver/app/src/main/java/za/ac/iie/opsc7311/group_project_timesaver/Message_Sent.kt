package za.ac.iie.opsc7311.group_project_timesaver

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Message_Sent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var done: Button

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_sent)
        done = findViewById(R.id.button5)


        done.setOnClickListener {
            val intent = Intent(this, Main_Menu::class.java)
            startActivity(intent)
        }

        }
    }