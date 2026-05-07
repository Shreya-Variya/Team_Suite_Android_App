package com.example.teamsuite.fragments.employee

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.abstractclass.BaseFragment
import com.example.teamsuite.data.model.leave.AdminData
import com.example.teamsuite.data.model.leave.ApplyForLeaveRequest
import com.example.teamsuite.data.model.leave.LeaveData
import com.example.teamsuite.data.model.leavepolicy.LeavePolicyData
import com.example.teamsuite.data.model.login.UserData
import com.example.teamsuite.data.model.notification.SendNotificationToAdminRequest
import com.example.teamsuite.databinding.FragmentApplyLeaveFormBinding
import com.example.teamsuite.repository.leave.ApplyForLeaveRepository
import com.example.teamsuite.repository.leavepolicy.GetLeavePolicyRepository
import com.example.teamsuite.repository.notification.SendNotificationToAdminRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.leave.ApplyForLeaveViewModel
import com.example.teamsuite.viewmodel.leavepolicy.GetLeavePolicyViewModel
import com.example.teamsuite.viewmodel.notification.SendNotificationToAdminViewModel
import com.example.teamsuite.viewmodelfactory.leave.ApplyForLeaveViewModelFactory
import com.example.teamsuite.viewmodelfactory.leavepolicy.GetLeavePolicyViewModelFactory
import com.example.teamsuite.viewmodelfactory.notification.SendNotificationToAdminViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ApplyLeaveFormFragment : BaseFragment() {
    private var _binding: FragmentApplyLeaveFormBinding?= null
    private val binding get() = _binding!!
    private var userData: UserData? = null
    private lateinit var getLeavePolicyViewModel: GetLeavePolicyViewModel
    private lateinit var leavePolicyList: List<LeavePolicyData>
    private lateinit var applyForLeaveViewModel: ApplyForLeaveViewModel
    private lateinit var leaveData: LeaveData
    private lateinit var adminData: AdminData
    private var selectedLeaveTypeId: String? = null
    private lateinit var sendNotificationToAdminViewModel: SendNotificationToAdminViewModel
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
        _binding = FragmentApplyLeaveFormBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_apply_leave_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGetLeavePolicyViewModel()
        setupApplyForLeaveViewModel()
        setupSendNotificationToAdminViewModel()
        userData?.let {
            getLeavePolicyViewModel.getLeavePolicy(it.companyId)
        }
        observeGetLeavePolicyResponse()
        observeApplyForLeaveResponse()
        observeSendNotificationToAdminResponse()
        setupOnClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Function for setup the on click listener
     */
    private fun setupOnClickListener(){
        binding.etStartDate.setOnClickListener {
            openDatePicker(binding.etStartDate, true)
        }
        binding.startDateLayout.setEndIconOnClickListener {
            openDatePicker(binding.etStartDate, true)
        }
        binding.etEndDate.setOnClickListener {
            openDatePicker(binding.etEndDate, true)
        }
        binding.endDateLayout.setEndIconOnClickListener{
            openDatePicker(binding.etEndDate, true)
        }
        binding.btnApply.setOnClickListener {
            if(validateTheFields()){
                applyForLeaveAndSendNotification()
            }
        }
    }

    /*
    Function for validate the fields
     */
    private fun validateTheFields(): Boolean{
        if (binding.etLeaveType.text.toString().trim().isEmpty()){
            binding.leaveTypeLayout.error = "Select leave type."
            return false
        }
        else binding.leaveTypeLayout.error = null

        if (binding.etStartDate.text.toString().trim().isEmpty()){
            binding.startDateLayout.error = "Select leave start date."
            return false
        }
        else binding.startDateLayout.error = null

        if (binding.etEndDate.text.toString().trim().isEmpty()){
            binding.endDateLayout.error = "Select leave end date."
            return false
        }
        else binding.endDateLayout.error = null

        if (binding.etReason.text.toString().trim().isEmpty()){
            binding.reasonLayout.error = "Enter reason for taking leave."
            return false
        }
        else binding.reasonLayout.error = null

        return true
    }

    /*
    Function that apply for leave and send notification to admin
     */
    private fun applyForLeaveAndSendNotification(){
        val reqest = ApplyForLeaveRequest(
            userData?._id ?: "",
            leavePolicyId = selectedLeaveTypeId ?: "",
            binding.etStartDate.text.toString().trim(),
            binding.etEndDate.text.toString().trim(),
            binding.etReason.text.toString().trim()
        )
        applyForLeaveViewModel.applyForLeave(reqest)
    }

    /*
    Function for SetUp the Get Leave Policy ViewModel
    */
    private fun setupGetLeavePolicyViewModel() {
        getLeavePolicyViewModel = ViewModelProvider(
            this,
            GetLeavePolicyViewModelFactory(GetLeavePolicyRepository())
        )[GetLeavePolicyViewModel::class.java]
    }

    /*
    Function for setup apply for leave view model
     */
    private fun setupApplyForLeaveViewModel(){
        applyForLeaveViewModel = ViewModelProvider(
            this,
            ApplyForLeaveViewModelFactory(ApplyForLeaveRepository())
        )[ApplyForLeaveViewModel::class.java]
    }

    /*
    Function for setup send notification to admin view model
     */
    private fun setupSendNotificationToAdminViewModel(){
        sendNotificationToAdminViewModel = ViewModelProvider(
            this,
            SendNotificationToAdminViewModelFactory(SendNotificationToAdminRepository())
        )[SendNotificationToAdminViewModel::class.java]
    }

    /*
    Function for get all leave policies of the company
     */
    private fun observeGetLeavePolicyResponse(){
        getLeavePolicyViewModel.leavePolicyData.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                leavePolicyList = response.data
                setupLeavePolicyDropdown(leavePolicyList)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        getLeavePolicyViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        getLeavePolicyViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the response of apply for leave request
     */
    private fun observeApplyForLeaveResponse(){
        applyForLeaveViewModel.applyForLeaveResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                leaveData = response.leavedata
                adminData = response.admindata
                parentFragmentManager.popBackStack()
                sendNotificationToAdmin()
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        applyForLeaveViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        applyForLeaveViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the response of send notification to admin
     */
    private fun observeSendNotificationToAdminResponse(){
        sendNotificationToAdminViewModel.sendNotificationResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        sendNotificationToAdminViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    /*
    Function for call the send notification to admin api
     */
    private fun sendNotificationToAdmin(){
        val empName = userData?.employeeName
        val request = SendNotificationToAdminRequest(
            adminData._id,
            leaveData._id ,
            leaveData.employeeId,
            "New Leave Request",
            "$empName applied leave from ${binding.etStartDate.text} to ${binding.etEndDate.text} because of ${binding.etReason.text}",
            "leave_request"
        )
        sendNotificationToAdminViewModel.sendNotificationToAdmin(request)
    }

    /*
    Function for setup department dropdown
     */
    private fun setupLeavePolicyDropdown(leavePolicyList: List<LeavePolicyData>){
        val list = leavePolicyList.map { it.leaveType }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            list
        )
        binding.etLeaveType.setAdapter(adapter)

        binding.etLeaveType.setOnItemClickListener { parent, view, position, id ->
            val selectedLeaveType = leavePolicyList[position]
            selectedLeaveTypeId = selectedLeaveType._id
        }
    }

    /*
    Function for set calender on click to edittext of dob and join date
     */
    private fun openDatePicker(editText: EditText, isDate: Boolean = false) {

        val calendar = Calendar.getInstance()

        val dialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)

                val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                editText.setText(sdf.format(selectedCalendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

//        if (isDate) {
//            dialog.datePicker.maxDate = System.currentTimeMillis()
//        }

        dialog.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ApplyLeaveFormFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ApplyLeaveFormFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}