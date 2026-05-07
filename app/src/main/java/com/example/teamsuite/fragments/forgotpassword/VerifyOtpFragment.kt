package com.example.teamsuite.fragments.forgotpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.R
import com.example.teamsuite.abstractclass.BaseFragment
import com.example.teamsuite.data.model.forgotpassword.VerifyCodeRequest
import com.example.teamsuite.databinding.FragmentLoginBinding
import com.example.teamsuite.databinding.FragmentVerifyOtpBinding
import com.example.teamsuite.repository.forgotpassword.ForgotPasswordRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.forgotpassword.ForgotPasswordViewModel
import com.example.teamsuite.viewmodelfactory.forgotpassword.ForgotPasswordViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VerifyOtpFragment : BaseFragment() {
    private var _binding: FragmentVerifyOtpBinding ?= null
    private val binding get() = _binding!!
    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel
    private var email: String ?= null
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
            email = arguments?.getString(Constant.Email)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerifyOtpBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_verify_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupForgotPasswordViewModel()
        setupOtpInputs()
        binding.verifyOtpBtn.setOnClickListener {
            if (validateField()){
                email?.let {
                    forgotPasswordViewModel.verifyCode(VerifyCodeRequest(it, mergeDigits()))
                }
            }
        }
        observeForgotPasswordResponse()
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Function for validate the otp
     */
    private fun validateField(): Boolean{
        val digit1 = binding.etOtp1.text.toString().trim()
        val digit2 = binding.etOtp2.text.toString().trim()
        val digit3 = binding.etOtp3.text.toString().trim()
        val digit4 = binding.etOtp4.text.toString().trim()
        val digit5 = binding.etOtp5.text.toString().trim()
        val digit6 = binding.etOtp6.text.toString().trim()
        if (digit1.isEmpty() || digit2.isEmpty() || digit3.isEmpty() || digit4.isEmpty() || digit5.isEmpty() || digit6.isEmpty()){
            Toast.makeText(requireContext(), "Enter all digits.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /*
    Function for merge the digits
     */
    private fun mergeDigits(): String{
        val digit1 = binding.etOtp1.text.toString().trim()
        val digit2 = binding.etOtp2.text.toString().trim()
        val digit3 = binding.etOtp3.text.toString().trim()
        val digit4 = binding.etOtp4.text.toString().trim()
        val digit5 = binding.etOtp5.text.toString().trim()
        val digit6 = binding.etOtp6.text.toString().trim()
        val otp = digit1 + digit2 + digit3 + digit4 + digit5 + digit6;
        return otp
    }

    /*
    Function for automatic go to next edittextbox after add a one digit
     */
    private fun setupOtpInputs() {
        val editTexts = listOf(
            binding.etOtp1,
            binding.etOtp2,
            binding.etOtp3,
            binding.etOtp4,
            binding.etOtp5,
            binding.etOtp6
        )

        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        if (i < editTexts.size - 1) {
                            editTexts[i + 1].requestFocus()
                        }
                    }
                }
                override fun afterTextChanged(s: android.text.Editable?) {}
            })

            // Handle backspace
            editTexts[i].setOnKeyListener { _, keyCode, event ->
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL &&
                    event.action == android.view.KeyEvent.ACTION_DOWN &&
                    editTexts[i].text.isNullOrEmpty()
                ) {
                    if (i > 0) {
                        editTexts[i - 1].requestFocus()
                    }
                }
                false
            }
        }
    }

    /*
    Function for setup Forgot Password View Model for verify otp
     */
    private fun setupForgotPasswordViewModel(){
        forgotPasswordViewModel = ViewModelProvider(
            requireActivity(),
            ForgotPasswordViewModelFactory(ForgotPasswordRepository())
        )[ForgotPasswordViewModel::class.java]
    }

    /*
     Function for get verify otp Response send from backend
     */
    private fun observeForgotPasswordResponse(){
        forgotPasswordViewModel.verifyCodeResult.removeObservers(viewLifecycleOwner)
        forgotPasswordViewModel.error.removeObservers(viewLifecycleOwner)

        forgotPasswordViewModel.verifyCodeResult.observe(viewLifecycleOwner){ response ->
            if (response == null) return@observe

            if (response.success){
                hideLoader()
                if (response.message.isNotBlank()){
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }

                forgotPasswordViewModel.clearVerifyCodeResult()
//                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                val fragment = ResetPasswordFragment()
                val bundle = Bundle().apply {
                    putString(Constant.Email, email)
                }
                fragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            else{
                if (!response.message.isNullOrBlank()){
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        forgotPasswordViewModel.error.observe(viewLifecycleOwner){error ->
            if (error != null){
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                forgotPasswordViewModel.clearError()
            }
        }
        forgotPasswordViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VerifyOtpFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VerifyOtpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}