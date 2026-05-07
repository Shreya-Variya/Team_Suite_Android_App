package com.example.teamsuite.fragments.admin

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.R
import com.example.teamsuite.abstractclass.BaseFragment
import com.example.teamsuite.adapter.AdminLeaveAdapter
import com.example.teamsuite.data.model.leave.LeaveByDateRequest
import com.example.teamsuite.data.model.login.UserData
import com.example.teamsuite.databinding.FragmentAdminLeaveBinding
import com.example.teamsuite.repository.leave.GetAllEmployeeLeaveReportRepository
import com.example.teamsuite.serviceclass.LeaveActionManager
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.leave.GetAllEmployeeLeaveReportViewModel
import com.example.teamsuite.viewmodelfactory.leave.GetAllEmployeeLeaveReportViewModelFactory
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdminLeaveFragment : BaseFragment() {
    private var _binding: FragmentAdminLeaveBinding?= null
    private val binding get() = _binding!!
    private var userData: UserData? = null
    private val leaveActionManager = LeaveActionManager()
    private lateinit var getAllEmployeeLeaveReportViewModel: GetAllEmployeeLeaveReportViewModel
    private lateinit var adapter: AdminLeaveAdapter
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
        _binding = FragmentAdminLeaveBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_admin_leave, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGetAllEmployeeLeaveReportViewModel()
        userData?.let {
            getAllEmployeeLeaveReportViewModel.getEmployeeLeaveReport(it.companyId)
        }
        setOnClickListeners()
        observeCommonStates()
        observeGetAllEmployeeLeaveReportResponse()
        observeAllLeaveRecordResponse()
        observeApprovedLeaveRecordResponse()
        observePendingLeaveRecordResponse()
        observeRejectedLeaveRecordResponse()
        observeLeaveByDateResponse()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Function for set on click listeners
     */
    private fun setOnClickListeners(){
        binding.chipAll.setOnClickListener {
            userData?.let {
                getAllEmployeeLeaveReportViewModel.getAllLeaveRecord(it.companyId)
            }
        }
        binding.chipMonth.setOnClickListener {
            userData?.let {
                getAllEmployeeLeaveReportViewModel.getEmployeeLeaveReport(it.companyId)
            }
        }
        binding.chipApproved.setOnClickListener {
            userData?.let {
                getAllEmployeeLeaveReportViewModel.getApprovedLeaveRecord(it.companyId)
            }
        }
        binding.chipPending.setOnClickListener {
            userData?.let {
                getAllEmployeeLeaveReportViewModel.getPendingLeaveRecord(it.companyId)
            }
        }
        binding.chipRejected.setOnClickListener {
            userData?.let {
                getAllEmployeeLeaveReportViewModel.getRejectedLeaveRecord(it.companyId)
            }
        }
        binding.chipDate.setOnClickListener {
            openDatePicker(binding.chipDate, true)
        }
    }

    /*
    Function for set calender on click to edittext of dob and join date
     */
    private fun openDatePicker(chip: Chip, isDate: Boolean = false) {

        val calendar = Calendar.getInstance()

        val dialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)

                val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                Log.i("Date", sdf.format(selectedCalendar.time))
                userData?.let {
                    getAllEmployeeLeaveReportViewModel.getLeaveByDate(it.companyId,
                        LeaveByDateRequest(sdf.format(selectedCalendar.time)))
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        dialog.show()
    }

    /*
    Function for setup get all employee leave report view model
     */
    private fun setupGetAllEmployeeLeaveReportViewModel(){
        getAllEmployeeLeaveReportViewModel = ViewModelProvider(
            this,
            GetAllEmployeeLeaveReportViewModelFactory(GetAllEmployeeLeaveReportRepository())
        )[GetAllEmployeeLeaveReportViewModel::class.java]
    }

    /*
    Function for observe the common stats of all apis
     */
    private fun observeCommonStates(){
        getAllEmployeeLeaveReportViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        getAllEmployeeLeaveReportViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the response of all employee leave report
     */
    private fun observeGetAllEmployeeLeaveReportResponse(){
        getAllEmployeeLeaveReportViewModel.getEmployeeLeaveReportResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                val list = response.data
                adapter = AdminLeaveAdapter(list,
                    onAcceptClick = { leave ->
                        leaveActionManager.callAcceptApi(
                            requireContext(),
                            leave.leaveId,
                            leave.employeeId,
                            onSuccess = {
                                refreshLeaveList()
                            },
                            onError = { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    onRejectClick = { leave ->
                        leaveActionManager.callRejectApi(
                            requireContext(),
                            leave.leaveId,
                            leave.employeeId,
                            onSuccess = {
                                refreshLeaveList()
                            },
                            onError = { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    })
                binding.rvEmployeeLeaveReport.adapter = adapter
                adapter.updateData(list)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
    Function for observe the all employees all leave record report
     */
    private fun observeAllLeaveRecordResponse(){
        getAllEmployeeLeaveReportViewModel.AllLeaveRecordResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                val list = response.data
                adapter = AdminLeaveAdapter(list,
                    onAcceptClick = { leave ->
                        leaveActionManager.callAcceptApi(
                            requireContext(),
                            leave.leaveId,
                            leave.employeeId,
                            onSuccess = {
                                refreshLeaveList()
                            },
                            onError = { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    onRejectClick = { leave ->
                        leaveActionManager.callRejectApi(
                            requireContext(),
                            leave.leaveId,
                            leave.employeeId,
                            onSuccess = {
                                refreshLeaveList()
                            },
                            onError = { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    })
                binding.rvEmployeeLeaveReport.adapter = adapter
                adapter.updateData(list)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
    Function for observe the approved leave record report
     */
    private fun observeApprovedLeaveRecordResponse(){
        getAllEmployeeLeaveReportViewModel.ApprovedLeaveRecordResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                val list = response.data
                adapter = AdminLeaveAdapter(list,
                    onAcceptClick = { leave ->
                        leaveActionManager.callAcceptApi(
                            requireContext(),
                            leave.leaveId,
                            leave.employeeId,
                            onSuccess = {
                                refreshLeaveList()
                            },
                            onError = { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    onRejectClick = { leave ->
                        leaveActionManager.callRejectApi(
                            requireContext(),
                            leave.leaveId,
                            leave.employeeId,
                            onSuccess = {
                                refreshLeaveList()
                            },
                            onError = { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    })
                binding.rvEmployeeLeaveReport.adapter = adapter
                adapter.updateData(list)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
    Function for observe the pending leave record report
     */
    private fun observePendingLeaveRecordResponse(){
        getAllEmployeeLeaveReportViewModel.PendingLeaveRecordResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                val list = response.data
                adapter = AdminLeaveAdapter(list,
                    onAcceptClick = { leave ->
                        leaveActionManager.callAcceptApi(
                            requireContext(),
                            leave.leaveId,
                            leave.employeeId,
                            onSuccess = {
                                refreshLeaveList()
                            },
                            onError = { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    onRejectClick = { leave ->
                        leaveActionManager.callRejectApi(
                            requireContext(),
                            leave.leaveId,
                            leave.employeeId,
                            onSuccess = {
                                refreshLeaveList()
                            },
                            onError = { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    })
                binding.rvEmployeeLeaveReport.adapter = adapter
                adapter.updateData(list)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
    Function for observe the rejected leave record report
     */
    private fun observeRejectedLeaveRecordResponse(){
        getAllEmployeeLeaveReportViewModel.RejectedLeaveRecordResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                val list = response.data
                adapter = AdminLeaveAdapter(list,
                    onAcceptClick = { leave ->
                        leaveActionManager.callAcceptApi(
                            requireContext(),
                            leave.leaveId,
                            leave.employeeId,
                            onSuccess = {
                                refreshLeaveList()
                            },
                            onError = { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    onRejectClick = { leave ->
                        leaveActionManager.callRejectApi(
                            requireContext(),
                            leave.leaveId,
                            leave.employeeId,
                            onSuccess = {
                                refreshLeaveList()
                            },
                            onError = { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    })
                binding.rvEmployeeLeaveReport.adapter = adapter
                adapter.updateData(list)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
    Function for observe the search leave by date response
     */
    private fun observeLeaveByDateResponse(){
        getAllEmployeeLeaveReportViewModel.LeaveByDateResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                val list = response.data
                adapter = AdminLeaveAdapter(list,
                    onAcceptClick = { leave ->
                        leaveActionManager.callAcceptApi(
                            requireContext(),
                            leave.leaveId,
                            leave.employeeId,
                            onSuccess = {
                                refreshLeaveList()
                            },
                            onError = { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    onRejectClick = { leave ->
                        leaveActionManager.callRejectApi(
                            requireContext(),
                            leave.leaveId,
                            leave.employeeId,
                            onSuccess = {
                                refreshLeaveList()
                            },
                            onError = { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    })
                binding.rvEmployeeLeaveReport.adapter = adapter
                adapter.updateData(list)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    /*
    Function for refresh leave report list
     */
    private fun refreshLeaveList(){
        userData?.let {
            getAllEmployeeLeaveReportViewModel.getEmployeeLeaveReport(it.companyId)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdminLeaveFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminLeaveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}