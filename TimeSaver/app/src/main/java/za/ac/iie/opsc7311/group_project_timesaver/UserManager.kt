package za.ac.iie.opsc7311.group_project_timesaver

// to manage the user session and data
object LoggedInUser {
    // gets and sets the username of the user currently logged in
    private var username: String? = null

    fun setUsername(username: String) {
        this.username = username
    }

    fun getUsername(): String? {
        return username
    }
}

