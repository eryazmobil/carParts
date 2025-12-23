package eryaz.software.carParts.ui.base

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import eryaz.software.carParts.data.persistence.SessionManager
import eryaz.software.carParts.util.LocaleWrapper
import eryaz.software.carParts.util.extensions.hideSoftKeyboard
import java.util.Locale

abstract class BaseActivity : AppCompatActivity() {

    override fun onSupportNavigateUp(): Boolean {
       hideSoftKeyboard()
        onBackPressed()
        return true
    }

    override fun attachBaseContext(newBase: Context) {
        val localeToSwitchTo = Locale(SessionManager.language.name.lowercase())
        val localeUpdatedContext: ContextWrapper =
            LocaleWrapper.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }

    abstract fun getContentView(): View
}