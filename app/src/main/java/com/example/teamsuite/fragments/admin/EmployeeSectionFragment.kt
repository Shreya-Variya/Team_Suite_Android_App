package com.example.teamsuite.fragments.admin

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
import com.example.teamsuite.adapter.EmployeeCardAdapter
import com.example.teamsuite.databinding.FragmentEmployeeSectionBinding
import com.example.teamsuite.repository.employee.DeleteEmployeeRepository
import com.example.teamsuite.repository.employee.GetAllEmployeesRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.employee.DeleteEmployeeViewModel
import com.example.teamsuite.viewmodel.employee.GetAllEmployeeViewModel
import com.example.teamsuite.viewmodelfactory.employee.DeleteEmployeeViewModelFactory
import com.example.teamsuite.viewmodelfactory.employee.GetAllEmployeeViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EmployeeSectionFragment : BaseFragment() {
    private var _binding: FragmentEmployeeSectionBinding ?= null
    private val binding get() = _binding!!
    private lateinit var viewModel: GetAllEmployeeViewModel
    private lateinit var deleteEmployeeViewModel: DeleteEmployeeViewModel
    private lateinit var adapter: EmployeeCardAdapter
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
        _binding = FragmentEmployeeSectionBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_employee_section, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupDeleteEmployeeViewModel()
        companyid?.let {
            viewModel.getAllEmployee(it)
        }
        observeEmployeeData()
        observeDeleteEmployeeResponse()
        binding.btnAddEmployee.setOnClickListener {
            val fragment = AddEmployeeFragment()
            val bundle = Bundle().apply {
                putString(Constant.companyid, companyid)
            }
            fragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment )
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Function for SetUp the GetAllEmployee ViewModel
    */
    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            GetAllEmployeeViewModelFactory(GetAllEmployeesRepository())
        )[GetAllEmployeeViewModel::class.java]
    }

    /*
    Function for SetUp the DeleteEmployee ViewModel
    */
    private fun setupDeleteEmployeeViewModel() {
        deleteEmployeeViewModel = ViewModelProvider(
            this,
            DeleteEmployeeViewModelFactory(DeleteEmployeeRepository())
        )[DeleteEmployeeViewModel::class.java]
    }

    /*
    Function for get all employee data send from backend
     */
    @SuppressLint("SetTextI18n")
    private fun observeEmployeeData(){
        viewModel.employeeData.observe(viewLifecycleOwner){ response ->
            if(response.success){
                hideLoader()
                val list = response.data

                binding.txtTotalEmployee.text = "${list.size} Employees"

                adapter = EmployeeCardAdapter(list,
                    onEditClick = { employee ->
                        val bundle = Bundle().apply {
                            putParcelable(Constant.Employee_Data, employee)
                            putString(Constant.companyid, companyid)
                        }
//                        Log.i("Employee Data For Update", employee.toString())
                        val fragment = EditEmployeeFragment()
                        fragment.arguments = bundle
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .addToBackStack(null)
                            .commit()
                    },
                    onDeleteClick = { employee ->
                        showDeleteConfirmationDialog(employee._id)
                    }
                )
                binding.rvEmployee.adapter = adapter
                adapter.updateData(list)
            }
            else {
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
//            Log.i("ERROR", error)
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        viewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
     Function for get Update Employee Response send from backend
     */
    private fun observeDeleteEmployeeResponse(){
        deleteEmployeeViewModel.deleteEmployeeResult.observe(viewLifecycleOwner) { response ->
            if (response.success){
                hideLoader()
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                companyid?.let {
                    viewModel.getAllEmployee(it)
                }
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        deleteEmployeeViewModel.error.observe(viewLifecycleOwner){error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        deleteEmployeeViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for create delete confirmation dialog box
     */
    private fun showDeleteConfirmationDialog(id: String){
        val dialog =MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_employee))
            .setMessage(getString(R.string.delete_confirmation_statement))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.delete)){dialog,_ ->
                deleteEmployeeViewModel.deleteEmployee(id)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EmployeeSectionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmployeeSectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}