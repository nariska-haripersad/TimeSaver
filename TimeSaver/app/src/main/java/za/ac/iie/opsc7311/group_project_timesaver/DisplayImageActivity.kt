package za.ac.iie.opsc7311.group_project_timesaver

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DisplayImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_image)

        // retrieve image URI from intent
        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(imageUriString)

        // load and display the image
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageURI(imageUri)
    }
}
