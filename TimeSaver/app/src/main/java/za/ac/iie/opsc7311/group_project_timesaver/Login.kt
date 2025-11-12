package za.ac.iie.opsc7311.group_project_timesaver

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editText_password)
        buttonLogin = findViewById(R.id.button2)
        back = findViewById(R.id.back_button)

        buttonLogin.setOnClickListener {
            // gets the username and password used during sign up and the username and password entered during login
            val enteredUsername = editTextUsername.text.toString()
            val enteredPassword = editTextPassword.text.toString()

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                checkUser()
            }
        }

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkUser() {
        val username = editTextUsername.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        // Firebase initialization
        val database = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/")
        val reference = database.reference.child("users")

        val checkUserQuery = reference.orderByChild("username").equalTo(username)

        checkUserQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    editTextUsername.error = null
                    val passwordFromDB =
                        snapshot.child(username).child("password").getValue(String::class.java)
                            ?: ""
                    if (password == passwordFromDB) {
                        // Login successful
                        Toast.makeText(this@Login, "Login successful", Toast.LENGTH_SHORT)
                            .show()
                        LoggedInUser.setUsername(username)
                        val intent = Intent(this@Login, Main_Menu::class.java)
                        startActivity(intent)
                    } else {
                        editTextPassword.error = "Invalid Credentials"
                        editTextPassword.requestFocus()
                    }
                } else {
                    editTextUsername.error = "User does not exist"
                    editTextUsername.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database errors
            }
        })
    }
}
