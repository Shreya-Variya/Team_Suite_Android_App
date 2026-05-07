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
import com.example.teamsuite.data.model.forgotpassword.ResetPasswordRequest
import com.example.teamsuite.databinding.FragmentLoginBinding
import com.example.teamsuite.databinding.FragmentResetPasswordBinding
import com.example.teamsuite.fragments.login.LoginFragment
import com.example.teamsuite.repository.forgotpassword.ForgotPasswordRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.forgotpassword.ForgotPasswordViewModel
import com.example.teamsuite.viewmodelfactory.forgotpassword.ForgotPasswordViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ResetPasswordFragment : BaseFragment() {
    private var _binding: FragmentResetPasswordBinding ?= null
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
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupForgotPasswordViewModel()
        binding.btnResetPassword.setOnClickListener {
            if (validateInputFields()){
                email?.let {
                    forgotPasswordViewModel.resetPassword(ResetPasswordRequest(it, binding.etConfirmPassword.text.toString().trim()))
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
    Function for validate the input fields
     */
    private fun validateInputFields(): Boolean{
        if (binding.etNewPassword.text.toString().trim().isEmpty()){
            binding.etLayoutNewPassword.error = getString(R.string.new_password_required)
            return false
        }
        else binding.etLayoutNewPassword.error = null

        if (binding.etConfirmPassword.text.toString().trim().isEmpty()){
            binding.etLayoutConfirmPassword.error = getString(R.string.confirm_password_required)
            return false
        }
        else binding.etLayoutConfirmPassword.error = null

        if (binding.etNewPassword.text.toString().trim() != binding.etConfirmPassword.text.toString().trim()){
            binding.etLayoutConfirmPassword.error = getString(R.string.password_validation)
            return false
        }
        else binding.etLayoutConfirmPassword.error = null

        return true
    }

    /*
    Function for setup Forgot Password View Model for reset the password
     */
    private fun setupForgotPasswordViewModel(){
        forgotPasswordViewModel = ViewModelProvider(
            requireActivity(),
            ForgotPasswordViewModelFactory(ForgotPasswordRepository())
        )[ForgotPasswordViewModel::class.java]
    }

    /*
     Function for get reset password Response send from backend
     */
    private fun observeForgotPasswordResponse(){
        forgotPasswordViewModel.resetPasswordResult.removeObservers(viewLifecycleOwner)
        forgotPasswordViewModel.error.removeObservers(viewLifecycleOwner)

        forgotPasswordViewModel.resetPasswordResult.observe(viewLifecycleOwner){ response ->
            if (response == null) return@observe

            if (response.success){
                hideLoader()
                if (response.message.isNotBlank()){
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }

                forgotPasswordViewModel.clearResetPasswordResult()
//                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                val fragment = LoginFragment()
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
         * @return A new instance of fragment ResetPasswordFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResetPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}