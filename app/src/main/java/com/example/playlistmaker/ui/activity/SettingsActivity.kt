package com.example.playlistmaker.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial

const val SHARED_PREFERENCES = "preference_for_settings"
const val KEY_FOR_THEME = "key_for_theme"

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
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
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
           sharedPrefs.edit()
               .putBoolean(KEY_FOR_THEME, checked)
               .apply()
        }
        themeSwitcher.isChecked = sharedPrefs.getBoolean(KEY_FOR_THEME, false)
    }
}

