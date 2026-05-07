package com.example.teamsuite.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.R
import com.example.teamsuite.abstractclass.BaseFragment
import com.example.teamsuite.data.model.department.DepartmentData
import com.example.teamsuite.data.model.employee.UpdateEmployeeRequest
import com.example.teamsuite.data.model.employee.UpdateEmployeeWrapper
import com.example.teamsuite.data.model.jobrole.JobRoleData
import com.example.teamsuite.data.model.login.Address
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import com.example.teamsuite.data.model.login.UserData
import com.example.teamsuite.databinding.FragmentEditEmployeeBinding
import com.example.teamsuite.repository.department.GetDepartmentRepository
import com.example.teamsuite.repository.employee.UpdateEmployeeRepository
import com.example.teamsuite.repository.jobrole.GetJobRoleRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.department.GetDepartmentViewModel
import com.example.teamsuite.viewmodel.employee.UpdateEmployeeViewModel
import com.example.teamsuite.viewmodel.jobrole.GetJobRoleViewModel
import com.example.teamsuite.viewmodelfactory.department.GetDepartmentViewModelFactory
import com.example.teamsuite.viewmodelfactory.employee.UpdateEmployeeViewModelFactory
import com.example.teamsuite.viewmodelfactory.jobrole.GetJobRoleViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditEmployeeFragment : BaseFragment() {
    private var _binding: FragmentEditEmployeeBinding? = null
    private val binding get() = _binding!!
    private lateinit var departmentViewModel: GetDepartmentViewModel
    private lateinit var jobRoleViewModel: GetJobRoleViewModel
    private lateinit var updateEmployeeViewModel: UpdateEmployeeViewModel
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
        _binding = FragmentEditEmployeeBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_edit_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val employee = arguments?.getParcelable<UserData>(Constant.Employee_Data)
//        Log.i("Employee Data Ready Update", employee.toString())
        employee?.let {
            binding.etEmpId.setText(it.employeeId)
            binding.etName.setText(it.employeeName)
            binding.etDob.setText(formatDate(it.dob))
            when(it.gender.lowercase()){
                "male" -> binding.genderGroup.check(R.id.radioMale)
                "female" -> binding.genderGroup.check(R.id.radioFemale)
            }
            binding.checkMarried.isChecked = it.maritalStatus
            binding.etEmail.setText(it.email)
            binding.etMobile.setText(it.mobileNo)
            binding.etStreet.setText(it.address.street)
            binding.etCity.setText(it.address.city)
            binding.etState.setText(it.address.state)
            binding.etEducation.setText(it.education)
            binding.etExperience.setText(it.experience.toString())
//            binding.etDepartment.setText(it.department.name)
//            binding.etJobRole.setText(it.jobRole.role)
            binding.etJoinDate.setText(formatDate(it.joinDate))
        }

        setupDepartmentViewModel()
        setupJobroleViewModel()
        setupUpdateEmployeeViewModel()
        departmentViewModel.getDepartment()
        observeDepartmentData()
        observeJobRoleData()
        observeUpdateEmployeeResponse()

        binding.btnUpdateEmployee.setOnClickListener {
            if (validateInputFields()){
                UpdateEmployeeIntoDatabase()
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
            return false
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
    Function for update employee into database
     */
    private fun UpdateEmployeeIntoDatabase(){
        val selectedGender = when(binding.genderGroup.checkedRadioButtonId){
            R.id.radioMale -> Constant.male
            R.id.radioFemale -> Constant.female
            else -> ""
        }

        val request = UpdateEmployeeRequest(
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
        val wrapper = UpdateEmployeeWrapper(request)
        val employeeId = arguments?.getParcelable<UserData>(Constant.Employee_Data)?._id ?: ""
        updateEmployeeViewModel.updateEmployee(employeeId,wrapper)
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
    Function for SetUp the Update employee ViewModel
     */
    private fun setupUpdateEmployeeViewModel(){
        updateEmployeeViewModel = ViewModelProvider(
            this,
            UpdateEmployeeViewModelFactory(UpdateEmployeeRepository())
        )[UpdateEmployeeViewModel::class.java]
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
     Function for get Update Employee Response send from backend
     */
    private fun observeUpdateEmployeeResponse(){
        updateEmployeeViewModel.updateEmployeeResult .observe(viewLifecycleOwner) { response ->
            if (response.success){
                hideLoader()
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        updateEmployeeViewModel.error.observe(viewLifecycleOwner){error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        updateEmployeeViewModel.isLoading.observe(viewLifecycleOwner){
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

        val employee = arguments?.getParcelable<UserData>(Constant.Employee_Data)
        employee?.let {
            val position = departmentlist.indexOfFirst { dept ->
                dept._id == it.department._id
            }
            if (position != -1){
                binding.etDepartment.setText(departmentlist[position].name, false)
                selectedDepartmentId = departmentlist[position]._id
                jobRoleViewModel.getJobRole(selectedDepartmentId!!)
            }
        }

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

        val employee = arguments?.getParcelable<UserData>("employeeData")
        employee?.let {
            val position = jobrolelist.indexOfFirst { role ->
                role._id == it.jobRole._id
            }
            if (position != -1){
                binding.etJobRole.setText(jobrolelist[position].role, false)
                selectedJobroleId = jobrolelist[position]._id
            }
        }

        binding.etJobRole.setOnItemClickListener{ parent, view, position, id ->
            val selectedRole = jobrolelist[position]
            selectedJobroleId = selectedRole._id
        }
    }

    /*
    Function for format the date send from backend
     */
    private fun formatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return ""

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val date = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateString.substring(0, 10) // fallback
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditEmployeeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditEmployeeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}