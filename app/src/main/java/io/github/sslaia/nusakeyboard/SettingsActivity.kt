package io.github.sslaia.nusakeyboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.activity.enableEdgeToEdge

class SettingsActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvKeyboardLangLabel: TextView
    private lateinit var rgKeyboardLang: RadioGroup
    private lateinit var rbLangNias: RadioButton
    private lateinit var rbLangIndo: RadioButton
    private lateinit var checkDarkMode: CheckBox
    private lateinit var checkWikiMode: CheckBox
    private lateinit var btnEnable: Button
    private lateinit var tvFooter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        val prefs = getSharedPreferences("nusa_prefs", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        
        // Apply Dark Mode before super.onCreate
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        // Initialize Views
        tvTitle = findViewById(R.id.tv_title)
        tvKeyboardLangLabel = findViewById(R.id.tv_keyboard_lang_label)
        rgKeyboardLang = findViewById(R.id.rg_keyboard_lang)
        rbLangNias = findViewById(R.id.rb_lang_nias)
        rbLangIndo = findViewById(R.id.rb_lang_indo)
        checkDarkMode = findViewById(R.id.check_dark_mode)
        checkWikiMode = findViewById(R.id.check_wiki_mode)
        btnEnable = findViewById(R.id.btn_enable_ime)
        tvFooter = findViewById(R.id.tv_footer)

        // Load saved settings
        checkDarkMode.isChecked = isDarkMode
        checkWikiMode.isChecked = prefs.getBoolean("wiki_mode", true)

        val keyboardLang = prefs.getString("keyboard_lang", "nias") ?: "nias"
        if (keyboardLang == "nias") {
            rbLangNias.isChecked = true
        } else {
            rbLangIndo.isChecked = true
        }

        rgKeyboardLang.setOnCheckedChangeListener { _, checkedId ->
            val langVal = if (checkedId == R.id.rb_lang_nias) "nias" else "indo"
            prefs.edit { putString("keyboard_lang", langVal) }
        }

        // Set UI text to Indonesian
        setupLanguageUI()

        checkDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit { putBoolean("dark_mode", isChecked) }
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            // recreate() is often needed to refresh the theme completely
            recreate()
        }

        checkWikiMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit { putBoolean("wiki_mode", isChecked) }
        }

        btnEnable.setOnClickListener {
            startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
        }
    }

    private fun setupLanguageUI() {
        tvTitle.text = "Keyboard Nusa"
        tvKeyboardLangLabel.text = "Bahasa Mengetik:"
        rbLangNias.text = "Bahasa Nias"
        rbLangIndo.text = "Bahasa Indonesia"
        checkDarkMode.text = "Gunakan Mode Gelap"
        checkWikiMode.text = "Aktifkan Mode Wiki"
        btnEnable.text = "Aktifkan Keyboard"
        tvFooter.text = "Atur di Pengaturan Android"
    }
}