package com.example.teamsuite.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.R
import com.example.teamsuite.abstractclass.BaseFragment
import com.example.teamsuite.adapter.AttendanceAdapter
import com.example.teamsuite.data.model.login.UserData
import com.example.teamsuite.databinding.FragmentAdminAttendanceBinding
import com.example.teamsuite.fragments.employee.EmployeeAttendanceFragment
import com.example.teamsuite.repository.attendance.AttendanceReportRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.attendance.AttendanceReportViewModel
import com.example.teamsuite.viewmodelfactory.attendance.AttendanceReportViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdminAttendanceFragment : BaseFragment() {
    private var _binding: FragmentAdminAttendanceBinding? = null
    private val binding get() = _binding!!
    private var userData: UserData? = null
    private lateinit var attendanceReportViewModel: AttendanceReportViewModel
    private lateinit var adapter: AttendanceAdapter
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
        _binding = FragmentAdminAttendanceBinding.inflate(inflater, container,false)
        return  binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_admin_attendance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGetAttendanceReportViewModel()
        userData?.let {
            attendanceReportViewModel.getAttendanceReport(it._id)
        }
        observeGetAttendanceReport()
        binding.cardEmployeeAttendance.setOnClickListener {
            navigateToFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Function for navigate to fragment
     */
    private fun navigateToFragment(){
        val fragment = EmployeeAttendanceReportFragment()
        val bundle = Bundle().apply {
            putParcelable(Constant.UserData, userData)
        }
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    /*
    Function for SetUp the Get Attendance Report ViewModel
    */
    private fun setupGetAttendanceReportViewModel() {
        attendanceReportViewModel = ViewModelProvider(
            this,
            AttendanceReportViewModelFactory(AttendanceReportRepository())
        )[AttendanceReportViewModel::class.java]
    }

    /*
    Function for observe the response of get attendance report
     */
    private fun observeGetAttendanceReport(){
        attendanceReportViewModel.attendanceData.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                val data = response.data
                adapter = AttendanceAdapter(data)
                binding.recyclerAttendance.adapter = adapter
                adapter.updateData(data)
            }
            else {
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        attendanceReportViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        attendanceReportViewModel.isLoading.observe(viewLifecycleOwner){
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
         * @return A new instance of fragment AdminAttendanceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminAttendanceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}