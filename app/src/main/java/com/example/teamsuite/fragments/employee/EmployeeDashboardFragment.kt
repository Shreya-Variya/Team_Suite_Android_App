package com.example.teamsuite.fragments.employee

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.R
import com.example.teamsuite.abstractclass.BaseFragment
import com.example.teamsuite.data.model.attendance.breakin.BreakInData
import com.example.teamsuite.data.model.attendance.breakout.BreakOutData
import com.example.teamsuite.data.model.attendance.breakout.BreakOutRequest
import com.example.teamsuite.data.model.attendance.breakout.BreakTimeData
import com.example.teamsuite.data.model.attendance.clockin.ClockInData
import com.example.teamsuite.data.model.attendance.clockout.ClockOutData
import com.example.teamsuite.data.model.attendance.clockout.ClockOutHistoryData
import com.example.teamsuite.data.model.login.UserData
import com.example.teamsuite.databinding.FragmentEmployeeDashboardBinding
import com.example.teamsuite.repository.attendance.BreakInRepository
import com.example.teamsuite.repository.attendance.BreakOutRepository
import com.example.teamsuite.repository.attendance.ClockInRepository
import com.example.teamsuite.repository.attendance.ClockOutRepository
import com.example.teamsuite.repository.stats.EmployeeStatsRepository
import com.example.teamsuite.utils.Constant
import com.example.teamsuite.viewmodel.attendance.BreakInViewModel
import com.example.teamsuite.viewmodel.attendance.BreakOutViewModel
import com.example.teamsuite.viewmodel.attendance.ClockInViewModel
import com.example.teamsuite.viewmodel.attendance.ClockOutViewModel
import com.example.teamsuite.viewmodel.stats.EmployeeStatsViewModel
import com.example.teamsuite.viewmodelfactory.attendance.BreakInViewModelFactory
import com.example.teamsuite.viewmodelfactory.attendance.BreakOutViewModelFactory
import com.example.teamsuite.viewmodelfactory.attendance.ClockInViewModelFactory
import com.example.teamsuite.viewmodelfactory.attendance.ClockOutViewModelFactory
import com.example.teamsuite.viewmodelfactory.stats.EmployeeStatsViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EmployeeDashboardFragment : BaseFragment() {
    private var _binding: FragmentEmployeeDashboardBinding ?= null
    private val binding get() = _binding!!
    private val sharedPref by lazy {
        requireContext().getSharedPreferences(Constant.PREF_NAME, android.content.Context.MODE_PRIVATE)
    }
    private lateinit var clockInViewModel: ClockInViewModel
    private var clockInData: ClockInData? = null
    private lateinit var clockOutViewModel: ClockOutViewModel
    private var clockOutData: ClockOutData? = null
    private var clockOutHistoryData: ClockOutHistoryData? = null
    private lateinit var breakInViewModel: BreakInViewModel
    private var breakInData: BreakInData? = null
    private lateinit var breakOutViewModel: BreakOutViewModel
    private var breakOutData: BreakOutData? = null
    private var breakTimeData: BreakTimeData? = null
    private var userData: UserData? = null
    private val handler = Handler(Looper.getMainLooper())

    private var workStartTime = 0L
    private var workPausedTime = 0L
    private var isWorkRunning = false

    private var breakStartTime = 0L
    private var breakPausedTime = 0L
    private var isBreakRunning = false
    private lateinit var runnable: Runnable
    private lateinit var employeeStatsViewModel: EmployeeStatsViewModel

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
        _binding = FragmentEmployeeDashboardBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_employee_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRunnable()
        loadTimerState()
        setupEmployeeStatsViewModel()
        setupClockInViewModel()
        setupClockOutViewModel()
        setupBreakInViewModel()
        setupBreakOutViewModel()
        setProfileData()
        userData?.let {
            employeeStatsViewModel.getWorkHour(it._id)
            employeeStatsViewModel.getCurrentMonthAttendance(it._id)
            employeeStatsViewModel.getLeaveBalance(it._id)
        }
        setupClickListeners()
        observeClockInResponse()
        observeClockOutResponse()
        observeBreakInResponse()
        observeBreakOutResponse()
        observeGetWorkHourResponse()
        observeGetCurrentMonthAttendanceResponse()
        observeLeaveBalanceResponse()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }

    /*
    Function for setup clock in view model
     */
    private fun setupClockInViewModel(){
        clockInViewModel = ViewModelProvider(
            this,
            ClockInViewModelFactory(ClockInRepository())
        )[ClockInViewModel::class.java]
    }

    /*
    Function for setup clock out view model
     */
    private fun setupClockOutViewModel(){
        clockOutViewModel = ViewModelProvider(
            this,
            ClockOutViewModelFactory(ClockOutRepository())
        )[ClockOutViewModel::class.java]
    }

    /*
    Function for setup break in view model
     */
    private fun setupBreakInViewModel(){
        breakInViewModel = ViewModelProvider(
            this,
            BreakInViewModelFactory(BreakInRepository())
        )[BreakInViewModel::class.java]
    }

    /*
    Function for setup break out view model
     */
    private fun setupBreakOutViewModel(){
        breakOutViewModel = ViewModelProvider(
            this,
            BreakOutViewModelFactory(BreakOutRepository())
        )[BreakOutViewModel::class.java]
    }

    /*
    Function for set up the employee stats view model
     */
    private fun setupEmployeeStatsViewModel(){
        employeeStatsViewModel = ViewModelProvider(
            this,
            EmployeeStatsViewModelFactory(EmployeeStatsRepository())
        )[EmployeeStatsViewModel::class.java]
    }

    /*
    Function for set the profile data
     */
    private fun setProfileData(){
        userData?.let {
            val name = it.employeeName
            val initials = name
                .trim()
                .split("\\s+".toRegex())
                .take(2)
                .mapNotNull { it.firstOrNull()?.toString() }
                .joinToString("")
                .uppercase()
            binding.profileSection.txtProfileName.setText(initials.uppercase())
            binding.profileSection.txtWelcomeName.setText("Welcome, " + it.employeeName)
            binding.profileSection.txtRole.setText(it.jobRole.role)
        }
    }

    /*
    Function for setup click listeners
     */
    private fun setupClickListeners(){
        binding.timeTracking.switchClockIn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                startTimer(true)
                binding.timeTracking.switchBreak.isEnabled = true
                userData?.let {
                    clockInViewModel.clockIn(it._id)
                }
            }
            else{
                resetTimer()
                binding.timeTracking.switchBreak.isChecked = false
                binding.timeTracking.switchBreak.isEnabled = false
                userData?.let {
                    clockOutViewModel.clockOut(it._id)
                }
            }
        }

        binding.timeTracking.switchBreak.setOnCheckedChangeListener { _,  isChecked ->
            if (isChecked){
                pauseTimer(true)
                startTimer(false)
                binding.timeTracking.switchClockIn.isEnabled = false
                userData?.let {
                    breakInViewModel.breakIn(it._id)
                }
            }
            else{
                pauseTimer(false)
                startTimer(true)
                binding.timeTracking.switchClockIn.isEnabled = true
                val attendanceId = sharedPref.getString(Constant.ATTENDANCE_ID, null)
                val empid = userData?._id.toString()
                if (attendanceId != null){
                    breakOutViewModel.breakOut(empid, BreakOutRequest(attendanceId))
                }
                else{
                    Toast.makeText(requireContext(), "You are not break in.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /*
    Function for start the timer
     */
    private fun startTimer(isWork: Boolean){
        val currentTime = System.currentTimeMillis()
        if (isWork){
            workStartTime = currentTime
            isWorkRunning = true
        }
        else{
            breakStartTime = currentTime
            isBreakRunning = true
        }

        saveTimerState()
        handler.removeCallbacks(runnable)
        handler.post(runnable)
    }

    /*
    Function for pause the timer
     */
    private fun pauseTimer(isWork: Boolean){
        val currentTime = System.currentTimeMillis()

        if (isWork && isWorkRunning){
            workPausedTime += currentTime - workStartTime
            isWorkRunning = false
        }
        else if (!isWork && isBreakRunning){
            breakPausedTime += currentTime - breakStartTime
            isBreakRunning = false
        }

        saveTimerState()
    }

    /*
    Function for reset the timer
     */
    private fun resetTimer(){
        workStartTime = 0L
        workPausedTime = 0L
        isWorkRunning = false

        breakStartTime = 0L
        breakPausedTime = 0L
        isBreakRunning = false

        sharedPref.edit {
            remove(Constant.ATTENDANCE_ID)
        }

        handler.removeCallbacks(runnable)
        saveTimerState()
        binding.timeTracking.txtWorkTimer.text = "00:00:00"
        binding.timeTracking.txtBreakTimer.text = "00:00:00"
    }

    /*
    Function for save timer state
     */
    private fun saveTimerState(){
        sharedPref.edit {
            putLong(Constant.WORK_START, workStartTime)
            putLong(Constant.WORK_PAUSED, workPausedTime)
            putBoolean(Constant.IS_WORK_RUNNING, isWorkRunning)

            putLong(Constant.BREAK_START, breakStartTime)
            putLong(Constant.BREAK_PAUSED, breakPausedTime)
            putBoolean(Constant.IS_BREAK_RUNNING, isBreakRunning)
        }
    }

    /*
    Function for load timer state after app starts
     */
    private fun loadTimerState(){
        workStartTime = sharedPref.getLong(Constant.WORK_START, 0)
        workPausedTime = sharedPref.getLong(Constant.WORK_PAUSED, 0)
        isWorkRunning = sharedPref.getBoolean(Constant.IS_WORK_RUNNING, false)

        breakStartTime = sharedPref.getLong(Constant.BREAK_START, 0)
        breakPausedTime = sharedPref.getLong(Constant.BREAK_PAUSED, 0)
        isBreakRunning = sharedPref.getBoolean(Constant.IS_BREAK_RUNNING, false)

        binding.timeTracking.switchClockIn.isChecked = isWorkRunning || workPausedTime > 0

        binding.timeTracking.switchBreak.isChecked = isBreakRunning

        binding.timeTracking.switchBreak.isEnabled = isWorkRunning || workPausedTime > 0

        binding.timeTracking.switchClockIn.isEnabled = !isBreakRunning

        if (isWorkRunning || isBreakRunning){
            handler.removeCallbacks(runnable)
            handler.post(runnable)
        }
    }

    /*
    Function for initialize runnable
     */
    private fun initRunnable(){
        runnable = object : Runnable {
            override fun run() {
                val currentTime = System.currentTimeMillis()

                val workTime = if (isWorkRunning){
                    workPausedTime + (currentTime - workStartTime)
                }
                else{
                    workPausedTime
                }

                val breakTime = if (isBreakRunning){
                    breakPausedTime + (currentTime - breakStartTime)
                }
                else{
                    breakPausedTime
                }

                binding.timeTracking.txtWorkTimer.text = formatTime(workTime)
                binding.timeTracking.txtBreakTimer.text = formatTime(breakTime)
                handler.postDelayed(this, 1000)
            }
        }
    }

    /*
    Function for format time
     */
    private fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    /*
    Function for observe the response of clock in
     */
    private fun observeClockInResponse(){
        clockInViewModel.clockInResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                clockInData = response.clockin
                sharedPref.edit{
                    putString(Constant.ATTENDANCE_ID, response.clockin?.attendanceId)
                }
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        clockInViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        clockInViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the response of clock out
     */
    private fun observeClockOutResponse(){
        clockOutViewModel.clockOutResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                clockOutData = response.clockout
                clockOutHistoryData = response.clockoutHistory
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        clockOutViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        clockOutViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the response of break in
     */
    private fun observeBreakInResponse(){
        breakInViewModel.breakInResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                breakInData = response.breakin
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        breakInViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        breakInViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the response of break out
     */
    private fun observeBreakOutResponse(){
        breakOutViewModel.breakOutResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                breakOutData = response.breakout
                breakTimeData = response.breaktime
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        breakOutViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        breakOutViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the get work hour response
     */
    private fun observeGetWorkHourResponse(){
        employeeStatsViewModel.getWorkHourResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                binding.employeestats.txtWorkHour.text = response.totalWorkHours
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        employeeStatsViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        employeeStatsViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the get current month attendance response
     */
    private fun observeGetCurrentMonthAttendanceResponse(){
        employeeStatsViewModel.getCurrentMonthAttendanceResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                binding.employeestats.txtPresentDays.text = response.present.toString() + " days"
                binding.employeestats.txtAbsentDays.text = response.absent.toString() + " days"
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        employeeStatsViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        employeeStatsViewModel.isLoading.observe(viewLifecycleOwner){
            if (it) showLoader() else hideLoader()
        }
    }

    /*
    Function for observe the leave balance response
     */
    private fun observeLeaveBalanceResponse(){
        employeeStatsViewModel.getLeaveBalanceResult.observe(viewLifecycleOwner){ response ->
            if (response.success){
                hideLoader()
                binding.employeestats.txtLeaveDays.text = response.leavebalance.toString() + " days"
            }
            else{
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            }
        }
        employeeStatsViewModel.error.observe(viewLifecycleOwner){ error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
        employeeStatsViewModel.isLoading.observe(viewLifecycleOwner){
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
         * @return A new instance of fragment EmployeeDashboardFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmployeeDashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}