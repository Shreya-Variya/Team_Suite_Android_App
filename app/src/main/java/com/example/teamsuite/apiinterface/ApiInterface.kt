package com.example.teamsuite.apiinterface

import com.example.teamsuite.data.model.adminstats.GetTotalEmployeesResponse
import com.example.teamsuite.data.model.adminstats.OnLeaveEmployeesResponse
import com.example.teamsuite.data.model.attendance.attendancereport.AttendanceByDateRequest
import com.example.teamsuite.data.model.attendance.attendancereport.AttendanceByDateResponse
import com.example.teamsuite.data.model.attendance.attendancereport.AttendanceReportResponse
import com.example.teamsuite.data.model.attendance.attendancereport.GetEmployeeAttendanceResponse
import com.example.teamsuite.data.model.attendance.breakin.BreakInResponse
import com.example.teamsuite.data.model.attendance.breakout.BreakOutRequest
import com.example.teamsuite.data.model.attendance.breakout.BreakOutResponse
import com.example.teamsuite.data.model.attendance.clockin.ClockInResponse
import com.example.teamsuite.data.model.attendance.clockout.ClockOutResponse
import com.example.teamsuite.data.model.changepassword.ChangePasswordRequest
import com.example.teamsuite.data.model.changepassword.ChangePasswordResponse
import com.example.teamsuite.data.model.company.GetCompanyDataResponse
import com.example.teamsuite.data.model.department.GetDepartmentResponse
import com.example.teamsuite.data.model.employee.AddEmployeeResponse
import com.example.teamsuite.data.model.employee.AddEmployeeWrapper
import com.example.teamsuite.data.model.employee.DeleteEmployeeResponse
import com.example.teamsuite.data.model.employee.GetAllEmployeeResponse
import com.example.teamsuite.data.model.employee.GetSpecificEmployeeResponse
import com.example.teamsuite.data.model.employee.UpdateEmployeeResponse
import com.example.teamsuite.data.model.employee.UpdateEmployeeWrapper
import com.example.teamsuite.data.model.employeestats.GetCurrentMonthAttendanceResponse
import com.example.teamsuite.data.model.employeestats.GetLeaveBalanceResponse
import com.example.teamsuite.data.model.employeestats.GetWorkHourResponse
import com.example.teamsuite.data.model.forgotpassword.ForgotPasswordResponse
import com.example.teamsuite.data.model.forgotpassword.ResetPasswordRequest
import com.example.teamsuite.data.model.forgotpassword.SendCodeRequest
import com.example.teamsuite.data.model.forgotpassword.VerifyCodeRequest
import com.example.teamsuite.data.model.jobrole.GetJobRoleResponse
import com.example.teamsuite.data.model.leave.AcceptRejectLeaveRequest
import com.example.teamsuite.data.model.leave.AcceptRejectLeaveResponse
import com.example.teamsuite.data.model.leave.ApplyForLeaveRequest
import com.example.teamsuite.data.model.leave.ApplyForLeaveResponse
import com.example.teamsuite.data.model.leave.GetAllEmployeeLeaveReportResponse
import com.example.teamsuite.data.model.leave.GetLeaveReportResponse
import com.example.teamsuite.data.model.leave.LeaveByDateRequest
import com.example.teamsuite.data.model.leavepolicy.AddLeavePolicyResponse
import com.example.teamsuite.data.model.leavepolicy.AddLeavePolicyWrapper
import com.example.teamsuite.data.model.leavepolicy.GetLeavePolicyResponse
import com.example.teamsuite.data.model.login.LoginRequest
import com.example.teamsuite.data.model.login.LoginResponse
import com.example.teamsuite.data.model.notification.RemoveFcmTokenResponse
import com.example.teamsuite.data.model.notification.SaveFcmTokenRequest
import com.example.teamsuite.data.model.notification.SaveFcmTokenResponse
import com.example.teamsuite.data.model.notification.SendNotificationToAdminRequest
import com.example.teamsuite.data.model.notification.SendNotificationToAdminResponse
import com.example.teamsuite.data.model.notification.SendNotificationToEmployeeRequest
import com.example.teamsuite.data.model.notification.SendNotificationToEmployeeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {

    //Login Module
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    //Forgot Password Module
    //Send Verification Code
    @POST("/auth/sendcode")
    suspend fun sendcode(@Body request: SendCodeRequest): Response<ForgotPasswordResponse>

    //Verify the Code
    @POST("/auth/verifycode")
    suspend fun verifyCode(@Body request: VerifyCodeRequest): Response<ForgotPasswordResponse>

    //Reset the Password
    @POST("/auth/resetpassword")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ForgotPasswordResponse>

    //Change Password
    @POST("/auth/changepassword")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<ChangePasswordResponse>

    //Department Module
    @GET("/getdepartment")
    suspend fun getDepartment(): Response<GetDepartmentResponse>

    //Job Role Module
    @GET("/jobrole/{id}")
    suspend fun getJobRole(
        @Path("id") id: String
    ): Response<GetJobRoleResponse>

    //Employee Module
    //Show Employees
    @GET("/employee/{companyid}")
    suspend fun getAllEmployee(
        @Path("companyid") companyid: String
    ): Response<GetAllEmployeeResponse>

    //Add Employeee
    @POST("/employee")
    suspend fun addEmployee(@Body request: AddEmployeeWrapper): Response<AddEmployeeResponse>

    //Get Specific Employee
    @GET("/employee/getemp")
    suspend fun getSpecificEmployee(@Body email: String): Response<GetSpecificEmployeeResponse>

    //Update the employee data
    @PUT("/employee/{id}")
    suspend fun updateEmployee(
        @Path("id") id: String,
        @Body request: UpdateEmployeeWrapper
    ): Response<UpdateEmployeeResponse>

    //Delete the employee
    @DELETE("/employee/{id}")
    suspend fun deleteEmployee(
        @Path("id") id: String
    ): Response<DeleteEmployeeResponse>

    //Attendance Module
    //Clock-In
    @POST("/attendance/clockin/{empid}")
    suspend fun clockIn(
        @Path("empid") empid: String
    ): Response<ClockInResponse>

    //Clock-Out
    @POST("/attendance/clockout/{empid}")
    suspend fun clockOut(
        @Path("empid") empid: String
    ): Response<ClockOutResponse>

    //Break-In
    @POST("/attendance/breakin/{empid}")
    suspend fun breakIn(
        @Path("empid") empid: String
    ): Response<BreakInResponse>

    //Break-Out
    @POST("/attendance/breakout/{empid}")
    suspend fun breakOut(
        @Path("empid") empid: String,
        @Body request: BreakOutRequest
    ): Response<BreakOutResponse>

    //Attendance Report
    @GET("/attendance/weeklyreport/{empid}")
    suspend fun getAttendanceReport(
        @Path("empid") empid: String
    ): Response<AttendanceReportResponse>

    //Employee Attendance Report
    @GET("/attendance/empattendance/{companyid}")
    suspend fun getEmployeeAttendance(
        @Path("companyid") companyid: String
    ): Response<GetEmployeeAttendanceResponse>

    //Employee attendance by date
    @POST("/attendance/date/{companyid}")
    suspend fun attendanceByDate(
        @Path ("companyid") companyid: String,
        @Body request: AttendanceByDateRequest
    ): Response<AttendanceByDateResponse>

    //Leave Policy Module
    //Add leave policy
    @POST("/leavepolicy/add")
    suspend fun addLeavePolicy(
        @Body request: AddLeavePolicyWrapper
    ): Response<AddLeavePolicyResponse>

    //Get leave policy
    @GET("/leavepolicy/{companyid}")
    suspend fun getLeavePolicy(
        @Path("companyid") companyid: String
    ): Response<GetLeavePolicyResponse>

    //Leave Module
    //Apply for leave
    @POST("/leave/apply")
    suspend fun applyForLeave(
        @Body request: ApplyForLeaveRequest
    ): Response<ApplyForLeaveResponse>

    //Accept the leave request
    @PATCH("/leave/accept")
    suspend fun acceptLeave(
        @Body request: AcceptRejectLeaveRequest
    ): Response<AcceptRejectLeaveResponse>

    //Reject the leave request
    @PATCH("/leave/reject")
    suspend fun rejectLeave(
        @Body request: AcceptRejectLeaveRequest
    ): Response<AcceptRejectLeaveResponse>

    //Get the monthly leave report of employee
    @GET("/leave/{empid}")
    suspend fun getLeaveReport(
        @Path ("empid") empid: String
    ): Response<GetLeaveReportResponse>

    //Get all leave records of employee
    @GET("/leave/allemp/{empid}")
    suspend fun getAllLeaveRecords(
        @Path ("empid") empid: String
    ): Response<GetLeaveReportResponse>

    //Get approved leave records of employee
    @GET("/leave/approvedemp/{empid}")
    suspend fun getApprovedLeaveRecords(
        @Path ("empid") empid: String
    ): Response<GetLeaveReportResponse>

    //Get pending leave records of employee
    @GET("/leave/pendingemp/{empid}")
    suspend fun getPendingLeaveRecords(
        @Path ("empid") empid: String
    ): Response<GetLeaveReportResponse>

    //Get rejected leave records of employee
    @GET("/leave/rejectedemp/{empid}")
    suspend fun getRejectedLeaveRecords(
        @Path ("empid") empid: String
    ): Response<GetLeaveReportResponse>

    //Get monthly leave report of all employees
    @GET("/leave/leavereport/{companyid}")
    suspend fun getEmployeeLeaveReport(
        @Path("companyid") companyid: String
    ): Response<GetAllEmployeeLeaveReportResponse>

    //Get all leave records
    @GET("/leave/all/{companyid}")
    suspend fun getAllLeaveRecord(
        @Path("companyid") companyid: String
    ): Response<GetAllEmployeeLeaveReportResponse>

    //Get approved leave records
    @GET("/leave/approved/{companyid}")
    suspend fun getApprovedLeaveRecord(
        @Path("companyid") companyid: String
    ): Response<GetAllEmployeeLeaveReportResponse>

    //Get pending leave records
    @GET("/leave/pending/{companyid}")
    suspend fun getPendingLeaveRecord(
        @Path("companyid") companyid: String
    ): Response<GetAllEmployeeLeaveReportResponse>

    //Get approved leave records
    @GET("/leave/rejected/{companyid}")
    suspend fun getRejectedLeaveRecord(
        @Path("companyid") companyid: String
    ): Response<GetAllEmployeeLeaveReportResponse>

    //Get leave record of particular date
    @POST("/leave/date/{companyid}")
    suspend fun getLeaveByDate(
        @Path("companyid") companyid: String,
        @Body request: LeaveByDateRequest
    ): Response<GetAllEmployeeLeaveReportResponse>

    //Notification Module
    //Save the fcm token to database
    @POST("/notification/savetoken")
    suspend fun saveFcmToken(
        @Body request: SaveFcmTokenRequest
    ): Response<SaveFcmTokenResponse>

    //Remove the fcm token to database
    @DELETE("/notification/delete/{empid}")
    suspend fun removeFcmToken(
        @Path("empid") empid: String
    ): Response<RemoveFcmTokenResponse>

    //Send the notification to admin
    @POST("/notification/sendtoadmin")
    suspend fun sendNotificationToAdmin(
        @Body request: SendNotificationToAdminRequest
    ): Response<SendNotificationToAdminResponse>

    //Send notification to employee
    @POST("/notification/sendtoemployee")
    suspend fun sendNotificationToEmployee(
        @Body request: SendNotificationToEmployeeRequest
    ): Response<SendNotificationToEmployeeResponse>

    //Employee Dashboard stat module
    //get current month working hours
    @GET("/getworkhour/{empid}")
    suspend fun getWorkHour(
        @Path("empid") empid: String
    ): Response<GetWorkHourResponse>

    //get current month attendance
    @GET("/getcurrentmonthattendance/{empid}")
    suspend fun getCurrentMonthAttendance(
        @Path("empid") empid: String
    ): Response<GetCurrentMonthAttendanceResponse>

    //get leave balance
    @GET("/getleavebalance/{empid}")
    suspend fun getLeaveBalance(
        @Path("empid") empid: String
    ): Response<GetLeaveBalanceResponse>

    //Admin Dashboard stat module
    //get companys total present & absent employees
    @GET("/todaysattendance/{companyid}")
    suspend fun getTotalEmployees(
        @Path("companyid")companyid: String
    ): Response<GetTotalEmployeesResponse>

    //get on leave employees
    @GET("/onleaveemployees/{companyid}")
    suspend fun onLeaveEmployees(
        @Path("companyid")companyid: String
    ): Response<OnLeaveEmployeesResponse>

    //Company module
    //Get company data
    @GET("/company/{companyid}")
    suspend fun getCompanyData(
        @Path("companyid")companyid: String
    ): Response<GetCompanyDataResponse>

}