package com.example.teamsuite.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.R
import com.example.teamsuite.data.model.changepassword.ChangePasswordRequest
import com.example.teamsuite.data.model.login.UserData
import com.example.teamsuite.databinding.FragmentAdminProfileBinding
import com.example.teamsuite.repository.changepassword.ChangePasswordRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.changepassword.ChangePasswordViewModel
import com.example.teamsuite.viewmodelfactory.changepassword.ChangePasswordViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import androidx.core.content.edit
import com.example.teamsuite.abstractclass.BaseFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdminProfileFragment : BaseFragment() {
    private var _binding: FragmentAdminProfileBinding? = null
    private val binding get() = _binding!!
    private var userData: UserData? = null
    private var changePasswordDialog: AlertDialog? = null
    private var latestNewPassword: String? = null
    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
            userData = args.getParcelable(Constant.UserData)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminProfileBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_admin_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAllFields()
        setupChangePasswordViewModel()
        binding.btnMore.setOnClickListener { view ->
            setupPopupMenu(view)
        }
        observeChangePasswordResponse()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Function for setup Change Password View Model
     */
    private fun setupChangePasswordViewModel(){
        changePasswordViewModel = ViewModelProvider(
            this,
            ChangePasswordViewModelFactory(ChangePasswordRepository())
        )[ChangePasswordViewModel::class.java]
    }

    /*
    Function for observe the response send from change password api
     */
    private fun observeChangePasswordResponse(){
        changePasswordViewModel.changePasswordResult.observe(viewLifecycleOwner){ response ->
            if(response != null){
                if (response.success){
                    hideLoader()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    latestNewPassword?.let { savePasswordToPrefs(it) }
                    latestNewPassword = null
                    changePasswordDialog?.dismiss()
                }
                else{
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    changePasswordDialog?.getButton(AlertDialog.BUTTON_POSITIVE)
                        ?.isEnabled = true
                }
            }
        }
        changePasswordViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        changePasswordViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for update new password into shared preference
     */
    private fun savePasswordToPrefs(newPassword: String){
        val prefs = requireContext().getSharedPreferences(Constant.PREF_NAME, android.content.Context.MODE_PRIVATE)
        prefs.edit { putString(Constant.KEY_PASSWORD, newPassword) }
    }

    /*
    Function for setup the all fields
     */
    private fun setupAllFields(){
        userData?.let {
            val name = it.employeeName
            val initials = name
                .trim()
                .split("\\s+".toRegex())
                .take(2)
                .mapNotNull { it.firstOrNull()?.toString() }
                .joinToString("")
                .uppercase()
            binding.txtInitials.setText(initials.uppercase())
            binding.txtName.setText(name)
            binding.txtJobRole.setText(it.jobRole.role)
            binding.txtCompanyName.setText(it.companyName)
            binding.txtEmployeeId.setText(it.employeeId)
            binding.txtJoining.setText(formatDate(it.joinDate))
            binding.txtname.setText(name)
            binding.txtDOB.setText(formatDate(it.dob))
            binding.txtGender.setText(it.gender)
            binding.txtMarried.setText(if(it.maritalStatus) Constant.Married else Constant.Unmarried)
            binding.txtEmail.setText(it.email)
            binding.txtMobileNo.setText(it.mobileNo)
            binding.txtStreet.setText(it.address.street)
            binding.txtCity.setText(it.address.city)
            binding.txtState.setText(it.address.state)
            binding.txtDegree.setText(it.education)
            binding.txtExp.setText(it.experience.toString() + "year")
            binding.txtJobrole.setText(it.jobRole.role)
            binding.txtDept.setText(it.department.name)
            binding.txtCategory.setText(it.category)
        }
    }

    /*
    Function for setup popup for change password
     */
    private fun setupPopupMenu(view: View){
        val popup = PopupMenu(requireContext(), view)
        popup.menu.add("Change Password")

        popup.setOnMenuItemClickListener { item ->
            when(item.title){
                "Change Password" -> {
                    showChangePasswordDialog()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    /*
    Function for setup dialogbox for change password
     */
    private fun showChangePasswordDialog(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null)
        val oldPassword = dialogView.findViewById<TextInputEditText>(R.id.etOldPassword)
        val newPassword = dialogView.findViewById<TextInputEditText>(R.id.etNewPassword)
        val confirmPassword = dialogView.findViewById<TextInputEditText>(R.id.etConfirmPassword)

        changePasswordDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Change Password")
            .setView(dialogView)
            .setPositiveButton("Change", null)
            .setNegativeButton("Cancel"){d,_ ->
                d.dismiss()
            }
            .create()
        changePasswordDialog?.show()

        changePasswordDialog?.getButton(AlertDialog.BUTTON_POSITIVE)
            ?.setTextColor(requireContext().getColor(R.color.colorPrimary))

        changePasswordDialog?.getButton(AlertDialog.BUTTON_NEGATIVE)
            ?.setTextColor(requireContext().getColor(R.color.error_color))

        changePasswordDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            val old = oldPassword.text.toString().trim()
            val new = newPassword.text.toString().trim()
            val confirm = confirmPassword.text.toString().trim()
            if (old.isEmpty() || new.isEmpty() || confirm.isEmpty()){
                Toast.makeText(requireContext(), getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                if (new != confirm){
                    Toast.makeText(requireContext(), getString(R.string.password_not_match), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else{
                    changePasswordDialog?.getButton(AlertDialog.BUTTON_POSITIVE)
                        ?.isEnabled = false
                    latestNewPassword = new
                    userData?.let {
                        changePasswordViewModel.changePassword(ChangePasswordRequest(it.email, old, new))
                    }
                }
            }
        }
    }

    /*
    Function for format the date send from backend
     */
    private fun formatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return ""

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val date = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateString.substring(0, 10) // fallback
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdminProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}