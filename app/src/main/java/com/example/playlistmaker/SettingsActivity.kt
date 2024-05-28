package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<Button>(R.id.settings_to_main).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.button_share).setOnClickListener {
            val message = getString(R.string.urlToShare)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(shareIntent, "Share with"))
        }
        findViewById<Button>(R.id.button_support).setOnClickListener {
                val message = getString(R.string.supportMessage)
                val title = getString(R.string.supportMessageTitle)
                val shareIntent = Intent(Intent.ACTION_SENDTO)
                shareIntent.data = Uri.parse("mailto:")
                shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportEmail)))
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, title)
                shareIntent.putExtra(Intent.EXTRA_TEXT, message)
                startActivity(shareIntent)
        }
        findViewById<Button>(R.id.button_terms_of_use).setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.urlTermsOfUse))
                    )
            )
        }
    }
}

