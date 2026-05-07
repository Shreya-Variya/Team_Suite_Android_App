package com.example.teamsuite.fragments.employee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.R
import com.example.teamsuite.abstractclass.BaseFragment
import com.example.teamsuite.adapter.EmployeeLeaveAdapter
import com.example.teamsuite.data.model.login.UserData
import com.example.teamsuite.databinding.FragmentEmployeeLeaveBinding
import com.example.teamsuite.repository.leave.GetLeaveReportRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.leave.GetLeaveReportViewModel
import com.example.teamsuite.viewmodelfactory.leave.GetLeaveReportViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EmployeeLeaveFragment : BaseFragment() {
    private var _binding: FragmentEmployeeLeaveBinding?= null
    private val binding get() = _binding!!
    private var userData: UserData? = null
    private lateinit var getLeaveReportViewModel: GetLeaveReportViewModel
    private lateinit var adapter: EmployeeLeaveAdapter
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
        _binding = FragmentEmployeeLeaveBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_employee_leave, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListener()
        setupGetLeaveReportViewModel()
        userData?.let {
            getLeaveReportViewModel.getLeaveReport(it._id)
        }
        observeGetLeaveReportResponse()
        observeGetAllLeaveReportResponse()
        observeGetApprovedLeaveReportResponse()
        observeGetPendingLeaveReportResponse()
        observeGetRejectedLeaveReportResponse()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Function for setup the on click listener
     */
    private fun setupOnClickListener(){
        binding.btnApplyLeave.setOnClickListener {
            val fragment = ApplyLeaveFormFragment()
            val bundle = Bundle().apply {
                putParcelable(Constant.UserData, userData)
            }
            fragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.chipAll.setOnClickListener {
            binding.txtLeaveTitle.text = "Your All Leaves"
            userData?.let {
                getLeaveReportViewModel.getAllLeaveRecords(it._id)
            }
        }
        binding.chipMonth.setOnClickListener {
            userData?.let {
                binding.txtLeaveTitle.text = "Your Monthly Leave Report"
                getLeaveReportViewModel.getLeaveReport(it._id)
            }
        }
        binding.chipApproved.setOnClickListener {
            userData?.let {
                binding.txtLeaveTitle.text = "Your Approved Leaves"
                getLeaveReportViewModel.getApprovedLeaveRecords(it._id)
            }
        }
        binding.chipPending.setOnClickListener {
            userData?.let {
                binding.txtLeaveTitle.text = "Your Pending Leaves"
                getLeaveReportViewModel.getPendingLeaveRecords(it._id)
            }
        }
        binding.chipRejected.setOnClickListener {
            userData?.let {
                binding.txtLeaveTitle.text = "Your Rejected Leaves"
                getLeaveReportViewModel.getRejectedLeaveRecords(it._id)
            }
        }
    }

    /*
    Function for setup the get leave report view model
     */
    private fun setupGetLeaveReportViewModel(){
        getLeaveReportViewModel = ViewModelProvider(
            this,
            GetLeaveReportViewModelFactory(GetLeaveReportRepository())
        )[GetLeaveReportViewModel::class.java]
    }

    /*
    Function for observe the get leave report response
     */
    private fun observeGetLeaveReportResponse(){
        getLeaveReportViewModel.getLeaveReportResult.observe(viewLifecycleOwner){ response ->
            if(response.success){
                hideLoader()
                val list = response.data
                adapter = EmployeeLeaveAdapter(list)
                binding.rvLeaveReport.adapter = adapter
                adapter.updateData(list)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        getLeaveReportViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        getLeaveReportViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the get all leave report response
     */
    private fun observeGetAllLeaveReportResponse(){
        getLeaveReportViewModel.getAllLeaveReportResult.observe(viewLifecycleOwner){ response ->
            if(response.success){
                hideLoader()
                val list = response.data
                adapter = EmployeeLeaveAdapter(list)
                binding.rvLeaveReport.adapter = adapter
                adapter.updateData(list)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        getLeaveReportViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        getLeaveReportViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the get approved leave report response
     */
    private fun observeGetApprovedLeaveReportResponse(){
        getLeaveReportViewModel.getApprovedLeaveReportResult.observe(viewLifecycleOwner){ response ->
            if(response.success){
                hideLoader()
                val list = response.data
                adapter = EmployeeLeaveAdapter(list)
                binding.rvLeaveReport.adapter = adapter
                adapter.updateData(list)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        getLeaveReportViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        getLeaveReportViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the get Pending leave report response
     */
    private fun observeGetPendingLeaveReportResponse(){
        getLeaveReportViewModel.getPendingLeaveReportResult.observe(viewLifecycleOwner){ response ->
            if(response.success){
                hideLoader()
                val list = response.data
                adapter = EmployeeLeaveAdapter(list)
                binding.rvLeaveReport.adapter = adapter
                adapter.updateData(list)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        getLeaveReportViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        getLeaveReportViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the get rejected leave report response
     */
    private fun observeGetRejectedLeaveReportResponse(){
        getLeaveReportViewModel.getRejectedLeaveReportResult.observe(viewLifecycleOwner){ response ->
            if(response.success){
                hideLoader()
                val list = response.data
                adapter = EmployeeLeaveAdapter(list)
                binding.rvLeaveReport.adapter = adapter
                adapter.updateData(list)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        getLeaveReportViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        getLeaveReportViewModel.isLoading.observe(viewLifecycleOwner){
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
         * @return A new instance of fragment EmployeeLeaveFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmployeeLeaveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}