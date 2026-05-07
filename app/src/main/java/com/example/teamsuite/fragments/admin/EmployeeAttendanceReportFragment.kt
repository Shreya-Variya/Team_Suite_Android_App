package com.example.teamsuite.fragments.admin

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.abstractclass.BaseFragment
import com.example.teamsuite.adapter.EmpAttendanceAdapter
import com.example.teamsuite.adapter.EmployeeAttendanceAdapter
import com.example.teamsuite.data.model.attendance.attendancereport.AttendanceByDateRequest
import com.example.teamsuite.data.model.login.UserData
import com.example.teamsuite.databinding.FragmentEmployeeAttendanceReportBinding
import com.example.teamsuite.repository.attendance.AttendanceByDateRepository
import com.example.teamsuite.repository.attendance.GetEmployeeAttendanceRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.attendance.AttendanceByDateViewModel
import com.example.teamsuite.viewmodel.attendance.GetEmployeeAttendanceViewModel
import com.example.teamsuite.viewmodelfactory.attendance.AttendanceByDateViewModelFactory
import com.example.teamsuite.viewmodelfactory.attendance.GetEmployeeAttendanceViewModelFactory
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EmployeeAttendanceReportFragment : BaseFragment() {
    private var _binding: FragmentEmployeeAttendanceReportBinding? = null
    private val binding get() = _binding!!
    private var userData: UserData? = null
    private lateinit var employeeAttendanceReportViewModel: GetEmployeeAttendanceViewModel
    private lateinit var attendanceByDateViewModel: AttendanceByDateViewModel
    private lateinit var adapter: EmployeeAttendanceAdapter
    private lateinit var empAdapter: EmpAttendanceAdapter
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
        _binding = FragmentEmployeeAttendanceReportBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_employee_attendance_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGetEmployeeAttendanceViewModel()
        setupAttendanceByDateViewModel()
        userData?.let {
            employeeAttendanceReportViewModel.getEmployeeAttendance(it.companyId)
        }
        binding.cardDateFilter.setOnClickListener {
            openDatePicker(binding.cardDateFilter, true)
        }
        observeGetEmployeeAttendance()
        observeAttendanceByDateResponse()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Function for set calender on click to edittext of dob and join date
     */
    private fun openDatePicker(card: MaterialCardView, isDate: Boolean = false) {

        val calendar = Calendar.getInstance()

        val dialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)

                val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                userData?.let {
                    attendanceByDateViewModel.attendanceByDate(it.companyId,
                        AttendanceByDateRequest(sdf.format(selectedCalendar.time)))
                }
                binding.txtSelectedDate.setText(sdf.format(selectedCalendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    /*
    Function for SetUp the Get Employee Attendance ViewModel
    */
    private fun setupGetEmployeeAttendanceViewModel() {
        employeeAttendanceReportViewModel = ViewModelProvider(
            this,
            GetEmployeeAttendanceViewModelFactory(GetEmployeeAttendanceRepository())
        )[GetEmployeeAttendanceViewModel::class.java]
    }

    /*
    Function for setup the attendance by date view model
     */
    private fun setupAttendanceByDateViewModel(){
        attendanceByDateViewModel = ViewModelProvider(
            this,
            AttendanceByDateViewModelFactory(AttendanceByDateRepository())
        )[AttendanceByDateViewModel::class.java]
    }

    /*
    Function for observe the response of get employee attendance report
     */
    private fun observeGetEmployeeAttendance(){
        employeeAttendanceReportViewModel.employeeAttendanceData.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                val data = response.data
                adapter = EmployeeAttendanceAdapter(data)
                binding.rcEmpAttendance.adapter = adapter
                adapter.updateData(data)
            }
            else {
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        employeeAttendanceReportViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        employeeAttendanceReportViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the response of attendance by date response
     */
    private fun observeAttendanceByDateResponse(){
        attendanceByDateViewModel.attendanceData.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                val data = response.data
                binding.txtAttendanceTitle.text = "Employee Attendance Report"
//                binding.txtHeaderEmpId.visibility = View.GONE
//                binding.txtHeaderEmpName.visibility = View.GONE
//                binding.txtHeaderDate.visibility = View.GONE
//                binding.txtHeaderStatus.visibility = View.GONE
                binding.txtHeaderWork.visibility = View.VISIBLE
                binding.txtHeaderBreak.visibility = View.VISIBLE
                empAdapter = EmpAttendanceAdapter(data)
                binding.rcEmpAttendance.adapter = empAdapter
                empAdapter.updateData(data)
            }
            else {
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        attendanceByDateViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        attendanceByDateViewModel.isLoading.observe(viewLifecycleOwner){
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
         * @return A new instance of fragment EmployeeAttendanceReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmployeeAttendanceReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}