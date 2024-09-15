package com.example.playlistmaker.ui.settings.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingViewModel
    private lateinit var binding: ActivitySettingsBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, SettingViewModel.getViewModelFactory())[SettingViewModel::class.java]
        val themeSwitcher = binding.themeSwitcher
        binding.settingsToMain.setOnClickListener {
            finish()
        }
        binding.buttonShare.setOnClickListener {
            val message = getString(R.string.urlToShare)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(shareIntent, "Share with"))
        }
        binding.buttonSupport.setOnClickListener {
                val message = getString(R.string.supportMessage)
                val title = getString(R.string.supportMessageTitle)
                val shareIntent = Intent(Intent.ACTION_SENDTO)
                shareIntent.data = Uri.parse("mailto:")
                shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportEmail)))
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, title)
                shareIntent.putExtra(Intent.EXTRA_TEXT, message)
                startActivity(shareIntent)
        }
        binding.buttonTermsOfUse.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.urlTermsOfUse))
                )
            )
        }
        viewModel.getThemeStateLiveData().observe(this) { themeState ->
            themeSwitcher.isChecked = themeState
        }
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.saveThemeState(checked)
            (applicationContext as App).switchTheme(checked)
        }
    }
}

