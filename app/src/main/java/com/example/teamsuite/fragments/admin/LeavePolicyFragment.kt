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
import com.example.teamsuite.adapter.LeavePolicyAdapter
import com.example.teamsuite.databinding.FragmentAddLeavePolicyBinding
import com.example.teamsuite.databinding.FragmentLeavePolicyBinding
import com.example.teamsuite.repository.employee.DeleteEmployeeRepository
import com.example.teamsuite.repository.leavepolicy.GetLeavePolicyRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.employee.DeleteEmployeeViewModel
import com.example.teamsuite.viewmodel.leavepolicy.GetLeavePolicyViewModel
import com.example.teamsuite.viewmodelfactory.employee.DeleteEmployeeViewModelFactory
import com.example.teamsuite.viewmodelfactory.leavepolicy.GetLeavePolicyViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LeavePolicyFragment : BaseFragment() {
    private var _binding: FragmentLeavePolicyBinding? = null
    private val binding get() = _binding!!
    private lateinit var getLeavePolicyViewModel: GetLeavePolicyViewModel
    private lateinit var adapter: LeavePolicyAdapter
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
        _binding = FragmentLeavePolicyBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_leave_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGetLeavePolicyViewModel()
        companyid?.let {
            getLeavePolicyViewModel.getLeavePolicy(it)
        }
        observeGetLeavePolicyResponse()
        binding.btnAddLeavePolicy.setOnClickListener {
            val fragment = AddLeavePolicyFragment();
            val bundle = Bundle().apply {
                putString(Constant.companyid, companyid)
            }
            fragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
    Function for get all leave policies of the company
     */
    private fun observeGetLeavePolicyResponse(){
        getLeavePolicyViewModel.leavePolicyData.observe(viewLifecycleOwner){ response ->
            if(response.success){
                hideLoader()
                val policies = response.data
                adapter = LeavePolicyAdapter(policies)
                binding.rvLeavePolicy.adapter = adapter
                adapter.updateData(policies)
            }
            else {
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LeavePolicyFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LeavePolicyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}