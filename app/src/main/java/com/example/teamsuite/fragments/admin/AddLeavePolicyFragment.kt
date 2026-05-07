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
import com.example.teamsuite.data.model.leavepolicy.AddLeavePolicyRequest
import com.example.teamsuite.data.model.leavepolicy.AddLeavePolicyWrapper
import com.example.teamsuite.databinding.FragmentAddLeavePolicyBinding
import com.example.teamsuite.repository.department.GetDepartmentRepository
import com.example.teamsuite.repository.leavepolicy.AddLeavePolicyRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.department.GetDepartmentViewModel
import com.example.teamsuite.viewmodel.leavepolicy.AddLeavePolicyViewModel
import com.example.teamsuite.viewmodelfactory.department.GetDepartmentViewModelFactory
import com.example.teamsuite.viewmodelfactory.leavepolicy.AddLeavePolicyViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddLeavePolicyFragment : BaseFragment() {
    private var _binding: FragmentAddLeavePolicyBinding? = null
    private val binding get() = _binding!!
    private lateinit var addLeavePolicyViewModel: AddLeavePolicyViewModel
    private var companyid: String? = null
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            companyid = arguments?.getString(Constant.companyid)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddLeavePolicyBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_leave_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAddLeavePolicyViewModel()
        observeAddLeavePolicyResponse()
        binding.btnAddLeavePolicy.setOnClickListener {
            if (validateFields()){
                AddLeavePolicy()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Function for validate the fields
     */
    private fun validateFields(): Boolean{
        if(binding.etLeaveType.text.toString().trim().isEmpty()){
            binding.leaveTypeLayout.error = "Leave type is required."
            return false
        }
        else binding.leaveTypeLayout.error = null

        if(binding.etMaxLeave.text.toString().trim().toIntOrNull() == null){
            binding.maxLeaveLayout.error = "Enter maximum leave per year."
            return false
        }
        else binding.maxLeaveLayout.error = null

        if (binding.etConsecutive.text.toString().trim().toIntOrNull() == null){
            binding.consecutiveLeaveLayout.error = "Enter maximum consecutive leave."
            return false
        }
        else binding.consecutiveLeaveLayout.error = null

        return true
    }

    /*
    Function for add leave policy data into database
     */
    private fun AddLeavePolicy(){
        val request = AddLeavePolicyRequest(
            companyid.toString(),
            binding.etLeaveType.text.toString(),
            Integer.parseInt(binding.etMaxLeave.text.toString()),
            Integer.parseInt(binding.etConsecutive.text.toString())
        )
        val wrapper = AddLeavePolicyWrapper(request)
        addLeavePolicyViewModel.addLeavePolicy(wrapper)
    }

    /*
    Function for SetUp the Add Leave Policy ViewModel
    */
    private fun setupAddLeavePolicyViewModel() {
        addLeavePolicyViewModel = ViewModelProvider(
            this,
            AddLeavePolicyViewModelFactory(AddLeavePolicyRepository())
        )[AddLeavePolicyViewModel::class.java]
    }

    /*
    Function for observe the response of add leave policy
     */
    private fun observeAddLeavePolicyResponse(){
        addLeavePolicyViewModel.addLeavePolicyResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        addLeavePolicyViewModel.error.observe(viewLifecycleOwner){error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        addLeavePolicyViewModel.isLoading.observe(viewLifecycleOwner){
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
         * @return A new instance of fragment AddLeavePolicyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddLeavePolicyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}