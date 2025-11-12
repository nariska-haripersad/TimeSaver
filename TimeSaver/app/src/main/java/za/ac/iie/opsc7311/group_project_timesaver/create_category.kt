// CreateCategory.kt
package za.ac.iie.opsc7311.group_project_timesaver

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// to create and add categories to be used in timesheet entries
class create_category : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category)

        nameEditText = findViewById(R.id.editTextText)
        descriptionEditText = findViewById(R.id.editTextText3)
        saveButton = findViewById(R.id.button)
        back = findViewById(R.id.back_button)

        saveButton.setOnClickListener {
            val username = LoggedInUser.getUsername() ?: return@setOnClickListener
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()

            if (name.isNotEmpty()) {
                val category = Category(name, description)

                CategoryManager.addCategory(username, category)

                Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show()

                nameEditText.text.clear()
                descriptionEditText.text.clear()
            } else {
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show()
            }
        }

        back.setOnClickListener {
            val intent = Intent(this, Main_Menu::class.java)
            startActivity(intent)
        }
    }
}


