package app.zero_one.attendance.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.zero_one.attendance.R
import app.zero_one.attendance.adapter.CalenderAdapter
import app.zero_one.attendance.api.Connection
import app.zero_one.attendance.model.CustomCalender
import app.zero_one.attendance.model.EmployeeDetail
import kotlinx.android.synthetic.main.activity_employee_detail.*
import kotlinx.android.synthetic.main.loader.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class EmployeeDetailPage : AppCompatActivity() {

    lateinit var dayLast: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_detail)

        val empId = intent.getStringExtra("emp_id")
        val empName = intent.getStringExtra("emp_name")
        val year = intent.getStringExtra("year")
        val monthNumber = intent.getStringExtra("monthNumber")
        val monthName = intent.getStringExtra("monthName")

        name.text = empName
        date.text = "$monthName $year"

        if (empId != null) {
            dayLast = getNumberOfDaysFromMonth(year!!, monthNumber!!)
            getEmployeeDetail(empId, "$year-$monthNumber-01", "$year-$monthNumber-$dayLast")
        } else Toast.makeText(this, "Invalid Employee", Toast.LENGTH_LONG).show()

    }

    private fun getEmployeeDetail(employeeID: String, fromDate: String, toDate: String) {
        val request = Connection.create().getEmployeeDetail(employeeID, fromDate, toDate)
        request.enqueue(object : Callback<List<EmployeeDetail>> {
            override fun onFailure(call: Call<List<EmployeeDetail>>, t: Throwable) {}
            override fun onResponse(
                call: Call<List<EmployeeDetail>>,
                response: Response<List<EmployeeDetail>>
            ) {
                if (response.body()!!.isNotEmpty()) {
                    val mData = ArrayList<CustomCalender>()
                    for (item in response.body()!!) {
                        val dateData = item.entry_at.split("-")
                        val day = dateData[2].substring(0, 2)
                        val hour = calcHours(item.entry_at, item.exit_at)
                        mData.add(CustomCalender(day, hour, false))
                    }
                    setRecyclerView(mData)
                } else {
                    loader.visibility = View.GONE
                    tv_nodata.visibility = View.VISIBLE
                }

            }

        })
    }

    private fun calcHours(entryAt: String, exitAt: String): String {
        val dateFormat = "yyyy-MM-dd kk:mm:ss"
        val startDate = SimpleDateFormat(dateFormat, Locale.US).parse(entryAt)
        val endDate = SimpleDateFormat(dateFormat, Locale.US).parse(exitAt)
        var secs = (endDate!!.time - startDate!!.time) / 1000
        val hours = secs / 3600
        secs %= 3600
        var mins = (secs / 60).toString()
        secs %= 60
        if (mins.toInt() < 10) mins = "0$mins"
        return "$hours.$mins"
    }

    private fun setRecyclerView(datas: ArrayList<CustomCalender>) {
        val mData = ArrayList<CustomCalender>()
        var totalHours = 0.0
        var totalPresent = 0
        var totalAbsent = 0
        for (i in 1..dayLast.toInt()) {
            var totalHour = 0.0
            var isLeave = true

            for (data in datas) {
                if (data.day.toInt() == i) {
                    totalHour += (data.hour).toDouble()
                    isLeave = false
                }
            }

            totalHours += totalHour
            if (totalHour == 0.0) totalAbsent += 1
            else totalPresent += 1

            val item =
                CustomCalender(day = i.toString(), hour = totalHour.toString(), isLeave = isLeave)
            mData.add(item)
        }

        tv_tot_hour.text = totalHours.toString()
        tv_tot_present.text = totalPresent.toString()
        tv_tot_abs.text = (totalAbsent - 9).toString()  ///saturday and sunday is considered holiday
        rv_detail.adapter = CalenderAdapter(this, mData)
        loader.visibility = View.GONE
    }

    private fun getNumberOfDaysFromMonth(year: String, month: String): String {
        val mycal = GregorianCalendar(year.toInt(), month.toInt(), 0)
        return mycal.getActualMaximum(Calendar.DAY_OF_MONTH).toString()
    }


}
