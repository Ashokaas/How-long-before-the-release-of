package fr.ashokas.nintendogamesreleases

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Hides the header
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Notification bar color change only available after Lollipop (Android 5.0)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.grey)
        }

        val myTextView = findViewById<TextView>(R.id.guthib_link)
        myTextView.setOnClickListener {
            val uri = Uri.parse("https://github.com/Ashokaas/How-long-before-the-release-of")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }



        // Close the application when you click on the button.
        val quitButton = findViewById<Button>(R.id.exit_button)
        quitButton.setOnClickListener {
            finish()
        }
    }
}