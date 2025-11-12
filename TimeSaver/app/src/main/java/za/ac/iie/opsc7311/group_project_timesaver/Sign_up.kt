// SignUp.kt
package za.ac.iie.opsc7311.group_project_timesaver

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUp : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonRegister: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var loginButton: TextView
    private lateinit var back: ImageView
    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextEmail = findViewById(R.id.editTextEmailAddress)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)
        loginButton = findViewById(R.id.loginOption)
        back = findViewById(R.id.back_button)

        // Set click listener for login button
        loginButton.setOnClickListener {
            // Launch the Login activity when the button is clicked
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        buttonRegister.setOnClickListener {
            val username = editTextUsername.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            database = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/")
            reference = database.getReference("users")

            // Perform input validation for username and email
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Setting password requirements/conditions, this method was adapted from (annianni.2021)
            fun validatePassword(pwd: String): Boolean {
                val minLength = 12

                // Check length
                if (pwd.length < minLength) {
                    return false
                }

                // Check for at least one uppercase letter
                if (!pwd.any { it.isUpperCase() }) {
                    return false
                }

                // Check for at least one lowercase letter
                if (!pwd.any { it.isLowerCase() }) {
                    return false
                }

                // Check for at least one digit
                if (!pwd.any { it.isDigit() }) {
                    return false
                }

                // Check for at least one special character
                val specialCharacters = setOf('!', '@', '#', '$', '%', '^', '&', '*')
                if (!pwd.any { it in specialCharacters }) {
                    return false
                }
                return true
            }

            //Performing password validation
            if (validatePassword(password)) {
                val user = User(email, username, password)
                reference.child(username).setValue(user)
                        Toast.makeText(
                            this,
                            "Registration successful. Welcome: $username",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

            else {
                Toast.makeText(
                    this,
                    "Invalid password. Please choose a stronger password.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    data class User(val email: String, val username: String, val password: String)
}
//Reference List:
//annianni.2021.How to Validate Password from Text Input in Android?, 11 June 2021. [Online]. Available at:https://www.geeksforgeeks.org/how-to-validate-password-from-text-input-in-android/ [Accessed 5 May 2024]
