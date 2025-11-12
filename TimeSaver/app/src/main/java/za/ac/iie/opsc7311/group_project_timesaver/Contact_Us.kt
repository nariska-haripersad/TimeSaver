package za.ac.iie.opsc7311.group_project_timesaver

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Contact_Us : AppCompatActivity() {

    lateinit var submit: Button
    lateinit var emailadd: EditText
    lateinit var text_help: EditText
    lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        submit = findViewById(R.id.button4)
        back = findViewById(R.id.back_button)
        emailadd = findViewById(R.id.editTextEmailAddress)
        text_help = findViewById(R.id.editTextHelp)

        back.setOnClickListener {
            val intent = Intent(this, Main_Menu::class.java)
            startActivity(intent)
        }

        submit.setOnClickListener {
            val email = emailadd.text.toString()
            val help = text_help.text.toString()

                if (email.isNotEmpty()  || help.isNotEmpty()) {
                    Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Message_Sent::class.java)
                    startActivity(intent)
                }
            }

        }
    }
