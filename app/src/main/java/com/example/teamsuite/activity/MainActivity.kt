package com.example.teamsuite.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.fragments.MainScreenFragment
import com.example.teamsuite.R
import com.example.teamsuite.data.model.login.UserData
import com.example.teamsuite.fragments.login.LoginFragment
import com.example.teamsuite.repository.login.LoginRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.login.LoginViewModel
import com.example.teamsuite.viewmodelfactory.login.LoginViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    /*
     Create a session
     */
    private val sharedPref by lazy {
        getSharedPreferences(Constant.PREF_NAME, MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }

        /*
        SetUp the Login ViewModel
         */
        setupViewModel()

        if (savedInstanceState == null) {
            decideStartScreen()
        }

        addBackListener()
    }

    /*
    Function for create notification channel
     */
    private fun createNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                "leave_channel",
                "Leave Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for leave request notifications"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /*
    Function for managing backstack event
     */
    private fun addBackListener() {
        onBackPressedDispatcher.addCallback(this) {

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

            if (navHostFragment != null) {
                val childFragmentManager = navHostFragment.childFragmentManager

                // 1. Handle nested fragment back stack
                if (childFragmentManager.backStackEntryCount > 0) {
                    childFragmentManager.popBackStack()
                    return@addCallback
                }
            }

            // 2. Handle main fragment back stack
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
                return@addCallback
            }

            // 3. Check if current fragment is Dashboard
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

            if (currentFragment is MainScreenFragment) {
                finish() // Exit activity
            } else {
                super.onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    /*
    Function for SetUp the Login ViewModel
     */
    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(LoginRepository())
        )[LoginViewModel::class.java]
    }

    /*
    Function for decide the starting screen
     */
    private fun decideStartScreen() {
        val isLoggedIn = sharedPref.getBoolean(Constant.KEY_LOGGED_IN, false)
        val savedEmail = sharedPref.getString(Constant.KEY_EMAIL, null)
        val savedPassword = sharedPref.getString(Constant.KEY_PASSWORD, null)

        if (isLoggedIn && !savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            autoLogin(savedEmail, savedPassword)
        } else {
            openLogin()
        }
    }

    /*
    Function for auto login the user and redirect to dashboard
     */
    private fun autoLogin(email: String, password: String) {
        viewModel.login(email, password)
        viewModel.loginResult.observe(this) { response ->
            if (response.success && response.userData != null) {
                openDashboard(response.userData)
            } else {
//                clearSession()
                openLogin()
            }
        }
        viewModel.error.observe(this) { error ->
//            Log.i("ERROR", error)
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
//            clearSession()
            openLogin()
        }
    }

    /*
    Function for open the login screen
     */
    private fun openLogin() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }

    /*
    Function for open the main dashboard screen
     */
    private fun openDashboard(userData: UserData) {
        val fragment = MainScreenFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constant.UserData, userData)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    /*
    Function for clear the session
     */
    private fun clearSession() {
        sharedPref.edit { clear() }
    }

    /*
    Function for store the data in session on successful login
     */
    fun onLoginSuccess(userData: UserData?, rememberMe: Boolean, email: String, password: String) {
        if (rememberMe) {
            sharedPref.edit {
                putBoolean(Constant.KEY_REMEMBER, true)
                putBoolean(Constant.KEY_LOGGED_IN, true)
                putString(Constant.KEY_EMAIL, email)
                putString(Constant.KEY_PASSWORD, password)
                putString(Constant.KEY_USER_ID, userData?._id)
                putString(Constant.KEY_CATEGORY, userData?.category)
            }
        } else {
//            clearSession()
        }
        openDashboard(userData!!)
    }


}