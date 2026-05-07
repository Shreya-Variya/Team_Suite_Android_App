package com.example.teamsuite.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.R
import com.example.teamsuite.abstractclass.BaseFragment
import com.example.teamsuite.data.model.login.UserData
import com.example.teamsuite.databinding.FragmentMainScreenBinding
import com.example.teamsuite.fragments.admin.AdminAttendanceFragment
import com.example.teamsuite.fragments.admin.AdminDashboardFragment
import com.example.teamsuite.fragments.admin.AdminLeaveFragment
import com.example.teamsuite.fragments.admin.AdminProfileFragment
import com.example.teamsuite.fragments.admin.EmployeeSectionFragment
import com.example.teamsuite.fragments.admin.LeavePolicyFragment
import com.example.teamsuite.fragments.admin.SettingsFragment
import com.example.teamsuite.fragments.employee.EmpEmployeeSectionFragment
import com.example.teamsuite.fragments.employee.EmployeeAttendanceFragment
import com.example.teamsuite.fragments.employee.EmployeeDashboardFragment
import com.example.teamsuite.fragments.employee.EmployeeLeaveFragment
import com.example.teamsuite.fragments.employee.EmployeeProfileFragment
import com.example.teamsuite.fragments.login.LoginFragment
import com.example.teamsuite.repository.notification.RemoveFcmTokenRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.notification.RemoveFcmTokenViewModel
import com.example.teamsuite.viewmodelfactory.notification.RemoveFcmTokenViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.messaging.FirebaseMessaging

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainScreenFragment : BaseFragment() {
    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    private var userData: UserData? = null
    private lateinit var removeFcmTokenViewModel: RemoveFcmTokenViewModel
//    private var param1: String? = null
//    private var param2: String? = null

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
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userData?.let {user ->
//            Log.i("USER_DATA", "USER : ${user}")
            loadFragmentForUser(user)
            setupNavigationDrawer(user)
        }
        setupRemoveFcmTokenViewModel()
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        observeRemoveFcmTokenResult()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Function for setup the remove fcm token view model
     */
    private fun setupRemoveFcmTokenViewModel(){
        removeFcmTokenViewModel = ViewModelProvider(
            this,
            RemoveFcmTokenViewModelFactory(RemoveFcmTokenRepository())
        )[RemoveFcmTokenViewModel::class.java]
    }

    /*
    Function for observe the remove fcm token response
     */
    private fun observeRemoveFcmTokenResult(){
        removeFcmTokenViewModel.removeFcmTokenResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                navigateToLoginFragment()
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        removeFcmTokenViewModel.error.observe(viewLifecycleOwner){ error ->
            hideLoader()
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            navigateToLoginFragment()
        }
        removeFcmTokenViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for load the dashboard according to user
     */
    private fun loadFragmentForUser(userData: UserData){
        val fragment = when {
            isAdmin(userData) -> AdminDashboardFragment()
            isEmployee(userData) -> EmployeeDashboardFragment()
            else -> EmployeeDashboardFragment()
        }

        val args = Bundle().apply {
            putParcelable(Constant.UserData,userData)
        }

        fragment.arguments = args

        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    /*
    Function for load the navigation drawer according to user
     */
    private fun setupNavigationDrawer(userData: UserData){
        val menu = when {
            isAdmin(userData) -> R.menu.drawer_menu_admin
            isEmployee(userData) -> R.menu.drawer_menu_employee
            else -> R.menu.drawer_menu_employee
        }

        binding.navigationView.inflateMenu(menu)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            hadleNavigationItemClick(menuItem, userData)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    /*
    Function for handle the event onclick of navigation item
     */
    private fun hadleNavigationItemClick(menuItem: MenuItem, userData: UserData): Boolean{
        return when (menuItem.itemId){
            R.id.dashboardAdmin -> {
                binding.topAppBar.title = context?.getString(R.string.dashboard)
                val fragment = AdminDashboardFragment()
                val bundle = Bundle().apply {
                    putParcelable(Constant.UserData, userData)
                }
                fragment.arguments = bundle
                navigateToFragment(fragment)
                true
            }
            R.id.dashboardEmployee -> {
                binding.topAppBar.title = context?.getString(R.string.dashboard)
                val fragment = EmployeeDashboardFragment()
                val bundle = Bundle().apply {
                    putParcelable(Constant.UserData, userData)
                }
                fragment.arguments = bundle
                navigateToFragment(fragment)
                true
            }
            R.id.profileAdmin -> {
                binding.topAppBar.title = context?.getString(R.string.profile)
                val fragment = AdminProfileFragment()
                val bundle = Bundle().apply {
                    putParcelable(Constant.UserData, userData)
                }
                fragment.arguments = bundle
                navigateToFragment(fragment)
                true
            }
            R.id.profileEmployee -> {
                binding.topAppBar.title = context?.getString(R.string.profile)
                val fragment = EmployeeProfileFragment()
                val bundle = Bundle().apply {
                    putParcelable(Constant.UserData, userData)
                }
                fragment.arguments = bundle
                navigateToFragment(fragment)
                true
            }
            R.id.employeeAdmin -> {
                val fragment = EmployeeSectionFragment()

                if(userData.companyId.isNullOrEmpty()){
                    Toast.makeText(requireContext(),"Company ID not found",Toast.LENGTH_SHORT).show()
                    return true
                }

                val bundle = Bundle().apply {
                    putString(Constant.companyid, userData.companyId)
                }
                fragment.arguments = bundle
                navigateToFragment(fragment)
                binding.topAppBar.title = context?.getString(R.string.employee)
                true
            }
            R.id.employeeEmployee -> {
                val fragment = EmpEmployeeSectionFragment()
                if (userData.companyId.isNullOrEmpty()){
                    Toast.makeText(requireContext(),"Company ID not found",Toast.LENGTH_SHORT).show()
                    return true
                }
                val bundle = Bundle().apply {
                    putString(Constant.companyid, userData.companyId)
                }
                fragment.arguments = bundle
                navigateToFragment(fragment)
                binding.topAppBar.title = context?.getString(R.string.employee)
                true
            }
            R.id.attendanceAdmin -> {
                val fragment = AdminAttendanceFragment()
                val bundle = Bundle().apply {
                    putParcelable(Constant.UserData, userData)
                }
                fragment.arguments = bundle
                navigateToFragment(fragment)
                binding.topAppBar.title = context?.getString(R.string.attendance)
                true
            }
            R.id.attendanceEmployee -> {
                val fragment = EmployeeAttendanceFragment()
                val bundle = Bundle().apply {
                    putParcelable(Constant.UserData, userData)
                }
                fragment.arguments = bundle
                navigateToFragment(fragment)
                binding.topAppBar.title = context?.getString(R.string.attendance)
                true
            }
            R.id.leaveAdmin -> {
                val fragment = AdminLeaveFragment()
                val bundle = Bundle().apply {
                    putParcelable(Constant.UserData, userData)
                }
                fragment.arguments = bundle
                navigateToFragment(fragment)
                binding.topAppBar.title = context?.getString(R.string.leave)
                true
            }
            R.id.leaveEmployee -> {
                val fragment = EmployeeLeaveFragment()
                val bundle = Bundle().apply {
                    putParcelable(Constant.UserData, userData)
                }
                fragment.arguments = bundle
                navigateToFragment(fragment)
                binding.topAppBar.title = context?.getString(R.string.leave)
                true
            }
            R.id.leavePolicy -> {
                val fragment = LeavePolicyFragment()
                val bundle = Bundle().apply {
                    putString(Constant.companyid, userData.companyId)
                }
                fragment.arguments = bundle
                navigateToFragment(fragment)
                binding.topAppBar.title = context?.getString(R.string.leave_policy)
                true
            }
            R.id.settings -> {
                val fragment = SettingsFragment()

                if(userData.companyId.isNullOrEmpty()){
                    Toast.makeText(requireContext(),"Company ID not found",Toast.LENGTH_SHORT).show()
                    return true
                }

                val bundle = Bundle().apply {
                    putString(Constant.companyid, userData.companyId)
                }
                fragment.arguments = bundle
                navigateToFragment(fragment)
                binding.topAppBar.title = context?.getString(R.string.settings)
                true
            }
            R.id.logout -> {
                showLogoutDialog()
                true
            }
            else -> false
        }
    }

    /*
    Function for navigation between fragments
     */
    private fun navigateToFragment(fragment: Fragment){
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(fragment.tag)
            .commit()
    }

    /*
    Function for create the logout dialog box
     */
    private fun showLogoutDialog(){
        val dialog =MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.logout_confirmation_statement))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.logout)){_,_ ->
                performLogout()
            }
            .setNegativeButton(getString(R.string.cancel)){dialog,_ ->
                dialog.dismiss()
            }
            .show()

        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
            .setTextColor(requireContext().getColor(R.color.error_color))

        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(requireContext().getColor(R.color.colorPrimary))
    }

    /*
    Function for perform the logout operation
     */
    private fun performLogout(){
        val sharedPref = requireContext()
            .getSharedPreferences(Constant.PREF_NAME, android.content.Context.MODE_PRIVATE)
        sharedPref.edit {
            putBoolean(Constant.KEY_LOGGED_IN, false)
            remove(Constant.KEY_USER_ID)
            remove(Constant.KEY_CATEGORY)
        }
        FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener { task ->
            if (task.isSuccessful){
//                val token = task.result
                userData?.let {
                    removeFcmTokenViewModel.removeFcmToken(it._id)
                }
            }
            else {
                Log.e("FCM", "Token deletion failed", task.exception)
            }
        }
    }

    /*
    Function for navigate to login fragment
     */
    private fun navigateToLoginFragment(){
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .addToBackStack(null)
            .commit()
    }

    /*
    Function for check the logged in user is admin
     */
    private fun isAdmin(userData: UserData): Boolean{
        return userData.category.contains(Constant.admin)
    }

    /*
    Function for check the logged in user is employee
     */
    private fun isEmployee(userData: UserData): Boolean{
        return userData.category.contains(Constant.employee)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainScreenFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}