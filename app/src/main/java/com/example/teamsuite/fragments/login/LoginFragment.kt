package com.example.teamsuite.fragments.login

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.R
import com.example.teamsuite.abstractclass.BaseFragment
import com.example.teamsuite.databinding.FragmentLoginBinding
import com.example.teamsuite.repository.login.LoginRepository
import com.example.teamsuite.viewmodel.login.LoginViewModel
import com.example.teamsuite.viewmodelfactory.login.LoginViewModelFactory
import com.example.teamsuite.activity.MainActivity
import com.example.teamsuite.data.model.login.UserData
import com.example.teamsuite.data.model.notification.SaveFcmTokenRequest
import com.example.teamsuite.fragments.forgotpassword.EmailInputFragment
import com.example.teamsuite.repository.notification.SaveFcmTokenRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.notification.SaveFcmTokenViewModel
import com.example.teamsuite.viewmodelfactory.notification.SaveFcmTokenViewModelFactory
import com.google.firebase.messaging.FirebaseMessaging

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : BaseFragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel
    private lateinit var saveFcmTokenViewModel: SaveFcmTokenViewModel

    private val sharedPref by lazy {
        requireContext().getSharedPreferences(Constant.PREF_NAME, android.content.Context.MODE_PRIVATE)
    }

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupSaveFcmTokenViewModel()
        autofillLoginDetails()
        observeLoginResult()
        observeSaveFcmTokenResult()
        setupClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
    Function for setup save fcmtoken view model
     */
    private fun setupSaveFcmTokenViewModel(){
        saveFcmTokenViewModel = ViewModelProvider(
            this,
            SaveFcmTokenViewModelFactory(SaveFcmTokenRepository())
            )[SaveFcmTokenViewModel::class.java]
    }

    /*
    Function for autofill login details if user is not new
     */
    private fun autofillLoginDetails(){
        val rememberMe = sharedPref.getBoolean(Constant.KEY_REMEMBER, false)
        val savedEmail = sharedPref.getString(Constant.KEY_EMAIL, "")
        val savedPassword = sharedPref.getString(Constant.KEY_PASSWORD, "")
        binding.etEmail.setText(savedEmail)
        binding.etPassword.setText(savedPassword)
        binding.checkRememberMe.isChecked = rememberMe
    }

    /*
    Function for SetUp the On Click Listener
    */
    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            validateAndLogin()
        }
        binding.txtForgotPassword.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EmailInputFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    /*
    Function for Validate the input fields and call the login api
    */
    private fun validateAndLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        when {
            email.isEmpty() && password.isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    context?.getString(R.string.email_and_password),
                    Toast.LENGTH_SHORT
                ).show()
            }

            email.isEmpty() -> {
                Toast.makeText(requireContext(), context?.getString(R.string.email_required), Toast.LENGTH_SHORT).show()
            }

            !isValidEmail(email) -> {
                Toast.makeText(requireContext(), context?.getString(R.string.valid_email), Toast.LENGTH_SHORT)
                    .show()
            }

            password.isEmpty() -> {
                Toast.makeText(requireContext(), context?.getString(R.string.password_required), Toast.LENGTH_SHORT).show()
            }

            else -> {
                viewModel.login(email, password)
            }
        }
    }

    /*
    Function for validate the email
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /*
    Function for save fcmtoken
     */
    private fun saveFcmToken(token: String, userData: UserData?){
        val request = SaveFcmTokenRequest(
            userData?._id ?: "",
            userData?.category ?: "",
            token
        )
        saveFcmTokenViewModel.saveFcmToken(request)
    }

    /*
    Function for get the response send through backend
     */
    private fun observeLoginResult() {
        viewModel.loginResult.observe(viewLifecycleOwner) { response ->
//            Log.i("SUCCESS", "${response.success}")
//            Log.i("MESSAGE", "Message : ${response.message}")
//            Log.i("USERDATA", "${response.userData}")

            if (response.success) {
                hideLoader()
                (activity as MainActivity).onLoginSuccess(
                    userData = response.userData,
                    rememberMe = binding.checkRememberMe.isChecked,
                    email = binding.etEmail.text.toString(),
                    password = binding.etPassword.text.toString()
                )
                Toast.makeText(
                    requireContext(),
                    context?.getString(R.string.welcome_user, response.userData?.employeeName),
                    Toast.LENGTH_SHORT
                ).show()
//                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//                    if (task.isSuccessful){
//                        val token = task.result
//                        response.userData?.let {
//                            saveFcmToken(token, it)
//                        }
//                    }
//                }
                val sharedPref = requireContext().getSharedPreferences(
                    Constant.PREF_NAME,
                    MODE_PRIVATE
                )

                val token = sharedPref.getString("FCM_TOKEN", null)

                if (token != null) {
                    saveFcmToken(token, response.userData)
                } else {
                    FirebaseMessaging.getInstance().token.addOnSuccessListener { newToken ->
                        sharedPref.edit().putString("FCM_TOKEN", newToken).apply()
                        saveFcmToken(newToken, response.userData)
                    }
                }
            } else {
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
//            Log.i("ERROR", error)
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        viewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the response of save fcmtoken view model
     */
    private fun observeSaveFcmTokenResult(){
        saveFcmTokenViewModel.saveFcmTokenResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        saveFcmTokenViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        saveFcmTokenViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }
}