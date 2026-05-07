package com.example.teamsuite.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import android.app.DatePickerDialog
import android.widget.EditText
import java.util.Calendar
import java.util.Locale
import java.text.SimpleDateFormat
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.R
import com.example.teamsuite.abstractclass.BaseFragment
import com.example.teamsuite.data.model.department.DepartmentData
import com.example.teamsuite.data.model.employee.AddEmployeeRequest
import com.example.teamsuite.data.model.employee.AddEmployeeWrapper
import com.example.teamsuite.data.model.jobrole.JobRoleData
import com.example.teamsuite.data.model.login.Address
import com.example.teamsuite.databinding.FragmentAddEmployeeBinding
import com.example.teamsuite.repository.department.GetDepartmentRepository
import com.example.teamsuite.repository.employee.AddEmployeeRepository
import com.example.teamsuite.repository.jobrole.GetJobRoleRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.department.GetDepartmentViewModel
import com.example.teamsuite.viewmodel.employee.AddEmployeeViewModel
import com.example.teamsuite.viewmodel.jobrole.GetJobRoleViewModel
import com.example.teamsuite.viewmodelfactory.department.GetDepartmentViewModelFactory
import com.example.teamsuite.viewmodelfactory.employee.AddEmployeeViewModelFactory
import com.example.teamsuite.viewmodelfactory.jobrole.GetJobRoleViewModelFactory


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddEmployeeFragment : BaseFragment() {
    private var _binding: FragmentAddEmployeeBinding ?= null
    private val binding get() = _binding!!
    private lateinit var departmentViewModel: GetDepartmentViewModel
    private lateinit var jobRoleViewModel: GetJobRoleViewModel
    private lateinit var addEmployeeViewModel: AddEmployeeViewModel

    private var departmentlist: List<DepartmentData> = emptyList()
    private var selectedDepartmentId: String? = null
    private var selectedJobroleId: String? = null

    private var companyid: String? = null

//    private var param1: String? = null
//    private var param2: String? = null

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
        _binding = FragmentAddEmployeeBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDepartmentViewModel()
        setupJobroleViewModel()
        setupAddEmployeeViewModel()
        departmentViewModel.getDepartment()
        observeDepartmentData()
        observeJobRoleData()
        observeAddEmployeeResponse()

        binding.etDob.setOnClickListener {
            openDatePicker(binding.etDob, isDob = true)
        }

        binding.dobLayout.setEndIconOnClickListener {
            openDatePicker(binding.etDob, isDob = true)
        }

        binding.etJoinDate.setOnClickListener {
            openDatePicker(binding.etJoinDate)
        }

        binding.joinLayout.setEndIconOnClickListener {
            openDatePicker(binding.etJoinDate)
        }

        binding.btnAddEmployee.setOnClickListener {
            if (validateInputFields()){
                AddEmployeeIntoDatabase()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Function for validate the input fields
     */
    private fun validateInputFields(): Boolean{
        //Employee Id
        if (binding.etEmpId.text.toString().trim().isEmpty()){
            binding.empIdLayout.error = getString(R.string.empid_required)
            return false
        }
        else binding.empIdLayout.error = null

        //Employee Name
        if (binding.etName.text.toString().trim().isEmpty()){
            binding.nameLayout.error = getString(R.string.name_required)
            return false
        }
        else binding.nameLayout.error = null

        //Date of Birth
        if (binding.etDob.text.toString().trim().isEmpty()){
            binding.dobLayout.error = getString(R.string.dob_required)
            return false
        }
        else binding.dobLayout.error = null

        //Gender
        if (binding.genderGroup.checkedRadioButtonId == -1){
            Toast.makeText(requireContext(),getString(R.string.select_gender), Toast.LENGTH_SHORT).show()
            return false
        }

        //Email
        if(binding.etEmail.text.toString().trim().isEmpty()){
            binding.emailLayout.error = getString(R.string.email_required)
            return false
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString().trim()).matches()){
            binding.emailLayout.error = getString(R.string.validate_email)
            return false
        }
        else binding.emailLayout.error = null

        //Mobile no
        if(binding.etMobile.text.toString().trim().isEmpty()){
            binding.mobileLayout.error = getString(R.string.mno_required)
        }
        else if (binding.etMobile.text.toString().trim().length < 10){
            binding.mobileLayout.error = getString(R.string.validate_mno)
            return false
        }
        else binding.mobileLayout.error = null

        //Street
        if (binding.etStreet.text.toString().trim().isEmpty()){
            binding.streetLayout.error = getString(R.string.street_required)
            return false
        }
        else binding.streetLayout.error = null

        //City
        if (binding.etCity.text.toString().trim().isEmpty()){
            binding.cityLayout.error = getString(R.string.city_required)
            return false
        }
        else binding.cityLayout.error = null

        //State
        if (binding.etState.text.toString().trim().isEmpty()){
            binding.stateLayout.error = getString(R.string.state_required)
            return false
        }
        else binding.stateLayout.error = null

        //Education
        if (binding.etEducation.text.toString().trim().isEmpty()){
            binding.educationLayout.error = getString(R.string.education_required)
            return false
        }
        else binding.educationLayout.error = null

        //Experience
        if (binding.etExperience.text.toString().trim().isEmpty()){
            binding.expLayout.error = getString(R.string.exp_required)
            return false
        }
        else binding.expLayout.error = null

        //Department
        if (binding.etDepartment.text.toString().trim().isEmpty()){
            binding.deptLayout.error = getString(R.string.select_dept)
            return false
        }
        else binding.deptLayout.error = null

        //Job role
        if (binding.etJobRole.text.toString().trim().isEmpty()){
            binding.jobLayout.error = getString(R.string.select_job_role)
            return false
        }
        else binding.jobLayout.error = null

        //Join Date
        if (binding.etJoinDate.text.toString().trim().isEmpty()){
            binding.joinLayout.error = getString(R.string.joindate_required)
            return false
        }
        else binding.joinLayout.error = null

        return true
    }

    /*
    Function for add employee into database
     */
    private fun AddEmployeeIntoDatabase(){
        val selectedGender = when(binding.genderGroup.checkedRadioButtonId){
            R.id.radioMale -> Constant.male
            R.id.radioFemale -> Constant.female
            else -> ""
        }

        val request = AddEmployeeRequest(
            binding.etEmpId.text.toString().trim(),
            binding.etName.text.toString().trim(),
            binding.etDob.text.toString().trim(),
            selectedGender,
            binding.checkMarried.isChecked,
            binding.etEmail.text.toString().trim(),
            binding.etMobile.text.toString().trim(),
            address = Address(
                binding.etStreet.text.toString().trim(),
                binding.etCity.text.toString().trim(),
                binding.etState.text.toString().trim()
            ),
            binding.etEducation.text.toString().trim(),
            binding.etExperience.text.toString().trim(),
            selectedDepartmentId ?: "",
            selectedJobroleId ?: "",
            binding.etJoinDate.text.toString().trim(),
            companyid.toString().trim()
        )
//        Log.i("Add Employee", request.toString())
        val wrapper = AddEmployeeWrapper(request)
        addEmployeeViewModel.addEmployee(wrapper)
    }

    /*
    Function for SetUp the Department ViewModel
    */
    private fun setupDepartmentViewModel() {
        departmentViewModel = ViewModelProvider(
            this,
            GetDepartmentViewModelFactory(GetDepartmentRepository())
        )[GetDepartmentViewModel::class.java]
    }

    /*
    Function for SetUp the JobRole ViewModel
    */
    private fun setupJobroleViewModel(){
        jobRoleViewModel = ViewModelProvider(
            this,
            GetJobRoleViewModelFactory(GetJobRoleRepository())
        )[GetJobRoleViewModel::class.java]
    }
    
    /*
    Function for SetUp the Addemployee ViewModel
     */
    private fun setupAddEmployeeViewModel(){
        addEmployeeViewModel = ViewModelProvider(
            this,
            AddEmployeeViewModelFactory(AddEmployeeRepository())
        )[AddEmployeeViewModel::class.java]
    }

    /*
    Function for get department data send from backend
     */
    private fun observeDepartmentData(){
        departmentViewModel.departmentData.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                departmentlist = response.data
                setupDepartmentDropdown(departmentlist)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        departmentViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        departmentViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for get jobrole data send from backend
     */
    private fun observeJobRoleData(){
        jobRoleViewModel.jobRoleData.observe(viewLifecycleOwner){response ->
            if (response.success){
                hideLoader()
                setupJobRoleDropdown(response.data)
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        jobRoleViewModel.error.observe(viewLifecycleOwner){error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        jobRoleViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
     Function for get Add Employee Response send from backend
     */
    private fun observeAddEmployeeResponse(){
        addEmployeeViewModel.addEmployeeResult.observe(viewLifecycleOwner) { response ->
            if (response.success){
                hideLoader()
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        addEmployeeViewModel.error.observe(viewLifecycleOwner){error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        addEmployeeViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for setup department dropdown
     */
    private fun setupDepartmentDropdown(departmentlist: List<DepartmentData>){
        val list = departmentlist.map { it.name }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            list
        )
        binding.etDepartment.setAdapter(adapter)

        binding.etDepartment.setOnItemClickListener { parent, view, position, id ->
            val selectedDepartment = departmentlist[position]
            selectedDepartmentId = selectedDepartment._id

            selectedJobroleId = null
            binding.etJobRole.setText("")

            selectedDepartmentId?.let {
                jobRoleViewModel.getJobRole(it)
            }
        }
    }

    /*
    Function for setup jobrole dropdown
     */
    private fun setupJobRoleDropdown(jobrolelist: List<JobRoleData>){
        val list = jobrolelist.map { it.role }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            list
        )

        binding.etJobRole.setAdapter(adapter)

        binding.etJobRole.setOnItemClickListener{ parent, view, position, id ->
            val selectedRole = jobrolelist[position]
            selectedJobroleId = selectedRole._id
        }
    }

    /*
    Function for set calender on click to edittext of dob and join date
     */
    private fun openDatePicker(editText: EditText, isDob: Boolean = false) {

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

        if (isDob) {
            dialog.datePicker.maxDate = System.currentTimeMillis()
        }

        dialog.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddEmployeeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddEmployeeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}