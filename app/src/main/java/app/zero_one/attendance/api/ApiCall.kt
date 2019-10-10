package app.zero_one.attendance.api

import app.zero_one.attendance.model.Employee
import app.zero_one.attendance.model.EmployeeDetail
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiCall {

    @GET("?e76c37b493ea168cea60b8902072387caf297979")
    fun getAllEmployees(): Call<List<Employee>>

    @FormUrlEncoded
    @POST("?e76c37b493ea168cea60b8902072387caf297979")
    fun getEmployeeDetail(
        @Field("emp_id") employeeID: String,
        @Field("from_dt") fromDate: String,
        @Field("to_dt") toDate: String):Call<List<EmployeeDetail>>
}