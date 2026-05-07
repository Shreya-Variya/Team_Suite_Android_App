package com.example.teamsuite.fragments.employee

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.R
import com.example.teamsuite.abstractclass.BaseFragment
import com.example.teamsuite.adapter.EmpEmployeeCardAdapter
import com.example.teamsuite.adapter.EmployeeCardAdapter
import com.example.teamsuite.databinding.FragmentEmpEmployeeSectionBinding
import com.example.teamsuite.fragments.admin.EditEmployeeFragment
import com.example.teamsuite.repository.employee.GetAllEmployeesRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.employee.GetAllEmployeeViewModel
import com.example.teamsuite.viewmodelfactory.employee.GetAllEmployeeViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EmpEmployeeSectionFragment : BaseFragment() {
    private var _binding: FragmentEmpEmployeeSectionBinding ?= null
    private val binding get() = _binding!!
    private lateinit var getAllEmployeeViewModel: GetAllEmployeeViewModel
    private lateinit var adapter: EmpEmployeeCardAdapter
    private var companyid: String? = null
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
            companyid = arguments?.getString(Constant.companyid)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmpEmployeeSectionBinding.inflate(inflater,container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_emp_employee_section, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGetAllEmployeeViewModel()
        companyid?.let {
            getAllEmployeeViewModel.getAllEmployee(it)
        }
        observeEmployeeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Function for SetUp the GetAllEmployee ViewModel
    */
    private fun setupGetAllEmployeeViewModel() {
        getAllEmployeeViewModel = ViewModelProvider(
            this,
            GetAllEmployeeViewModelFactory(GetAllEmployeesRepository())
        )[GetAllEmployeeViewModel::class.java]
    }

    /*
    Function for get all employee data send from backend
     */
    @SuppressLint("SetTextI18n")
    private fun observeEmployeeData(){
        getAllEmployeeViewModel.employeeData.observe(viewLifecycleOwner){ response ->
            if(response.success){
                hideLoader()
                val list = response.data

                binding.txtTotalEmployee.text = "${list.size} Employees"

                adapter = EmpEmployeeCardAdapter(list)
                binding.rvEmployee.adapter = adapter
                adapter.updateData(list)
            }
            else {
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        getAllEmployeeViewModel.error.observe(viewLifecycleOwner) { error ->
//            Log.i("ERROR", error)
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        getAllEmployeeViewModel.isLoading.observe(viewLifecycleOwner){
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
         * @return A new instance of fragment EmpEmployeeSectionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmpEmployeeSectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}