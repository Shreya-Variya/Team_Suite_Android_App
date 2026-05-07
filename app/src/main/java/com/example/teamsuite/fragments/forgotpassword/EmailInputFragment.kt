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
import com.example.teamsuite.data.model.forgotpassword.SendCodeRequest
import com.example.teamsuite.databinding.FragmentEmailInputBinding
import com.example.teamsuite.databinding.FragmentLoginBinding
import com.example.teamsuite.repository.forgotpassword.ForgotPasswordRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.forgotpassword.ForgotPasswordViewModel
import com.example.teamsuite.viewmodelfactory.forgotpassword.ForgotPasswordViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class EmailInputFragment : BaseFragment() {
    private var _binding: FragmentEmailInputBinding ?= null
    private val binding get() = _binding!!

    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel
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
        _binding = FragmentEmailInputBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_email_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupForgotPasswordViewModel()
        binding.sendOtpBtn.setOnClickListener {
            if (validateField()){
                forgotPasswordViewModel.sendcode(SendCodeRequest(binding.etEmail.text.toString().trim()))
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
    Function for validate the field
     */
    private fun validateField(): Boolean{
        if (binding.etEmail.text.toString().trim().isEmpty()){
            binding.etLayoutEmail.error = getString(R.string.email_required)
            return false
        }
        else binding.etLayoutEmail.error = null
        return true
    }

    /*
    Function for setup Forgot Password View Model for send verification code
     */
    private fun setupForgotPasswordViewModel(){
        forgotPasswordViewModel = ViewModelProvider(
            requireActivity(),
            ForgotPasswordViewModelFactory(ForgotPasswordRepository())
        )[ForgotPasswordViewModel::class.java]
    }

    /*
     Function for get send verification code response send from backend
     */
    private fun observeForgotPasswordResponse(){
        forgotPasswordViewModel.sendCodeResult.removeObservers(viewLifecycleOwner)
        forgotPasswordViewModel.error.removeObservers(viewLifecycleOwner)

        forgotPasswordViewModel.sendCodeResult.observe(viewLifecycleOwner){ response ->
            if (response == null) return@observe

            if (response.success){
                hideLoader()
                if (response.message.isNotBlank()){
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }

                forgotPasswordViewModel.clearSendCodeResult()
//                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                val fragment = VerifyOtpFragment()
                val bundle = Bundle().apply {
                    putString(Constant.Email, binding.etEmail.text.toString().trim())
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
            if(error != null){
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
         * @return A new instance of fragment EmailInputFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmailInputFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}