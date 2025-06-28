    package com.example.cric

    import android.content.Context
    import android.content.SharedPreferences

    class SessionManager(context: Context) {

        private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

        companion object {
            private const val KEY_LOGGED_IN = "KEY_LOGGED_IN"
            private const val KEY_NAME = "KEY_NAME"
            private const val KEY_EMAIL = "KEY_EMAIL"
        }

        fun saveLoginState(isLoggedIn: Boolean) {
            prefs.edit().putBoolean(KEY_LOGGED_IN, isLoggedIn).apply()
        }

        fun isLoggedIn(): Boolean {
            return prefs.getBoolean(KEY_LOGGED_IN, false)
        }

        fun saveUserDetails(name: String?, email: String?) {
            prefs.edit().apply {
                putString(KEY_NAME, name)
                putString(KEY_EMAIL, email)
                apply()
            }
        }

        fun getUserName(): String {
            return prefs.getString(KEY_NAME, "Admin") ?: "Admin"
        }

        fun getUserEmail(): String {
            return prefs.getString(KEY_EMAIL, "admin@example.com") ?: "admin@example.com"
        }

        fun clearSession() {
            prefs.edit().clear().apply()
        }
    }
