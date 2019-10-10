package app.zero_one.attendance.page

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.zero_one.attendance.R
import app.zero_one.attendance.api.Connection
import app.zero_one.attendance.model.Employee
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loader.*
import kotlinx.android.synthetic.main.row_month.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, EmployeeDetailPage::class.java)

        //setting current month as selected
        val radioButton = radio_group.getChildAt(getCurrentMonth())
        radio_group.check(radioButton.id)

        setYear()
        getEmployees()
        btn.setOnClickListener {

            val employee = employeeSpinner.selectedItem
            val year = sp_year.selectedItem


            ///get selected month (checked radio button)
            val radioButtonID = radio_group.checkedRadioButtonId
            val view = radio_group.findViewById<RadioButton>(radioButtonID)
            val index = radio_group.indexOfChild(view)
            val radio = radio_group.getChildAt(index) as RadioButton
            //get month number from text( 01 JAN => 01)
            val monthNumber = radio.text.toString().substring(0, 2)
            val monthName = radio.text.toString().substring(3)

            if (employee != null) {
                intent.putExtra("emp_id", (employee as Employee).emp_id)
                intent.putExtra("emp_name", employee.name)
                intent.putExtra("monthNumber", monthNumber)
                intent.putExtra("monthName", monthName)
                intent.putExtra("year", "" + year)
                startActivity(intent)
            } else
                Toast.makeText(this, "Select Employee", Toast.LENGTH_LONG).show()
        }//btn click listener


    }


    private fun getEmployees() {
        val request = Connection.create().getAllEmployees()
        request.enqueue(object : Callback<List<Employee>> {
            override fun onFailure(call: Call<List<Employee>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error:" + t.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }

            override fun onResponse(
                call: Call<List<Employee>>,
                response: Response<List<Employee>>
            ) {
                employeeSpinner.setTitle(resources.getString(R.string.select_employee))
                employeeSpinner.adapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_list_item_1,
                    response.body()!!.toList()
                )
                loader.visibility = View.GONE
            }

        })
    }

    private fun setYear() {
        sp_year.setTitle(resources.getString(R.string.select_year))
        val adapter = ArrayAdapter(
            this@MainActivity,
            android.R.layout.simple_list_item_1,
            (2000..3000).toList()
        )
        sp_year.adapter = adapter
        //Getting current year and setting as selected in spinner
        sp_year.setSelection(adapter.getPosition(getCurrentYear()))
    }

    private fun getCurrentYear(): Int? {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    private fun getCurrentMonth(): Int {
        return Calendar.getInstance().get(Calendar.MONTH)
    }


}
