// Category.kt
package za.ac.iie.opsc7311.group_project_timesaver

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// a class for managing the storage and retrieval of category data
data class Category(
    val name: String = "",
    val description: String = ""
)
// manage categories
    object CategoryManager {
    private val userCategories: MutableMap<String, MutableList<Category>> = mutableMapOf()

    fun addCategory(username: String, category: Category)
    {
        lateinit var database: FirebaseDatabase
        lateinit var reference: DatabaseReference
        database = FirebaseDatabase.getInstance("https://opsc7311-p-default-rtdb.firebaseio.com/")
        reference = database.getReference("categories")
        reference.child(username).push().setValue(category)
    }
// get the categories for the current user
    fun getUserCategories(username: String): List<Category>? {
        return userCategories[username]
    }
}

