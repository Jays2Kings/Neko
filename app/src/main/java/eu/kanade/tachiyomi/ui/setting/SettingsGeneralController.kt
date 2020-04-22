package eu.kanade.tachiyomi.ui.setting

import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import androidx.preference.PreferenceScreen
import eu.kanade.tachiyomi.BuildConfig
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.preference.getOrDefault
import eu.kanade.tachiyomi.data.updater.UpdaterJob
import eu.kanade.tachiyomi.ui.security.SecureActivityDelegate
import eu.kanade.tachiyomi.util.system.ThemeUtil
import eu.kanade.tachiyomi.widget.preference.IntListMatPreference
import eu.kanade.tachiyomi.data.preference.PreferenceKeys as Keys

class SettingsGeneralController : SettingsController() {

    private val isUpdaterEnabled = BuildConfig.INCLUDE_UPDATER

    override fun setupPreferenceScreen(screen: PreferenceScreen) = with(screen) {
        titleRes = R.string.general

        intListPreference(activity) {
            key = Keys.theme
            titleRes = R.string.app_theme
            entriesRes = arrayOf(
                R.string.system_default,
                R.string.white_theme,
                R.string.dark_black
            )
            entryValues = listOf(1, 2, 3)
            defaultValue = 1

            onChange { newValue ->
                AppCompatDelegate.setDefaultNightMode(ThemeUtil.nightMode(newValue as Int))
                true
            }
        }

        listPreference(activity) {
            key = Keys.dateFormat
            titleRes = R.string.date_format
            entryValues = listOf("", "MM/dd/yy", "dd/MM/yy", "yyyy-MM-dd")
            entries = entryValues.map { value ->
                if (value == "") {
                    context.getString(R.string.system_default)
                } else {
                    value
                }
            }
            defaultValue = ""
            summary = "%s"
        }

        switchPreference {
            key = Keys.automaticUpdates
            titleRes = R.string.check_for_updates
            summaryRes = R.string.auto_check_for_app_versions
            defaultValue = true

            if (isUpdaterEnabled) {
                onChange { newValue ->
                    val checked = newValue as Boolean
                    if (checked) {
                        UpdaterJob.setupTask()
                    } else {
                        UpdaterJob.cancelTask()
                    }
                    true
                }
            } else {
                isVisible = false
            }
        }

        preferenceCategory {
            titleRes = R.string.security

            val biometricManager = BiometricManager.from(context)
            if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
                var preference: IntListMatPreference? = null
                switchPreference {
                    key = Keys.useBiometrics
                    titleRes = R.string.lock_with_biometrics
                    defaultValue = false

                    onChange {
                        preference?.isVisible = it as Boolean
                        true
                    }
                }
                preference = intListPreference(activity) {
                    key = Keys.lockAfter
                    titleRes = R.string.lock_when_idle
                    isVisible = preferences.useBiometrics().getOrDefault()
                    val values = listOf(0, 2, 5, 10, 20, 30, 60, 90, 120, -1)
                    entries = values.mapNotNull {
                        when (it) {
                            0 -> context.getString(R.string.always)
                            -1 -> context.getString(R.string.never)
                            else -> resources?.getQuantityString(
                                R.plurals.after_minutes, it.toInt(), it
                            )
                        }
                    }
                    entryValues = values
                    defaultValue = 0
                }
            }

            switchPreference {
                key = Keys.secureScreen
                titleRes = R.string.secure_screen
                summaryRes = R.string.hide_tachi_from_recents
                defaultValue = false

                onChange {
                    it as Boolean
                    SecureActivityDelegate.setSecure(activity, it)
                    true
                }
            }
        }
    }
}
